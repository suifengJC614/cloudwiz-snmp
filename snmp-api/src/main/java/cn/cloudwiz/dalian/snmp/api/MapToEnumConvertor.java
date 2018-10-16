package cn.cloudwiz.dalian.snmp.api;

import cn.cloudwiz.dalian.commons.projection.ProjectionConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@Component
public class MapToEnumConvertor implements ProjectionConverter {
    @Override
    public <T> T convert(Method method, Class<T> returnType, Object origin) {
        Map<?, ?> datas = (Map<?, ?>) origin;
        Object code = datas.get("code");
        T[] enums = returnType.getEnumConstants();
        for(T item : returnType.getEnumConstants()){
            EnumItem<?> enumItem = (EnumItem<?>) item;
            if(Objects.equals(code, enumItem.getCode())){
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean canConvert(Method method, Class<?> type, Object origin) {
        if(type.isEnum() && EnumItem.class.isAssignableFrom(type) && origin instanceof Map){
            Map<?, ?> datas = (Map<?, ?>) origin;
            return datas.containsKey("code");
        }
        return false;
    }
}
