package whu.edu.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whu.edu.common.Advice;
import whu.edu.controller.DemoController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
class Week6ApplicationTests {
    @Autowired
    DemoController demoController;

    @Autowired
    Advice advice;

    @Test
     void testGetOne() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i = 0;i<1000;i++){
            service.execute(
                    ()->{
                        try {
                            demoController.api1();
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                        try {
                            demoController.api2();
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                    }
            );
        }
        Thread.sleep(100000);
        advice.getMap().entrySet().forEach(
                entry->{
                    log.info("entry={},",entry.getKey());
                    log.info("maxtime={},",entry.getValue().getMaxTime());
                    log.info( "mintime={},",entry.getValue().getMinTime());
                    log.info( "times={},",entry.getValue().getTimes());
                    log.info("sumTime={},",entry.getValue().getSumTime());
                    log.info("avgtime={}",1.0*entry.getValue().getSumTime()/entry.getValue().getTimes());
                }
        );
    }

}