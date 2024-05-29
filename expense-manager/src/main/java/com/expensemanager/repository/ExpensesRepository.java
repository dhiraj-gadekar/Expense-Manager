package com.expensemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanager.entity.Expenses;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    
}
