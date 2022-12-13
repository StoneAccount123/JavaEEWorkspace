package whu.wz.jwtwork.aspect;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class RestApiRecord {

    private int minResponseTime = 0;

    private int maxResponseTime = 0;

    private int totalResponseCount = 0;

    private int totalResponseTime = 0;

    Map<String, Integer> exceptionCountMap = Collections.synchronizedMap(new HashMap<>());

    public void insertRecord(int time){
        totalResponseTime +=time;
        totalResponseCount++;
        if(time>maxResponseTime){
            maxResponseTime=time;
        }else if(time<minResponseTime){
            minResponseTime=time;
        }
    }

    public void insertException(Throwable e){
        String exceptionName = e.getClass().getName();
        Integer base = exceptionCountMap.get(exceptionName);
        exceptionCountMap.put(exceptionName,base==null?1:base+1);
    }

}
