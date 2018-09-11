package cn.cloudwiz.dalian.snmp;

import cn.cloudwiz.dalian.commons.core.autoconfigure.EnableCloudwizCommons;
import cn.cloudwiz.dalian.commons.projection.autoconfigure.EnableProjection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProjection
@EnableCloudwizCommons
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
