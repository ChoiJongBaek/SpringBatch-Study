package io.springbatch.springbatchlecture;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessorListner implements ItemProcessListener<Customer, Customer> {
    @Override
    public void beforeProcess(Customer customer) {

    }

    @Override
    public void afterProcess(Customer item, Customer result) {
        System.out.println("Thread : " + Thread.currentThread().getName() + ", process item : " + item.getId());
    }

    @Override
    public void onProcessError(Customer item, Exception e) {

    }
}
