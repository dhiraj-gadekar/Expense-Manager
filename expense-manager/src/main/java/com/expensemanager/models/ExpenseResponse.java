package com.expensemanager.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    
    private String message;
    private String item;
    private double cost;
    private LocalDate date;
}
