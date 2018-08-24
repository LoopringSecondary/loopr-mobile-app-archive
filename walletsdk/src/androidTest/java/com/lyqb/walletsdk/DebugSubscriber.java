package com.lyqb.walletsdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rx.Subscriber;

public class DebugSubscriber<T> extends Subscriber<T> {

    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void onCompleted() {
        System.out.println("complete.");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        Class<?> tClass = t.getClass();
        System.out.println(tClass.getName());
        try {
            String json = mapper.writeValueAsString(t);
            System.out.println(t.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
