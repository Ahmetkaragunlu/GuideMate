package com.ahmetkaragunlu.guidemate.chat.data.realtime

import com.ahmetkaragunlu.guidemate.auth.domain.session.AccessTokenProvider
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatRealtimeErrorResponseDto
import com.ahmetkaragunlu.guidemate.common.network.ApiBaseUrl
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

private const val MESSAGE_DESTINATION = "/user/queue/chat-messages"
private const val ERROR_DESTINATION = "/user/queue/chat-errors"

@Singleton
class OkHttpChatRealtimeClient
@Inject
constructor(
    okHttpClient: OkHttpClient,
    private val accessTokenProvider: AccessTokenProvider,
    @ApiBaseUrl apiBaseUrl: HttpUrl,
    private val gson: Gson,
) : ChatRealtimeClient {
    private val client =
        okHttpClient
            .newBuilder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .pingInterval(20, TimeUnit.SECONDS)
            .build()
    private val webSocketUrl = checkNotNull(apiBaseUrl.resolve("/ws"))
    private val mutableEvents = MutableSharedFlow<ChatRealtimeEvent>(extraBufferCapacity = 64)
    override val events: Flow<ChatRealtimeEvent> = mutableEvents.asSharedFlow()

    private val lock = Any()
    private var webSocket: WebSocket? = null
    private var state = ConnectionState.DISCONNECTED
    private var frameBuffer = ""
    private var disconnectRequested = false

    override fun connect() {
        val accessToken = accessTokenProvider.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            mutableEvents.tryEmit(ChatRealtimeEvent.Disconnected)
            return
        }

        synchronized(lock) {
            if (state != ConnectionState.DISCONNECTED) return
            state = ConnectionState.CONNECTING
            disconnectRequested = false
            frameBuffer = ""
            val request =
                Request
                    .Builder()
                    .url(webSocketUrl)
                    .header("Sec-WebSocket-Protocol", "v12.stomp")
                    .build()
            webSocket = client.newWebSocket(request, listener(accessToken))
        }
    }

    override fun disconnect() {
        val socket =
            synchronized(lock) {
                disconnectRequested = true
                state = ConnectionState.DISCONNECTED
                frameBuffer = ""
                webSocket.also { webSocket = null }
            }
        socket?.close(1000, "Client disconnected")
    }

    private fun listener(accessToken: String): WebSocketListener =
        object : WebSocketListener() {
            override fun onOpen(
                webSocket: WebSocket,
                response: Response,
            ) {
                webSocket.send(StompFrameCodec.connect(accessToken))
            }

            override fun onMessage(
                webSocket: WebSocket,
                text: String,
            ) {
                consumeFrames(webSocket, text)
            }

            override fun onMessage(
                webSocket: WebSocket,
                bytes: ByteString,
            ) {
                consumeFrames(webSocket, bytes.utf8())
            }

            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String,
            ) {
                finishConnection(webSocket)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?,
            ) {
                finishConnection(webSocket)
            }
        }

    private fun consumeFrames(
        socket: WebSocket,
        data: String,
    ) {
        val frames =
            synchronized(lock) {
                if (webSocket !== socket) return
                val (decodedFrames, remaining) = StompFrameCodec.consume(frameBuffer + data)
                frameBuffer = remaining
                decodedFrames
            }
        frames.forEach { frame -> handleFrame(socket, frame) }
    }

    private fun handleFrame(
        socket: WebSocket,
        frame: StompFrame,
    ) {
        when (frame.command) {
            "CONNECTED" -> {
                synchronized(lock) {
                    if (webSocket !== socket) return
                    state = ConnectionState.CONNECTED
                }
                socket.send(StompFrameCodec.subscribe("chat-messages", MESSAGE_DESTINATION))
                socket.send(StompFrameCodec.subscribe("chat-errors", ERROR_DESTINATION))
                mutableEvents.tryEmit(ChatRealtimeEvent.Connected)
            }
            "MESSAGE" -> handleMessage(frame)
            "ERROR" -> {
                mutableEvents.tryEmit(ChatRealtimeEvent.Error(code = null))
                socket.close(1002, "STOMP protocol error")
            }
        }
    }

    private fun handleMessage(frame: StompFrame) {
        runCatching {
            when (frame.headers["destination"]) {
                MESSAGE_DESTINATION ->
                    ChatRealtimeEvent.MessageReceived(
                        gson.fromJson(frame.body, ChatMessageResponseDto::class.java),
                    )
                ERROR_DESTINATION ->
                    ChatRealtimeEvent.Error(
                        gson.fromJson(frame.body, ChatRealtimeErrorResponseDto::class.java).code,
                    )
                else -> null
            }
        }.getOrNull()?.let(mutableEvents::tryEmit)
    }

    private fun finishConnection(socket: WebSocket) {
        val shouldNotify =
            synchronized(lock) {
                if (webSocket !== socket) return
                webSocket = null
                state = ConnectionState.DISCONNECTED
                frameBuffer = ""
                !disconnectRequested
            }
        if (shouldNotify) mutableEvents.tryEmit(ChatRealtimeEvent.Disconnected)
    }

    private enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
    }
}
