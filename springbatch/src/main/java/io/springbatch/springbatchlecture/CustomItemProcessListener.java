package io.springbatch.springbatchlecture;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener {
    @Override
    public void beforeProcess(Object o) {
        System.out.println(">> before Process");
    }

    @Override
    public void afterProcess(Object o, Object o2) {
        System.out.println(">> after Process");
    }

    @Override
    public void onProcessError(Object o, Exception e) {
        System.out.println(">> after Process Error");
    }
}
