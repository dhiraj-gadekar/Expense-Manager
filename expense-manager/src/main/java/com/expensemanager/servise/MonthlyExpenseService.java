package com.expensemanager.servise;

import com.expensemanager.exception.UsernameNotFoundException;

public interface MonthlyExpenseService {
    
    Double setMonthlyBudget(double budget, String token) throws UsernameNotFoundException;
}
