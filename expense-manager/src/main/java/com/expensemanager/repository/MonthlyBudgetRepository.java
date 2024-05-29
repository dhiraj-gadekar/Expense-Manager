package com.expensemanager.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanager.entity.MonthlyBudget;
import com.expensemanager.entity.User;

@Repository
public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Integer> {

    List<MonthlyBudget> findMonthlyBudgetsByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
