package com.ahmetkaragunlu.guidemate.common.network.realtime

internal data class StompFrame(
    val command: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
)

internal object StompFrameCodec {
    private const val FRAME_END = '\u0000'

    fun connect(accessToken: String): String =
        encode(
            StompFrame(
                command = "CONNECT",
                headers =
                    linkedMapOf(
                        "accept-version" to "1.2",
                        "heart-beat" to "10000,10000",
                        "Authorization" to "Bearer $accessToken",
                    ),
            ),
        )

    fun subscribe(
        id: String,
        destination: String,
    ): String =
        encode(
            StompFrame(
                command = "SUBSCRIBE",
                headers =
                    linkedMapOf(
                        "id" to id,
                        "ack" to "auto",
                        "destination" to destination,
                    ),
            ),
        )

    fun consume(buffer: String): Pair<List<StompFrame>, String> {
        val frames = mutableListOf<StompFrame>()
        var remaining = buffer
        while (true) {
            val frameEnd = remaining.indexOf(FRAME_END)
            if (frameEnd < 0) break
            val rawFrame = remaining.substring(0, frameEnd).trimStart('\n', '\r')
            remaining = remaining.substring(frameEnd + 1)
            if (rawFrame.isNotBlank()) decode(rawFrame)?.let(frames::add)
        }
        return frames to remaining
    }

    private fun encode(frame: StompFrame): String =
        buildString {
            append(frame.command)
            append('\n')
            frame.headers.forEach { (name, value) ->
                append(name)
                append(':')
                append(value)
                append('\n')
            }
            append('\n')
            append(frame.body)
            append(FRAME_END)
        }

    private fun decode(rawFrame: String): StompFrame? {
        val normalized = rawFrame.replace("\r\n", "\n")
        val separator = normalized.indexOf("\n\n")
        val head = if (separator >= 0) normalized.substring(0, separator) else normalized
        val body = if (separator >= 0) normalized.substring(separator + 2) else ""
        val lines = head.lineSequence().toList()
        val command = lines.firstOrNull()?.trim().orEmpty()
        if (command.isEmpty()) return null
        val headers =
            lines.drop(1).mapNotNull { line ->
                val colon = line.indexOf(':')
                if (colon <= 0) null else line.substring(0, colon) to line.substring(colon + 1)
            }.toMap()
        return StompFrame(command = command, headers = headers, body = body)
    }
}
