package com.walkmind.extensions.marshallers

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

interface ByteArrayMarshaller<T> {
    fun encode(value: T): ByteArray
    fun decode(value: ByteArray): T

    fun <V>bimap(enc: (V) -> T, dec: (T) -> V): ByteArrayMarshaller<V> = object : ByteArrayMarshaller<V> {
        override fun encode(value: V): ByteArray {
            return this@ByteArrayMarshaller.encode(enc(value))
        }

        override fun decode(value: ByteArray): V {
            return dec(this@ByteArrayMarshaller.decode(value))
        }
    }
}

object DefaultLongMarshaller : ByteArrayMarshaller<Long> {
    override fun encode(value: Long): ByteArray {
        return ByteBuffer.allocate(Long.SIZE_BYTES).putLong(value).array()
    }

    override fun decode(value: ByteArray): Long {
        return ByteBuffer.allocate(Long.SIZE_BYTES).put(value).flip().long
    }
}

object DefaultStringMarshaller : ByteArrayMarshaller<String> {
    override fun encode(value: String): ByteArray {
        return value.toByteArray(StandardCharsets.UTF_8)
    }

    override fun decode(value: ByteArray): String {
        return String(value, StandardCharsets.UTF_8)
    }
}