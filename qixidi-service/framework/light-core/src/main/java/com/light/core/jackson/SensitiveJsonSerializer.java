package com.light.core.jackson;

import com.light.core.annotation.Sensitive;
import com.light.core.core.service.SensitiveService;
import com.light.core.enums.SensitiveStrategy;
import com.light.core.utils.spring.SpringUtils;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.Objects;

/**
 * 数据脱敏json序列化工具（Jackson 3：ContextualSerializer 能力并入 ValueSerializer）
 *
 * @author Yjoioooo
 */
public class SensitiveJsonSerializer extends ValueSerializer<String> {

    private SensitiveStrategy strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializationContext serializers) {
        SensitiveService sensitiveService = SpringUtils.getBean(SensitiveService.class);
        if (sensitiveService.isSensitive()) {
            gen.writeString(value);
        } else {
            gen.writeString(strategy.desensitizer().apply(value));
        }

    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext ctxt, BeanProperty property) {
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return ctxt.findValueSerializer(property.getType());
    }
}
