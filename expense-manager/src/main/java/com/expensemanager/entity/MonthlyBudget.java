package com.expensemanager.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.expensemanager.utils.Month;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private double amount;

    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private Month month;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private Set<Expenses> expenseSet = new HashSet<>();  
}
