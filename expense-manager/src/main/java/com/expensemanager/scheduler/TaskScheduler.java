package com.expensemanager.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.expensemanager.servise.MonthlyExpenseService;

public class TaskScheduler {

    @Autowired
    private MonthlyExpenseService monthlyExpenseService;
    
    @Scheduled(cron = "0 0 0 1 * *")
    public void reportGeneratorScheduler() {

        monthlyExpenseService.expenseReportGenerator();
    }
}
