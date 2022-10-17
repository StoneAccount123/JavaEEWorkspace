package edu.whu.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author myAdministrator
 * 读取配置文件的类
 */
@Component
@Data
@PropertySource({"classpath:myapp.properties"})
public class FileReader {

    FileReader(){
        System.out.println("初始化了");
    }

    @Value("${bootstrapClass}")
    private String bootstrapClass;

}