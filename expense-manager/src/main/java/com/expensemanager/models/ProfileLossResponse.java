package com.expensemanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileLossResponse {
    
    private double amount;

    private String profileOrLoss;
}
