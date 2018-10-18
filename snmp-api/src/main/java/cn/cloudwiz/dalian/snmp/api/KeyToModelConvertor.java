package cn.cloudwiz.dalian.snmp.api;

import cn.cloudwiz.dalian.commons.projection.ProjectionConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyToModelConvertor implements ProjectionConverter {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Method method, Class<T> returnType, Object origin) {
        Map<String, Object> datas = new HashMap<>();
        datas.put("key", ((Number) origin).longValue());
        return (T) datas;
    }

    @Override
    public boolean canConvert(Method method, Class<?> type, Object origin) {
        return type.isInterface() && origin instanceof Number;
    }

}
