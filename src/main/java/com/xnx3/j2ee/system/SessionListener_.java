package com.xnx3.j2ee.system;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;
/**
 * 整个系统的Session会话管理
 * @author 管雷鸣
 *
 */
@Component
public class SessionListener_ implements SessionListener {

    private final AtomicInteger sessionCount = new AtomicInteger(0);
    
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
//        Log.debug("登录+1=="+sessionCount.get());
    }

    public void onStop(Session session) {
        sessionCount.decrementAndGet();
//        Log.debug("登录退出-1=="+sessionCount.get());
    }

    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
//        Log.debug("登录过期-1=="+sessionCount.get());
        
    }

    public int getSessionCount() {
        return sessionCount.get();
    }
}