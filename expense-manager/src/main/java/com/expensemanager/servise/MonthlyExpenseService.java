package com.expensemanager.servise;

import java.time.LocalDate;
import java.util.Set;

import com.expensemanager.entity.Expenses;
import com.expensemanager.exception.MonthlyBudgetException;
import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.models.ExpenseResponse;
import com.expensemanager.models.ProfileLossResponse;

public interface MonthlyExpenseService {
    
    double setMonthlyBudget(double budget, String token) throws UsernameNotFoundException, MonthlyBudgetException;

    Set<Expenses> findExpensesByYearAndMonth(int year, int month, String token) throws UsernameNotFoundException;

    ExpenseResponse updateExpanses(Expenses expenses, String token) throws UsernameNotFoundException, MonthlyBudgetException;

    Set<Expenses> getExpensesByDate(LocalDate date, String token) throws UsernameNotFoundException;

    ProfileLossResponse getProfiteOrLoss(String token);
}
