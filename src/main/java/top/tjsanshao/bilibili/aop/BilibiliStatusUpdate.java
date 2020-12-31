package top.tjsanshao.bilibili.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.tjsanshao.bilibili.current.Init;

import javax.annotation.Resource;

/**
 * user status update
 *
 * @author TjSanshao
 * @date 2020-12-31 10:40
 */
@Slf4j
@Aspect
@Component
public class BilibiliStatusUpdate {
    @Resource
    private Init init;

    @Pointcut("execution(public void top.tjsanshao.bilibili.action.*.act())")
    public void action() {};

    @Before("action()")
    public void before(JoinPoint joinPoint) {
        String action = joinPoint.getSignature().getDeclaringTypeName();
        log.info("Enter 【{}】 user status refresh...", action);
        init.refresh();
        log.info("Action 【{}】 refresh user status successfully...", action);
    }

    @After("action()")
    public void after(JoinPoint joinPoint) {
        String action = joinPoint.getSignature().getDeclaringTypeName();
        log.info("Exit 【{}】 user status refresh...", action);
        init.refresh();
        log.info("Exit 【{}】 refresh user status successfully...", action);
    }
}
