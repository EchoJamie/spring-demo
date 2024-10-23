package org.jamie.rpc.serializer;

import org.jamie.rpc.exception.SerializerException;

/**
 * 序列化器接口
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 13:06
 */
public interface Serializer {


    <T> byte[] serialize(T object) throws SerializerException;


    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException;
}
