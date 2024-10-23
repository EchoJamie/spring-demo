package org.jamie.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 协议消息
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 13:14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    private Header header;

    private T body;

    /**
     * 消息头 总共18 byte
     */
    @Getter
    @Setter
    public static class Header {
        /**
         * 魔数 保证安全性 16 bit
         */
        private short magic;
        /**
         * 版本号 8 bit
         */
        private byte version;
        /**
         * 序列化类型 8 bit
         */
        private byte serializer;
        /**
         * 消息类型 高 4 bit
         * 压缩类型 低 4 bit
         */
        private byte typeAndCompress;
        /**
         * 状态码 8 bit
         */
        private byte status;
        /**
         * 请求id 64 bit
         */
        private long requestId;
        /**
         * 消息体长度 32 bit
         */
        private int bodyLength;

        public byte getType() {
            return (byte) (typeAndCompress >> 4);
        }

        public byte getCompress() {
            return (byte) (typeAndCompress & 0x0F);
        }

        public void setType(byte type) {
            typeAndCompress = (byte) ((type << 4) | (typeAndCompress & 0x0F));
        }

        public void setCompress(byte compress) {
            typeAndCompress = (byte) ((compress & 0x0F) | (typeAndCompress & 0xF0));
        }
    }
}
