package ru.inno.local.cache;

import java.lang.reflect.Proxy;

public class CacheUtils {
    public static <T> T cache(T tClassElement, TimeOutChecker checker) {
        ClassLoader tClassLoader = tClassElement.getClass().getClassLoader();
        Class[] interfaces = tClassElement.getClass().getInterfaces();
        CacheInvocationHandler cacheInvocationHandler = new CacheInvocationHandler(tClassElement, checker);
        Thread thread = new Thread(cacheInvocationHandler, "Clear cache");
        thread.setDaemon(true);
        try {
            thread.start();
        } finally {
            thread.interrupt();
        }
        T proxyElement = (T) Proxy.newProxyInstance(tClassLoader, interfaces, cacheInvocationHandler);
        return proxyElement;
    }

}
