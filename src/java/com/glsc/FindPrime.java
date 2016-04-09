package com.glsc;

import org.omg.CORBA.TIMEOUT;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Skywalker on 2016/4/9.
 */
public class FindPrime {

    public static void main(String[] args) {
        long scale = 100_000L;
        long start = System.currentTimeMillis();
//        traditional(scale);
//        newThread(scale);
//        innerClass(scale);
//        forkJoin(scale);
        lambda(scale);
        long end = System.currentTimeMillis();
        System.out.println("Took " + (end - start) + "ms");
    }

    private static void lambda(long scale) {
        List<Callable<Object>> list = new ArrayList<Callable<Object>>();
        for (long i = 1; i <= scale; i++) {
            final long number = i;
            list.add(() -> {
                if (number < 2) return false;
                for (long j = 2; j < number; j++) {
                    if (number % j == 0) return false;
                }
                System.out.println(number);
                return true;
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(64);
        try {
            executorService.invokeAll(list);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void forkJoin(long scale) {
        List list = new ArrayList<>();
        callablePrime(scale, list);
//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinPool forkJoinPool = new ForkJoinPool(64);
        forkJoinPool.invokeAll(list);
    }

    private static void innerClass(long scale) {
        List list = new ArrayList<>();
        callablePrime(scale, list);
        ExecutorService executorService = Executors.newFixedThreadPool(64);
        try {
            executorService.invokeAll(list);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void callablePrime(long scale, List list) {
        for (long i = 1; i <= scale; i++) {
            final long number = i;
            list.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    if (number < 2) return false;
                    for (long i = 2; i < number; i++) {
                        if (number % i == 0) return false;
                    }
                    System.out.println(number);
                    return true;
                }
            });
        }
    }

    private static void newThread(long scale) {
        List list = new ArrayList<>();
        for (long i = 1; i <= scale; i++) {
            list.add(new FindPrimeThread(i));
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            executorService.invokeAll(list);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void traditional(long scale) {
        for (long i = 1; i <= scale; i++) {
            if (checkPrime(i)) {
                System.out.println(i);
            }
        }
    }

    public static boolean checkPrime(long number) {
        if (number < 2) return false;
        for (long i = 2; i < number; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

}
