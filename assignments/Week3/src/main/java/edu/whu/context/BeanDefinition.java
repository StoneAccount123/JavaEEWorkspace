package edu.whu.context;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author myAdministrator
 */
@Data
@AllArgsConstructor
public class BeanDefinition {
    private String beanName;
    private Class beanClass;
}