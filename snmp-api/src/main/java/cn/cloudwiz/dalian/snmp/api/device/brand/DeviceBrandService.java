package cn.cloudwiz.dalian.snmp.api.device.brand;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DeviceBrandService {

    public String uploadLogo(InputStream input, String fileName) throws IOException;

    public Long save(DeviceBrand brand) throws IOException;

    public List<DeviceBrand> getDeviceBrands(DeviceClass classify);

    public DeviceBrand getDeviceBrand(Long key);

    public void remove(Long key);

}
