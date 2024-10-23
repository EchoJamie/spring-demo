package org.jamie.rpc.serializer;

import org.jamie.rpc.serializer.impl.JdkSerializer;
import org.jamie.rpc.serializer.impl.JsonSerializer;

/**
 * 序列化工厂
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 17:07
 */
public class SerializerFactory {

    public static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getSerializer(byte serializer) {
        return new JsonSerializer();
    }

}
