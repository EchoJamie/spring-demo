package org.jamie.rpc.serializer.impl;

import org.jamie.rpc.exception.SerializerException;
import org.jamie.rpc.serializer.Serializer;

import java.io.*;

/**
 * JDK 序列化
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 16:59
 */
public class JdkSerializer implements Serializer {
    /**
     * @param object
     * @param <T>
     * @return
     * @throws SerializerException
     */
    @Override
    public <T> byte[] serialize(T object) throws SerializerException {

        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ) {
            objectOutputStream.writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e);
        }
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
        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            return clazz.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializerException(e);
        }
    }
}
