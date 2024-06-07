package com.expensemanager.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expensemanager.entity.Expenses;
import com.expensemanager.entity.MonthlyBudget;

@Repository
public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Integer> {

    Optional<MonthlyBudget> findMonthlyBudgetsByUserIdAndDateBetween(int userId, LocalDate startDate,
            LocalDate endDate);

    @Query("SELECT mb.expenses FROM MonthlyBudget mb " +
            "WHERE mb.date BETWEEN :startDate AND :endDate " +
            "AND mb.userId = :userId")
    Optional<Set<Expenses>> findExpensesBetweenDatesByUser(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") int userId);

    @Query("SELECT e FROM Expenses e " + 
    "INNER JOIN MonthlyBudget mb ON mb.id = e.monthlyBudgetId " + 
    "WHERE mb.userId = :userId AND e.date = :date")
    Set<Expenses> findExpensesByDate(
            @Param("userId") int userId,
            @Param("date") LocalDate date);
}
