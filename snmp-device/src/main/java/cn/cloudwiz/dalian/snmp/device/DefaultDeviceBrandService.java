package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.core.BaseData;
import cn.cloudwiz.dalian.commons.core.exceptions.DataNotFoundException;
import cn.cloudwiz.dalian.commons.utils.UUIDUtils;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceClass;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceBrandDao;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultDeviceBrandService implements DeviceBrandService {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private DeviceBrandDao brandDao;
    @Value("${device.logo-root}")
    private String rootPath;

    @Override
    public DeviceBrand getDeviceBrand(Long key) {
        Assert.notNull(key, "getDeviceBrand, key cannot be null");
        BrandEntity entity = brandDao.getEntityByKey(key);
        if (entity == null) {
            throw new DataNotFoundException(String.format("getDeviceBrand for key[%s], but it is not found", key));
        }
        return projection(DeviceBrand.class, entity);
    }

    @Override
    public List<DeviceBrand> getDeviceBrands(DeviceClass classify) {
        List<BrandEntity> result = brandDao.getListByClassify(classify);
        return result.stream().map(item -> projection(DeviceBrand.class, item))
                .collect(Collectors.toList());
    }

    protected <T extends DeviceBrand> T projection(Class<T> type, BrandEntity entity) {
        return proxyFactory.createNullableProjection(type, entity);
    }

    @Override
    public String uploadLogo(InputStream input, String suffix) throws IOException {
        Assert.notNull(input, "uploadLogo, input cannot be null");
        Assert.hasText(suffix, "uploadLogo, suffix cannot be null");
        File temp = FileUtils.getTempDirectory();
        String fileName = generateFileName(suffix);
        File file = new File(FileUtils.getTempDirectory(), fileName);
        FileUtils.copyInputStreamToFile(input, file);
        return fileName;
    }

    protected String generateFileName(String suffix) {
        return String.format("%s.%s", UUIDUtils.randomUUID(), suffix.toLowerCase());
    }

    @Override
    public Long save(DeviceBrand brand) throws IOException {
        Assert.notNull(brand, "save brand, brand cannot be null");
        Long key = brand.getKey();
        BrandEntity entity = null;
        if (key != null) {
            entity = brandDao.getEntityByKey(key);
        }
        if (entity == null) {
            entity = BaseData.merge(BrandEntity.class, brand);
            moveLogoImage(entity);
            brandDao.insert(entity);
        } else {
            if (ObjectUtils.notEqual(brand.getLogoImage(), entity.getLogoImage())) {
                File old = new File(rootPath, entity.getLogoImage());
                FileUtils.deleteQuietly(old);
            }
            entity.merge(brand);
            moveLogoImage(entity);
            brandDao.update(entity);
        }
        return entity.getPrimaryKey();
    }

    protected void moveLogoImage(BrandEntity entity) throws IOException {
        String logoImage = entity.getLogoImage();
        if (StringUtils.isNotEmpty(logoImage)) {
            File file = new File(FileUtils.getTempDirectory(), logoImage);
            if (file.exists()) {
                FileUtils.moveFile(file, new File(rootPath, file.getName()));
            } else {
                entity.setLogoImage(null);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void remove(Long key) {
        if (key != null) {
            BrandEntity entity = brandDao.getEntityByKey(key);
            if (entity != null) {
                brandDao.delete(key);
                String logoImage = entity.getLogoImage();
                if (StringUtils.isNotEmpty(logoImage)) {
                    FileUtils.deleteQuietly(new File(rootPath, logoImage));
                }
            }
        }
    }
}
