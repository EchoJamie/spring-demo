package org.jamie.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码 1B
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 12:59
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageStatus {

    OK(20, "ok"),
    BAD_REQUEST(40, "bad request"),
    BAD_RESPONSE(50, "bad response"),
    ;

    private final int code;

    private final String desc;

    public static ProtocolMessageStatus of(byte code) {
        for (ProtocolMessageStatus status : ProtocolMessageStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
