package cn.cloudwiz.dalian.snmp.utils;

import org.springframework.data.projection.ProjectionFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProjectionUtils {

    public static <T> T mergeKey(Class<T> type, Object keyValue, Object origin, ProjectionFactory proxyFactory){
        Map<String, Object> override = new HashMap<>();
        override.put("key", keyValue);
        return proxyFactory.createProjection(type, Arrays.asList(override, origin));
    }

}
