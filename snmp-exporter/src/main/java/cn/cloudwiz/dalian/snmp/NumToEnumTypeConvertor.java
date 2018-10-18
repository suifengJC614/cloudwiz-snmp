package cn.cloudwiz.dalian.snmp;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.commons.projection.ProjectionConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class NumToEnumTypeConvertor implements ProjectionConverter {
    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Method method, Class<T> returnType, Object origin) {
        int value = ((Number) origin).intValue();
        for (T item : returnType.getEnumConstants()) {
            Integer code = ((EnumType<Integer>) item).getJdbcValue();
            if (Objects.equals(value, code)) {
                return (T) item;
            }
        }
        return null;
    }

    @Override
    public boolean canConvert(Method method, Class<?> type, Object origin) {
        return type.isEnum() && EnumType.class.isAssignableFrom(type) && origin instanceof Number;
    }
}
