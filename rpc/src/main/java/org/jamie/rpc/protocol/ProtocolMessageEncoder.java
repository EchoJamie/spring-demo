package org.jamie.rpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.jamie.rpc.exception.SerializerException;
import org.jamie.rpc.serializer.Serializer;
import org.jamie.rpc.serializer.SerializerFactory;
import org.jamie.rpc.spi.SpiLoader;

/**
 * 消息编码器
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 15:16
 */
public class ProtocolMessageEncoder {

    public static Buffer encode(ProtocolMessage<?> message) throws SerializerException {
        if (message.getHeader() == null || message.getBody() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = message.getHeader();

        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer()
                .appendShort(header.getMagic())
                .appendByte(header.getVersion())
                .appendByte(header.getSerializer())
                .appendByte(header.getTypeAndCompress())
                .appendByte(header.getStatus())
                .appendLong(header.getRequestId());

        Serializer serializer = SerializerFactory.getSerializer(header.getSerializer());

        byte[] bodyBytes = serializer.serialize(message.getBody());
        // 写入 body的长度和数据
        return buffer.appendInt(bodyBytes.length)
                .appendBytes(bodyBytes);
    }
}
