package com.ahmetkaragunlu.guidemate.common.network.realtime

import com.ahmetkaragunlu.guidemate.auth.domain.session.AccessTokenProvider
import com.ahmetkaragunlu.guidemate.common.network.ApiBaseUrl
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

private val SUBSCRIPTIONS =
    linkedMapOf(
        "chat-messages" to RealtimeDestination.CHAT_MESSAGES,
        "chat-participant-updates" to RealtimeDestination.CHAT_PARTICIPANT_UPDATES,
        "chat-errors" to RealtimeDestination.CHAT_ERRORS,
        "notifications" to RealtimeDestination.NOTIFICATIONS,
    )

@Singleton
class OkHttpRealtimeClient
@Inject
constructor(
    okHttpClient: OkHttpClient,
    private val accessTokenProvider: AccessTokenProvider,
    @ApiBaseUrl apiBaseUrl: HttpUrl,
) : RealtimeClient {
    private val client =
        okHttpClient
            .newBuilder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .pingInterval(20, TimeUnit.SECONDS)
            .build()
    private val webSocketUrl = checkNotNull(apiBaseUrl.resolve("/ws"))
    private val mutableEvents = MutableSharedFlow<RealtimeEvent>(extraBufferCapacity = 64)
    override val events: Flow<RealtimeEvent> = mutableEvents.asSharedFlow()

    private val lock = Any()
    private var webSocket: WebSocket? = null
    private var state = ConnectionState.DISCONNECTED
    private var frameBuffer = ""
    private var disconnectRequested = false
    private var connectedAccessToken: String? = null

    override fun connect() {
        val accessToken = accessTokenProvider.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            mutableEvents.tryEmit(RealtimeEvent.Disconnected)
            return
        }

        val previousSocket =
            synchronized(lock) {
                if (state != ConnectionState.DISCONNECTED && connectedAccessToken == accessToken) {
                    return
                }
                val previous = webSocket
                state = ConnectionState.CONNECTING
                disconnectRequested = false
                frameBuffer = ""
                connectedAccessToken = accessToken
                val request =
                    Request
                        .Builder()
                        .url(webSocketUrl)
                        .header("Sec-WebSocket-Protocol", "v12.stomp")
                        .build()
                webSocket = client.newWebSocket(request, listener(accessToken))
                previous
            }
        previousSocket?.close(1000, "Session changed")
    }

    override fun disconnect() {
        val socket =
            synchronized(lock) {
                disconnectRequested = true
                state = ConnectionState.DISCONNECTED
                frameBuffer = ""
                connectedAccessToken = null
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
                SUBSCRIPTIONS.forEach { (id, destination) ->
                    socket.send(StompFrameCodec.subscribe(id, destination))
                }
                mutableEvents.tryEmit(RealtimeEvent.Connected)
            }
            "MESSAGE" ->
                frame.headers["destination"]?.let { destination ->
                    mutableEvents.tryEmit(
                        RealtimeEvent.Message(destination = destination, body = frame.body),
                    )
                }
            "ERROR" -> {
                mutableEvents.tryEmit(RealtimeEvent.ProtocolError)
                socket.close(1002, "STOMP protocol error")
            }
        }
    }

    private fun finishConnection(socket: WebSocket) {
        val shouldNotify =
            synchronized(lock) {
                if (webSocket !== socket) return
                webSocket = null
                state = ConnectionState.DISCONNECTED
                frameBuffer = ""
                connectedAccessToken = null
                !disconnectRequested
            }
        if (shouldNotify) mutableEvents.tryEmit(RealtimeEvent.Disconnected)
    }

    private enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
    }
}
