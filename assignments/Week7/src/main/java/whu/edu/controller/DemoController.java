package whu.edu.controller;


import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class DemoController {
    public void api1() {
        log.info("==api1执行==");
        try {
            //随机休眠20-200ms
            Thread.sleep(RandomUtil.randomInt(20,1000));
            //0.4的概率抛异常
            if(RandomUtil.randomInt(10)<4){
                throw new RuntimeException("api2出现异常");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void api2() {
        log.info("==api2执行==");
        try {
            //随机休眠100-300ms
            Thread.sleep(RandomUtil.randomInt(100,300));
            //0.2的概率抛异常
            if(RandomUtil.randomInt(10)<2){
                throw new RuntimeException("api1出现异常");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}