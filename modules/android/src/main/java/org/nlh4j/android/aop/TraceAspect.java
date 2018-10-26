/*
 * @(#)TraceAspect.java 1.0 Nov 17, 2016 Copyright 2016 by SystemEXE Inc. All
 * rights reserved.
 */
package org.nlh4j.android.aop;

import java.io.Serializable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * Debug/Trace Aspect
 *
 * @author Hai Nguyen
 */
@Aspect
public class TraceAspect implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** Activity point-cut */
    private static final String POINTCUT_APPLICATION_METHOD =
            "execution(* org.nlh4j.android.AbstractApplication+.*(..))";
    @Pointcut(POINTCUT_APPLICATION_METHOD)
    public void applicationPointcut() {}
    /** Activity point-cut */
    private static final String POINTCUT_ACTIVITY_METHOD =
            "execution(* org.nlh4j.android.activities.AbstractActivity+.*(..))";
    @Pointcut(POINTCUT_ACTIVITY_METHOD)
    public void activityPointcut() {}
    /** Receiver point-cut */
    private static final String POINTCUT_RECEIVER_METHOD =
            "execution(* org.nlh4j.android.receivers.AbstractBroadcastReceiver+.*(..))";
    @Pointcut(POINTCUT_RECEIVER_METHOD)
    public void receiverPointcut() {}
    /** Service point-cut */
    private static final String POINTCUT_SERVICE_METHOD =
            "execution(* org.nlh4j.android.services.AbstractService+.*(..))";
    @Pointcut(POINTCUT_SERVICE_METHOD)
    public void servicePointcut() {}
    /** Task point-cut */
    private static final String POINTCUT_TASK_METHOD =
            "execution(* org.nlh4j.android.tasks.AbstractAsyncTask+.*(..))";
    @Pointcut(POINTCUT_TASK_METHOD)
    public void taskPointcut() {}

    /**
     * Trace methods
     *
     * @param joinPoint {@link ProceedingJoinPoint}
     *
     * @return the method result
     */
    @Around("applicationPointcut() || activityPointcut() || receiverPointcut() || servicePointcut() || taskPointcut()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) {
        MethodSignature medSign = BeanUtils.safeType(joinPoint.getSignature(), MethodSignature.class);
        Assert.notNull(medSign, "MethodSignature");
        String className = medSign.getDeclaringType().getSimpleName();
        String methodName = medSign.getName();
        // log start
        LogUtils.debugStart(className, methodName);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            LogUtils.w(e.getMessage());
            throw new ApplicationRuntimeException(e);
        } finally {
            // log end
            sw.stop();
            LogUtils.debugEnd(className, methodName, sw.getTotalTimeMillis());
        }
    }
}
