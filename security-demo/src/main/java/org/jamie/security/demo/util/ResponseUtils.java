package org.jamie.security.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jamie.security.demo.exception.JamieException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 响应工具类
 * @date 2023/5/28 16:13
 */
@Component
public final class ResponseUtils {

    private static ObjectMapper objectMapper;

    public ResponseUtils(ObjectMapper objectMapper) {
        ResponseUtils.objectMapper = objectMapper;
    }

    public static void responseJson(HttpServletResponse response, ResponseVO<?> responseVO) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(responseVO));
        } catch (IOException e) {
            throw new JamieException("序列化JSON出现异常: " + e.getMessage(), e);
        }
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class ResponseVO<T> {
        private Integer code;
        private String msg;
        private T data;

        public static <T> ResponseVO<T> success(String msg, T data) {
            return of(0, msg, data);
        }

        public static <T> ResponseVO<T> success(String msg) {
            return of(0, msg, null);
        }

        public static ResponseVO failure(Integer code, String msg) {
            return of(code, msg, null);
        }
    }
}
