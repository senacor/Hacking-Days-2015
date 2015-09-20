package com.senacor.hackingdays.lmax.lmax;

import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultCollector extends ExternalResource {

    private List<Result> results = new ArrayList<>();

    @Override
    protected void after() {
        sortByTime();
        printResults();
    }

    private void sortByTime() {
        Collections.sort(results, Comparator.comparing(Result::getElapsedMillis));
    }

    private void printResults() {
        results.forEach(res -> System.err.printf("%s Milliseconds for run with ProducerType %s, WaitStrategy %s\n", res.getElapsedMillis(), res.getProducerType(), res.getWaitStrategy().getClass().getSimpleName()));
    }

    public void addResult(ProducerType producerType, WaitStrategy waitStrategy, long elapsed) {
        results.add(new Result(producerType, waitStrategy, elapsed));

    }


    private static final class Result {

        private final ProducerType producerType;
        private final WaitStrategy waitStrategy;
        private final long elapsedMillis;


        private Result(ProducerType producerType, WaitStrategy waitStrategy, long elapsedMillis) {
            this.producerType = producerType;
            this.waitStrategy = waitStrategy;
            this.elapsedMillis = elapsedMillis;
        }

        public ProducerType getProducerType() {
            return producerType;
        }

        public WaitStrategy getWaitStrategy() {
            return waitStrategy;
        }

        public long getElapsedMillis() {
            return elapsedMillis;
        }
    }


}
