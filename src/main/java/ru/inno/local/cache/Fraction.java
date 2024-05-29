package ru.inno.local.cache;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public class ZeroDenum extends RuntimeException{
        public ZeroDenum(String message) {
            super(message);
        }
    }

    public Fraction(int num, int denum) {
        if (denum == 0) throw new ZeroDenum("Знаменатель не может быть равен нулю");
        this.num = num;
        this.denum = denum;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    public double doubleValue() {
        return (double) num / denum;
    }
}
