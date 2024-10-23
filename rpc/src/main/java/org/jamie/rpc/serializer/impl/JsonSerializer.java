package org.jamie.rpc.serializer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jamie.rpc.exception.SerializerException;
import org.jamie.rpc.model.RpcRequest;
import org.jamie.rpc.model.RpcResponse;
import org.jamie.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * JSON 序列化
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 18:09
 */
public class JsonSerializer implements Serializer {
    /**
     * @param object
     * @param <T>
     * @return
     * @throws SerializerException
     */
    @Override
    public <T> byte[] serialize(T object) throws SerializerException {
        return writeJson(object);
    }

    /**
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializerException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException {
        T obj = readJson(bytes, clazz);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, clazz);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, clazz);
        }
        return obj;
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcRequest rpc 请求
     * @param type       类型
     * @return {@link T}
     * @throws SerializerException IO异常
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws SerializerException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getParameters();

        // 循环处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            // 如果类型不同，则重新处理一下类型
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] argBytes = writeJson(args[i]);
                args[i] = readJson(argBytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcResponse rpc 响应
     * @param type        类型
     * @return {@link T}
     * @throws SerializerException IO异常
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws SerializerException {
        // 处理响应数据
        byte[] dataBytes = writeJson(rpcResponse.getResult());
        rpcResponse.setResult(readJson(dataBytes, rpcResponse.getResultClass()));
        return type.cast(rpcResponse);
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private <T> T readJson(byte[] bytes, Class<T> clazz) throws SerializerException {
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }

    private <T> byte[] writeJson(T object) throws SerializerException {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }
}
