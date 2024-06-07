package com.expensemanager.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class TaskScheduler {
    
    @Scheduled(cron = "0 0 0 1 * *")
    public void reportGeneratorScheduler() {

        
    }
}
