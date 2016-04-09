package com.glsc;

import java.util.concurrent.Callable;

/**
 * Created by Skywalker on 2016/4/9.
 */
public class FindPrimeThread extends Thread implements Callable {

    private long number;

    public FindPrimeThread(long scale) {
        number = scale;
    }

    @Override
    public void run() {
        if (number < 2) return;
        for (long i = 2; i < number; i++) {
            if (number % i == 0) return;
        }
        System.out.println(number);
    }

    @Override
    public Object call() throws Exception {
        if (number < 2) return false;
        for (long i = 2; i < number; i++) {
            if (number % i == 0) return false;
        }
        System.out.println(number);
        return true;
    }
}
