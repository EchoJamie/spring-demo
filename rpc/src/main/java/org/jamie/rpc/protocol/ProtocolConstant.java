package org.jamie.rpc.protocol;

/**
 * 协议常量
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 12:52
 */
public interface ProtocolConstant {

    /**
     * 协议头长度
     */
    byte HEAD_LENGTH = 18;

    /**
     * 协议魔数 2Byte
     */
    short PROTOCOL_MAGIC = 0x12AD;

    /**
     * 协议版本号 1B
     */
    byte PROTOCOL_VERSION = 0x1;


}
