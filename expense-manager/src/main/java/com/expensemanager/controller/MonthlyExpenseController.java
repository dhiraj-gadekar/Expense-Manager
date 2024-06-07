package com.expensemanager.controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.entity.Expenses;
import com.expensemanager.entity.User;
import com.expensemanager.exception.MonthlyBudgetException;
import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.models.ExpenseResponse;
import com.expensemanager.models.ProfileLossResponse;
import com.expensemanager.repository.UserRepository;
import com.expensemanager.servise.JwtService;
import com.expensemanager.servise.MonthlyExpenseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class MonthlyExpenseController {

    @Autowired
    private MonthlyExpenseService monthlyExpenseService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/monthlybudget/{budget}")
    public ResponseEntity<Double> setMonthlyBudget(@PathVariable("budget") double budget, @RequestHeader("Authorization") String token) throws UsernameNotFoundException, MonthlyBudgetException {

        return ResponseEntity.ok().body(monthlyExpenseService.setMonthlyBudget(budget, token));
    }

    @GetMapping("/expenses/{year}/{month}")
    public ResponseEntity<Set<Expenses>> getExpensesByMonth(@PathVariable("year") int year, @PathVariable("month") int month, @RequestHeader("Authorization")String token) throws UsernameNotFoundException {
        
        return ResponseEntity.ok().body(monthlyExpenseService.findExpensesByYearAndMonth(year, month, token));
    }
    
    @PutMapping("/updateexpenses")
    public ResponseEntity<ExpenseResponse> updateExpenses(@RequestBody Expenses expenses, @RequestHeader("Authorization") String token) throws UsernameNotFoundException, MonthlyBudgetException {

        return ResponseEntity.ok().body(monthlyExpenseService.updateExpanses(expenses, token));
    }

    @GetMapping("/expenses/{date}")
    public ResponseEntity<Set<Expenses>> getExpensesByDate(@PathVariable("date") LocalDate date, @RequestHeader("Authorization") String token) throws UsernameNotFoundException {
        
        return ResponseEntity.ok().body(monthlyExpenseService.getExpensesByDate(date, token));
    }

    @GetMapping("/expenses/profitloss/{month}/{year}")
    public ResponseEntity<ProfileLossResponse> getProfitOrLoss(int month, int year, @RequestHeader("Authorization") String token) {
        
        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);

        LocalDate date = LocalDate.of(year, month, 1);
        return ResponseEntity.ok().body(monthlyExpenseService.getProfiteOrLoss(optionalUser.get().getUserId(), date));
    }
 
}
