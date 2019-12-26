package com.exer.calendarfy.retry;

import com.exer.calendarfy.push.PushRequest;

import java.util.ArrayList;

public class RetryQueue {

    private static RetryQueue instance;
    private final ArrayList<PushRequest> messageQueue = new ArrayList<>();

    public static RetryQueue getInstance(){
        if (instance == null)
            instance = new RetryQueue();

        return instance;
    }

    public void addMessageToQueue(PushRequest request){
        synchronized (messageQueue){
            messageQueue.add(request);
        }
    }

    public void removeMessageFromQueue(PushRequest request){
        synchronized (messageQueue){
            messageQueue.remove(request);
        }
    }

    public PushRequest getOldestRequest(){
        synchronized (messageQueue){
            if (messageQueue.size() != 0)
                return messageQueue.get(0);
        }

        return null;
    }
}
