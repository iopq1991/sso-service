package cn.com.flaginfo.platform.sso.aop;

import cn.com.flaginfo.platform.common.util.JsonHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chenglong.xu
 */

@Aspect
@Component
public class WebLogAop {

    private final String CONTROLLER = "controller";

    private final String RPC = "rpc";

    private static final Logger logger = LoggerFactory.getLogger(WebLogAop.class);

    @Around("execution(* cn.com.flaginfo.platform.sso.controller.*.*(..))")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return showLog(joinPoint, CONTROLLER);
    }

    @Around("execution(* cn.com.flaginfo.platform.sso.rpc.*.*(..))")
    public Object rpcLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return showLog(joinPoint, RPC);
    }

    public Object showLog(ProceedingJoinPoint joinPoint, String sourceName) throws Throwable {
        String memthodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String param = "";
        if (CONTROLLER.equals(sourceName)) {
            if (args.length > 0) {
                param = JsonHelper.parseToJson(args[0]);
            }
        } else {
            param = JsonHelper.parseToJson(args);
        }
        logger.info("---- 【" + sourceName + "请求】:" + memthodName + ", param:" + param + " ----");
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed(args);
        long endTime = System.currentTimeMillis();
        logger.info("---- 【" + sourceName + "返回】:" + memthodName + ", result:" + JsonHelper.parseToJson(obj) + " ----");
        printExecTime(memthodName, startTime, endTime, sourceName);
        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     *
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime, String sourceName) {
        long diffTime = endTime - startTime;
        long serverSlowTime = 0L;
        if (diffTime > serverSlowTime) {
            logger.warn("---- 【" + sourceName + "耗时】:" + methodName + " 耗时：" + diffTime + " ms; ----");
        }
    }

}
