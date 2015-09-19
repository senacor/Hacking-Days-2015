package com.senacor.hackingdays.remoting;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class Result implements Comparable<Result> {
    private long time;
    private String name;

    public Result(long time, String name) {
        this.time = time;
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Result o) {
        return time > o.time ? 1 : -1;
    }
}
