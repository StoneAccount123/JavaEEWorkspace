import edu.whu.boot.MainApplication;
import edu.whu.config.Config;
import edu.whu.config.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;

@DisplayName("MainApplication的测试类")
public class ApplicationTest {

    @DisplayName("测试创建对象")
    @RepeatedTest(5)
    public void creatObjTest(){
//        Assertions.assertNotNull(new MainApplication().creatObj());
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println("----");
//        FileReader bean = context.getBean(FileReader.class);
    }

    @DisplayName("测试注解方法")
    @Test
    public void annotationTest(){
        Assertions.assertDoesNotThrow(()->new MainApplication().invokeMethod());
        //因为采用了try catch语句，故不会抛出异常，此时断言仍然可以通过
        Assertions.assertDoesNotThrow(()->new MainApplication().invokeMethod("不必要的参数"));
    }

}