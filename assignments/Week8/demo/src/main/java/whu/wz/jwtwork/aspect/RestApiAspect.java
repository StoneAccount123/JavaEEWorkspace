package whu.wz.jwtwork.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class RestApiAspect {

    /**
     * 统计结果表
     */
    Map<String, RestApiRecord> records = Collections.synchronizedMap(new HashMap<>());

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object doProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
        String method=joinPoint.getSignature().toString();
        RestApiRecord restApiRecord = records.get(method);
        if(restApiRecord == null){
            restApiRecord = new RestApiRecord();
            records.put(method, restApiRecord);
        }
        try {
            long t1= Calendar.getInstance().getTimeInMillis();
            Object result = joinPoint.proceed();
            long t2= Calendar.getInstance().getTimeInMillis();
            restApiRecord.insertRecord((int)(t2-t1));
            return result;
        } catch (Throwable e) {
            restApiRecord.insertException(e);
            throw e;
        }
    }

    public Map<String, RestApiRecord> getRecords() {
        return records;
    }
}
