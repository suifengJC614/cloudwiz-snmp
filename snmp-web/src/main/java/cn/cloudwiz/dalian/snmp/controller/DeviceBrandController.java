package cn.cloudwiz.dalian.snmp.controller;

import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("brand")
public class DeviceBrandController {

    @Autowired
    private DeviceBrandService brandbs;

    @RequestMapping(path = "logo", method = RequestMethod.POST)
    public RestResponse<String> uploadLogo(@RequestParam("file") MultipartFile file){
        return RestResponse.success("");
    }

    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<Long> create(@RequestBody DeviceBrand brand){
        return RestResponse.success(1L);
    }

}
