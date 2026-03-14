package com.light.web.response;

import cn.hutool.core.text.AntPathMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 接口统一格式返回
 */
@ControllerAdvice(basePackages = "com.qixidi")
public class ResponseAdvice implements ResponseBodyAdvice {
    @Autowired
    private ObjectMapper objectMapper;

    // 用于匹配路径的 Ant 风格路径匹配器
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 表示对哪些请求执行 统一数据返回格式处理 (这里就简单的全部执行)
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //获取当前请求的路径
        String requestPath = request.getURI().getPath();

        // 判断路径是否匹配 /whitelist/original/**（特殊路径）,直接返回原始 body，不做处理
//        if (PATH_MATCHER.match("/whitelist/original/**", requestPath)) {
//            return body;
//        }

        // 非特殊路径：执行统一响应处理
        if (body instanceof TableDataInfo<?>) {
            return body;
        } else if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(R.ok(body));
        }
        return R.ok(body);
    }
}
