package com.expensemanager.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Expenses {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer expenseId;

    private String item;

    private Date date;

    private double cost;
}
