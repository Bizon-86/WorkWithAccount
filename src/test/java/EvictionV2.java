import org.junit.Test;
import ru.inno.local.cache.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public interface EvictionV2 {
    interface CacheCleanup {
        void callDeferred(Runnable runnable, long delayMillis);
    }

    class CachedProxy {
        private final CacheCleanup cleanup;

        public CachedProxy(CacheCleanup cleanup) {
            this.cleanup = cleanup;
        }

        public Object create(Object instance) {
            // use cleanup - pass to handler
            ClassLoader tClassLoader = instance.getClass().getClassLoader();
            Class[] interfaces = instance.getClass().getInterfaces();
           CacheInvocationHandler cacheInvocationHandler = new CacheInvocationHandler(this.cleanup);
           return Proxy.newProxyInstance(tClassLoader, interfaces, cacheInvocationHandler);
        }
    }

    class CacheInvocationHandler implements InvocationHandler {
        class CacheKey {Object[] callArguments; }
        // V2. store
        private final CacheCleanup cleanup;
        private final ConcurrentHashMap<Method, ConcurrentHashMap<CacheKey, Object>>
                store = new ConcurrentHashMap<>();

        public CacheInvocationHandler(CacheCleanup cleanup) {
            this.cleanup = cleanup;
        }

        @Override public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            // store.put(cacheKey, value)
            cleanup.callDeferred(() -> store.get(method).remove(1), 1000L);
            return null;
        }
    }

    class ThreadPoolCleanup implements CacheCleanup {
        // wrap ExecutorService, spy with Mockito
        final ScheduledExecutorService executor;

        public ThreadPoolCleanup(ScheduledExecutorService executor) {
            this.executor = executor;
        }

        @Override public void callDeferred(Runnable runnable, long delay) {
            executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        }
    }

    class CacheTest {
        class TestCleanup implements CacheCleanup {
            List<Runnable> cleanupQueue = new ArrayList<>();

            @Override public void callDeferred(Runnable runnable, long delay) {
                cleanupQueue.add(runnable);
            }

            public void flush() {
                cleanupQueue.forEach(Runnable::run);
                cleanupQueue.clear();
            }
        }

        interface TestIface {
            @Cache(1000)
            Object testMethod();
        }

        class TestClass implements TestIface {
            @Override public Object testMethod() {
                return new Object();
            }
        }

        @Test
        public void cleanupEnqueued() {
            var cleanup = new TestCleanup();
            var ci = (TestIface) new CachedProxy(cleanup).create(new TestClass());

            var v1 = ci.testMethod();
            assertEquals(1,cleanup.cleanupQueue.size());
            var v2 = ci.testMethod();

            assertEquals(1,cleanup.cleanupQueue.size());
            assertEquals(v1, v2);

            cleanup.flush();
            var v3 = ci.testMethod();

            assertNotEquals(v2, v3);
        }
    }
}
