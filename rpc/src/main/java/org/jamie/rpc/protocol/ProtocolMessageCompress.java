package org.jamie.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息压缩常量
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/10/8 17:36
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageCompress {

    NONE(0, "不压缩"),
    GZIP(1, "GZIP压缩"),
    SNAPPY(2, "SNAPPY压缩"),
    LZ4(3, "LZ4压缩"),
    LZMA(4, "LZMA压缩"),
    LZF(5, "LZF压缩"),
    LZ4_HC(6, "LZ4_HC压缩"),
    ZSTD(7, "ZSTD压缩"),
    ;

    private final int code;
    private final String desc;

    public static ProtocolMessageCompress of(int code) {
        for (ProtocolMessageCompress compress : values()) {
            if (compress.code == code) {
                return compress;
            }
        }
        return null;
    }
}
