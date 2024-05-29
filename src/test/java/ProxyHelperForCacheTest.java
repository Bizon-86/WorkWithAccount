import ru.inno.local.cache.Cache;
import ru.inno.local.cache.Fractionable;
import ru.inno.local.cache.Mutator;

public class ProxyHelperForCacheTest implements Fractionable {
    private int cnt = 0;
    private int num;
    private int denum;

    public ProxyHelperForCacheTest(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    public int getCnt() {
        return cnt;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    //@Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }


    @Override
    @Cache(1000)
    public double doubleValue() {
        cnt++;
        return (double) num / denum;
    }
}
