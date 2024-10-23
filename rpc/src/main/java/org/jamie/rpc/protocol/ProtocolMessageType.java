package org.jamie.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型 4 bit
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 13:10
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageType {

    REQUEST(0, "请求"),
    RESPONSE(1, "响应"),
    HEARTBEAT(2, "心跳"),
    UNKNOWN(3, "其它"),
    ;

    private final int code;

    private final String desc;

    public static ProtocolMessageType of(byte code) {
        for (ProtocolMessageType type : ProtocolMessageType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown code:" + code);
    }
}
