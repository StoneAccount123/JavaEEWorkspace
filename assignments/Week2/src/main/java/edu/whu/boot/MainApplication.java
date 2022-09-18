package edu.whu.boot;

import edu.whu.annotation.InitMethod;
import edu.whu.config.Config;
import edu.whu.config.FileReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.lang.reflect.Method;

/**
 * @author myAdministrator
 */
public class MainApplication {
    
    public Class<?> getMyClass(){
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        FileReader bean = context.getBean(FileReader.class);
        try {
             return Class.forName(bean.getBootstrapClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object creatObj(){
        try {
            return getMyClass().newInstance();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void invokeMethod(Object... args) {
        Object instance = creatObj();
        Method[] methods = getMyClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(InitMethod.class)){
                try {
                    method.invoke(instance,args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new MainApplication().invokeMethod();
    }

}
