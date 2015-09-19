package com.senacor.hackingdays.lmax.lmax;

import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultCollector extends ExternalResource {

    private List<Result> results = new ArrayList<>();

    public void addResult(int poolsize, long elapsedMillis) {
        results.add(new Result(poolsize, elapsedMillis));
    }

    @Override
    protected void after() {
        sortByTime();
        printResults();
    }

    private void sortByTime() {
        Collections.sort(results, Comparator.comparing(Result::getElapsedMillis));
    }

    private void printResults() {
        results.forEach(res -> System.err.printf("%s Milliseconds for run with pool size of %s\n", res.getElapsedMillis(), res.getPoolsize()));
    }


    private static final class Result {

        private final int poolsize;
        private final long elapsedMillis;

        public Result(int poolsize, long elapsedMillis) {

            this.poolsize = poolsize;
            this.elapsedMillis = elapsedMillis;
        }

        public int getPoolsize() {
            return poolsize;
        }

        public long getElapsedMillis() {
            return elapsedMillis;
        }
    }
}
