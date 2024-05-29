import org.junit.Test;
import ru.inno.local.cache.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CacheTest {
    @Test
    public void createFractionWithZeroDenum() {
        assertThrows(Fraction.ZeroDenum.class, () -> {
            var x = new Fraction(1, 0);
        });
    }

    @Test
    public void createFractionNormally() {
        Fraction x = null;
        x = new Fraction(1, 2);
        assertEquals((double) 1 / 2, x.doubleValue(), 0.00001d);
    }

    @Test
    public void runProxyWithoutMutator() {
        ProxyHelperForCacheTest fr = new ProxyHelperForCacheTest(2, 3);
        TimeOutChecker timeOutChecker = new TimeOut(1L);
        Fractionable proxyNum = CacheUtils.cache(fr, timeOutChecker);
        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout молчит
        double d = proxyNum.doubleValue(); //sout молчит
        assertEquals((double) 2 / 3, d, 0.00001d);
        assertEquals(1, fr.getCnt());
    }

    @Test
    public void runProxyWithSetNum() {
        ProxyHelperForCacheTest fr = new ProxyHelperForCacheTest(4, 2);
        TimeOutChecker timeOutChecker = new TimeOut(1L);
        Fractionable proxyNum = CacheUtils.cache(fr, timeOutChecker);
        proxyNum.doubleValue(); //sout сработал
        proxyNum.setNum(5);
        proxyNum.doubleValue(); //sout сработал
        double d = proxyNum.doubleValue(); //sout молчит
        assertEquals((double) 5 / 2, d, 0.00001d);
        assertEquals(2, fr.getCnt());
    }

    @Test
    public void runProxyWithSetDenum() {
        ProxyHelperForCacheTest fr = new ProxyHelperForCacheTest(6, 2);
        TimeOut timeOutChecker = new TimeOut(100L);
        Fractionable proxyNum = CacheUtils.cache(fr, timeOutChecker);
        System.out.println(proxyNum.doubleValue());
        System.out.println(proxyNum.doubleValue());
        proxyNum.setDenum(5);
        timeOutChecker.setTime(4500L);
        double d = proxyNum.doubleValue();
        assertEquals((double) 6 / 5, d, 0.00001d);
        assertEquals(2, fr.getCnt());
    }
}
