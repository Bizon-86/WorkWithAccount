package ru.inno.local.cache;

public class TimeOut implements TimeOutChecker {
    private long time;
    @Override
    public long curTime() {
        return time;
    }

    public void setTime(long time) {
        System.out.println("Set time");
        this.time = time;
    }

    public TimeOut(long time) {
        this.time = time;
    }
}
