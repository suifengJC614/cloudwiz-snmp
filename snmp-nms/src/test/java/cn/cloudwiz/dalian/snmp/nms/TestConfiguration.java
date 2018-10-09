package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.commons.core.autoconfigure.EnableCloudwizCommons;
import cn.cloudwiz.dalian.commons.projection.autoconfigure.EnableProjection;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableProjection
@EnableCloudwizCommons
@EnableTransactionManagement
@ComponentScan(basePackageClasses = TestConfiguration.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class TestConfiguration {
}
