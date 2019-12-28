package com.exer.calendarfy.controller;

import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;

@RestController
public class CalanderfyController {

    @RequestMapping("/healthcheck")
    public HashMap<String, String> healthcheck() {
        BaseResponse response = new Response();
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();

        response.addResponseHeader("uptime", String.valueOf(mxBean.getUptime()));
        response.setIsSuccessful(true);

        return response.getResponse();
    }
}
