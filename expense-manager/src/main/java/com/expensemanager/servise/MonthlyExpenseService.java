package com.expensemanager.servise;

import java.time.LocalDate;
import java.util.Set;

import com.expensemanager.entity.Expenses;
import com.expensemanager.exception.MonthlyBudgetException;
import com.expensemanager.exception.UsernameNotFoundException;

public interface MonthlyExpenseService {
    
    double setMonthlyBudget(double budget, String token) throws UsernameNotFoundException, MonthlyBudgetException;

    Set<Expenses> findExpensesByYearAndMonth(int year, int month, String token) throws UsernameNotFoundException;

    Expenses updateExpanses(int year, int month, Expenses expenses, String token) throws UsernameNotFoundException, MonthlyBudgetException;

    Set<Expenses> getExpensesByDate(LocalDate date, String token) throws UsernameNotFoundException;
}
