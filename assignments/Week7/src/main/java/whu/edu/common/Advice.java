package whu.edu.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Aspect
@Slf4j
@Component
public class Advice {

    private HashMap<String, HandlerInfo> map = new HashMap();

    public HashMap<String, HandlerInfo> getMap() {
        return map;
    }

    //向map初始化一个api的信息
    public void add(String signature){
        HandlerInfo info = new HandlerInfo();
        info.setMaxTime(Long.MIN_VALUE);
        info.setMinTime(Long.MAX_VALUE);
        map.put(signature,info);
    }

    //统计api调用次数  在此认为没有异常才计数
    @AfterReturning("execution(* whu.edu.controller.GoodsController.*(..))")
    public void before(JoinPoint joinPoint){
        //以方法签名为key
        String signature = joinPoint.getSignature().toString();
        log.info("signature={}",signature);

        //解决并发写的问题 锁为方法签名
        synchronized (signature.intern()){
            if(!map.containsKey(signature)){
                add(signature);
            }
            HandlerInfo handlerInfo = map.get(signature);
            handlerInfo.setTimes(handlerInfo.getTimes()+1);
        }
    }

    //统计api的最长最短和总的访问时间
    @Around(value = "execution(* whu.edu.controller.*.*(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String signature = proceedingJoinPoint.getSignature().toString();
        log.info("signature={}",signature);
        long begin = System.currentTimeMillis();
        //被增强的方法执行
        proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        //解决并发写的问题
        synchronized (signature.intern()){
            if(!map.containsKey(signature)){
                add(signature);
            }
            HandlerInfo handlerInfo = map.get(signature);
            Long exectime = end - begin;
            handlerInfo.setMaxTime(Math.max(handlerInfo.getMaxTime(),exectime));
            handlerInfo.setMinTime(Math.min(handlerInfo.getMinTime(),exectime));
            handlerInfo.setSumTime(handlerInfo.getSumTime()+exectime);
        }
    }

    //统计api异常的次数
    @AfterThrowing(value = "execution(* whu.edu.controller.*.*(..))")
    public void excption(JoinPoint joinPoint){
        String signature = joinPoint.getSignature().toString();
        log.info("signature={}",signature);
        synchronized (signature.intern()){
            if(!map.containsKey(signature)){
                add(signature);
            }
            HandlerInfo handlerInfo = map.get(signature);
            handlerInfo.setExceptionTimes(handlerInfo.getExceptionTimes()+1);
        }
    }

}