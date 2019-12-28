package com.exer.calendarfy.push;

import com.exer.calendarfy.retry.RetryQueue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FCMPush implements BasePush {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public void sendPushToSender(PushRequest pushRequest) {
        Request request = new Request.Builder()
                .url("http://localhost:8888/messageRequest")
                .addHeader("deviceToken", pushRequest.getDeviceToken())
                .addHeader("messageTitle", pushRequest.getEvent().getEventTitle())
                .addHeader("messageBody",pushRequest.getEvent().getEventDesc())
                .build();
        new Thread(() -> {
            Response response;
            try {
                response = httpClient.newCall(request).execute();
                System.out.println("Posted to Sender with response: " + response);
                response.close();
            } catch (IOException e) {
                System.out.println("Failed to post message to sender: " + e);
                RetryQueue.getInstance().addMessageToQueue(pushRequest);
            }
        }).start();
    }

    @Override
    public boolean validatePush(PushRequest pushRequest) {
        return false;
    }
}
