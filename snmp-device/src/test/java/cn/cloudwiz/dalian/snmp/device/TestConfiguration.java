package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.projection.autoconfigure.EnableProjection;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableProjection
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = TestConfiguration.class)
public class TestConfiguration {
}
