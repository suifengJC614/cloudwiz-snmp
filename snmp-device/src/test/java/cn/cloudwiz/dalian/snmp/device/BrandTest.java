package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class BrandTest {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private DeviceBrandService brandbs;
    @Value("classpath:test_logo.jpg")
    private Resource testLogo;
    @Value("classpath:test_data.json")
    private Resource testJson;

    @Ignore
    @Test
    public void remove(){
        brandbs.remove(2L);
    }

    @Ignore
    @Test
    public void getBrand(){
        DeviceBrand brand = brandbs.getDeviceBrand(1L);
        System.out.println(JsonUtils.toJson(brand));
    }

    @Ignore
    @Test
    public void findByClassify() {
        List<DeviceBrand> brands = brandbs.getDeviceBrands(DeviceClass.NETWORK);
        brands.forEach(item -> {
            System.out.println(JsonUtils.toJson(item));
        });
    }

    @Ignore
    @Test
    public void save() throws IOException {
        String path = brandbs.uploadLogo(testLogo.getInputStream(), "jpg");
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        DeviceBrand brand = proxyFactory.createProjection(MockLogoBrand.class, Arrays.asList(testData.getDeviceBrand(), path));
        System.out.println(JsonUtils.toJson(brand));
        Long savedKey = brandbs.save(brand);
        System.out.println(String.format("save success, savedKey is %s", savedKey));
    }

    @Ignore
    @Test
    public void updateLogo() throws IOException {
        String path = brandbs.uploadLogo(testLogo.getInputStream(), "jpg");
        System.out.println(path);
    }

    public interface MockLogoBrand extends DeviceBrand {
        @Value("#{target[1]}")
        public String getLogoImage();
    }

}
