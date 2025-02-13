package com.admin.panel.cronjob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.admin.panel.service.JwtService;



@Component
public class EveryMorning10AmCronJobs {

    private final JwtService jwtService;

    public EveryMorning10AmCronJobs(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void clearExpiredTokens() {
        jwtService.cleanUpExpiredTokens();
        System.out.println("Cleaning up expired tokens");
    }


}
