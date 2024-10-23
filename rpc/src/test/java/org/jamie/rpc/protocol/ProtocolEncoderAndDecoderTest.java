package org.jamie.rpc.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.buffer.Buffer;
import org.jamie.rpc.exception.SerializerException;
import org.jamie.rpc.model.RpcRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 * 序列化与反序列化测试
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 17:27
 */
public class ProtocolEncoderAndDecoderTest {

    @Test
    public void testEncoder() throws SerializerException, JsonProcessingException {
        ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) 0);
        header.setType((byte) ProtocolMessageType.REQUEST.getCode());
        header.setCompress((byte) ProtocolMessageCompress.NONE.getCode());
        header.setStatus((byte) ProtocolMessageStatus.OK.getCode());
        header.setRequestId(1L);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion("1.0.0");
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setParameters(new Object[]{"aaa", "bbb"});
        message.setHeader(header);
        message.setBody(rpcRequest);

        Buffer buffer = ProtocolMessageEncoder.encode(message);
        System.out.println(buffer.toString());
        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(buffer);
        System.out.println(new ObjectMapper().writeValueAsString(decode));
        Assert.assertNotNull(decode);
        try {
            byte[] array = buffer.getByteBuf().array();
            FileOutputStream fileOutputStream = new FileOutputStream("/Users/jamie/namespace/spring-demo/rpc/src/test/resources/test.txt");
            fileOutputStream.write(array);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
