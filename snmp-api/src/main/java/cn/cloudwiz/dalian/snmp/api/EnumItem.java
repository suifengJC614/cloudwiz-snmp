package cn.cloudwiz.dalian.snmp.api;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface EnumItem<T> {

    public static final String EMPTY_ENUM_ITEM_CODE = "<empty>";

    public T getCode();
    public String getLabel();

    @JsonValue
    default Map<String, Object> toJson(){
        Map<String, Object> result = new HashMap<>();
        result.put("code", getCode());
        result.put("label", getLabel());
        return result;
    }

    public static <E, T extends EnumItem<E>> T codeOf(E code, Class<T> type){
        if(type != null && type.isEnum()){
            for(T item : type.getEnumConstants()){
                if(Objects.equals(item.getCode(), code)){
                    return item;
                }
            }
        }
        return null;
    }

}
