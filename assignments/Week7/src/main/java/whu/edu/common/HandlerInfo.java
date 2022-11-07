package whu.edu.common;

import lombok.Data;

@Data
public class HandlerInfo {
    private int times;

    private long maxTime;

    private long minTime;

    private long sumTime;

    private int exceptionTimes;

}
