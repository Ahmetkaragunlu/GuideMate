package com.ahmetkaragunlu.guidemate.common.network.realtime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StompFrameCodecTest {
    @Test
    fun `connect frame carries bearer token and stomp version`() {
        val frame = StompFrameCodec.connect("jwt-token")

        assertTrue(frame.startsWith("CONNECT\n"))
        assertTrue(frame.contains("accept-version:1.2"))
        assertTrue(frame.contains("Authorization:Bearer jwt-token"))
        assertTrue(frame.endsWith('\u0000'))
    }

    @Test
    fun `consume keeps partial frame and decodes following payload`() {
        val firstPart = "MESSAGE\ndestination:/user/queue/chat-messages\n\n{\"messageId\":"
        val (firstFrames, remaining) = StompFrameCodec.consume(firstPart)

        assertTrue(firstFrames.isEmpty())
        assertEquals(firstPart, remaining)

        val (frames, finalRemaining) = StompFrameCodec.consume(remaining + "\"1\"}\u0000\n")

        assertEquals(1, frames.size)
        assertEquals("MESSAGE", frames.single().command)
        assertEquals("/user/queue/chat-messages", frames.single().headers["destination"])
        assertEquals("{\"messageId\":\"1\"}", frames.single().body)
        assertEquals("\n", finalRemaining)
    }
}
