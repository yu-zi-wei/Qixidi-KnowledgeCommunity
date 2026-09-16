package com.light.core.config;

import com.light.core.jackson.BigNumberSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * jackson 配置（Jackson 3 / tools.jackson）
 * 通过官方 JsonMapperBuilderCustomizer 挂到自动装配的 JsonMapper 上，spring.jackson.* 配置继续生效
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JacksonConfig {

    private final JacksonProperties jacksonProperties;

    @Bean
    public JsonMapperBuilderCustomizer bigNumberJsonMapperCustomizer() {
        return builder -> {
            // 全局配置序列化返回 JSON 处理
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
            simpleModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
            simpleModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
            simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(jacksonProperties.getDateFormat());
            simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
            simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
            builder.addModule(simpleModule);
            builder.defaultTimeZone(TimeZone.getDefault());
            log.info("初始化 jackson 配置");
        };
    }

}
