package org.jamie.rpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.jamie.rpc.exception.SerializerException;
import org.jamie.rpc.model.RpcRequest;
import org.jamie.rpc.model.RpcResponse;
import org.jamie.rpc.serializer.Serializer;
import org.jamie.rpc.serializer.SerializerFactory;
import org.jamie.rpc.spi.SpiLoader;

/**
 * 消息解码器
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 15:16
 */
public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws SerializerException {
        // 分别从指定位置读出 Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        short magic = buffer.getShort(0);
        // 校验魔数
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(2));
        header.setSerializer(buffer.getByte(3));
        header.setTypeAndCompress(buffer.getByte(4));
        header.setStatus(buffer.getByte(5));
        header.setRequestId(buffer.getLong(6));

        Serializer serializer = SerializerFactory.getSerializer(header.getSerializer());

        byte[] bodyBytes = buffer.getBytes(ProtocolConstant.HEAD_LENGTH, buffer.length());
        ProtocolMessageType messageType = ProtocolMessageType.of(header.getType());
        switch (messageType) {
            case REQUEST:
                // 请求消息
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                // 响应消息
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEARTBEAT:
                // 心跳消息
            case UNKNOWN:
                // 其它消息
            default:
                throw new RuntimeException("不支持的消息类型");
        }
    }
}
