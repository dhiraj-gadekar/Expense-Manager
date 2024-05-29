package com.expensemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.servise.MonthlyExpenseService;

@RestController
@RequestMapping("/api")
public class MonthlyExpenseController {

    @Autowired
    private MonthlyExpenseService monthlyExpenseService;

    @PutMapping("/monthlyexpense/{budget}")
    public ResponseEntity<Double> setMonthlyBudget(@PathVariable("budget") double budget, @RequestHeader("Authorization") String token) throws UsernameNotFoundException {

        return ResponseEntity.ok().body(monthlyExpenseService.setMonthlyBudget(budget, token));
    }
}
