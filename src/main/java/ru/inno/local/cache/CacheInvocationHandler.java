package ru.inno.local.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheInvocationHandler implements InvocationHandler,Runnable {
    private Object object;
    private ConcurrentHashMap<Integer,ObjectWithMethodResult> cacheTableObject;
    private long cacheTimeOut = 0;
    private boolean needClearCache = false;
    private TimeOutChecker timeChecker;

    public CacheInvocationHandler(Object object, TimeOutChecker timeChecker) {
        this.object = object;
        this.cacheTableObject = new ConcurrentHashMap<>();
        this.timeChecker = timeChecker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method tmp = object.getClass().getMethod(method.getName(), method.getParameterTypes());
        Cache cache = tmp.getAnnotation(Cache.class);
        Mutator mutator = tmp.getAnnotation(Mutator.class);
        if (tmp.isAnnotationPresent(Cache.class)) {
            var paramsWithMethod = new ParamsWithMethod(method, args);
            var objAndMethodHashCode = object.hashCode() + paramsWithMethod.hashCode();
            if (!cacheTableObject.isEmpty()) {
                if (cacheTableObject.containsKey(objAndMethodHashCode)) {
                    var objFromCache = cacheTableObject.get(objAndMethodHashCode);

                    if (paramsWithMethod.equals(objFromCache.getParamsWithMethod()) && objFromCache.getObject().equals(object)) {
                        cacheTimeOut = cache.value();
                        System.out.println(timeChecker.curTime() + " - " + objFromCache.getLastUse() + " - " + cacheTimeOut);
                        if (timeChecker.curTime() - objFromCache.getLastUse() < cacheTimeOut) {
                            objFromCache.setLastUse(timeChecker.curTime());
                            System.out.println("вывод из перехваченного рефлексией метода. расчета не было, значение взяли из кэша");
                            return objFromCache.getMethodResult();

                        } else {
                            needClearCache = true;
                        }
                    }
                }
            }
            var methodResult = method.invoke(object, args);
            cacheTableObject.put(objAndMethodHashCode, new ObjectWithMethodResult(object, methodResult, paramsWithMethod, timeChecker.curTime()));

            return methodResult;
        }
        if (method.isAnnotationPresent(Mutator.class)) {
           cacheTableObject.clear();
        }
        return method.invoke(object, args);
    }

    @Override
    public void run() {
        long lastStart = timeChecker.curTime();

        do {
            if(!Thread.interrupted()){
                if (needClearCache || timeChecker.curTime() - lastStart > cacheTimeOut) {
                    clearCache();
                    lastStart = timeChecker.curTime();
                }
            } else {
                return;
            }

            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
        while (true);
    }
    public void clearCache() {
        cacheTableObject.forEach((k, v) -> {
                    if (timeChecker.curTime() - v.getLastUse() < cacheTimeOut) {
                        cacheTableObject.remove(k);
                    }
                }
        );
        needClearCache = false;
    }
}
