package cn.cloudwiz.dalian.snmp.config;

import cn.cloudwiz.dalian.commons.projection.ProjectionConverter;
import cn.cloudwiz.dalian.commons.projection.ProxyProjectionFactoryBean.DefaultConverter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
public class StringToDateConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Date.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        ProjectionConverter converter = DefaultConverter.STRING_TO_DATE;
        if(converter.canConvert(null, Date.class, source)){
            return converter.convert(null, Date.class, source);
        }
        return null;
    }

}
