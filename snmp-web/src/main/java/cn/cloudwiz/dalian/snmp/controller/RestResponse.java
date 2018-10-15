package cn.cloudwiz.dalian.snmp.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

@ApiModel("响应数据容器")
public class RestResponse<T> {

    public static final int STATUS_SUCCESS = 200;

    @ApiModelProperty(value = "元数据信息，状态等")
    private Map<String, Object> meta;
    @ApiModelProperty(value = "数据体")
    private T body;

    private RestResponse(T body){
        this.meta = new HashMap<>();
        this.body = body;
    }

    public void setMeta(String code, String value){
        meta.put(code, value);
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public T getBody() {
        return body;
    }

    private static void preparedSuccessMeta(RestResponse<?> response){
        response.getMeta().put("status", STATUS_SUCCESS);
    }

    public static RestResponse<Void> success(){
        RestResponse<Void> result = new RestResponse<>(null);
        preparedSuccessMeta(result);
        return result;
    }

    public static <T> RestResponse<T> success(T body){
        RestResponse<T> result = new RestResponse<>(body);
        preparedSuccessMeta(result);
        return result;
    }

    public static RestResponse<Void> error(String status){
        RestResponse<Void> result = new RestResponse<>(null);
        preparedSuccessMeta(result);
        result.meta.put("status", status);
        return result;
    }

}
