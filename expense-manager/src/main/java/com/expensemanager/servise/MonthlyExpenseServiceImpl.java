package com.expensemanager.servise;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.expensemanager.entity.MonthlyBudget;
import com.expensemanager.entity.User;
import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.repository.MonthlyBudgetRepository;
import com.expensemanager.repository.UserRepository;

@Service
public class MonthlyExpenseServiceImpl implements MonthlyExpenseService {

    private JwtService jwtService;

    private UserRepository userRepository;

    private MonthlyBudgetRepository monthlyBudgetRepository;

    public MonthlyExpenseServiceImpl(JwtService jwtService, UserRepository userRepository,
            MonthlyBudgetRepository monthlyBudgetRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    @Override
    public Double setMonthlyBudget(double budget, String token) throws UsernameNotFoundException {
        
        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) 
            throw new UsernameNotFoundException("User Not Found");
            
        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        
        System.out.println("Start Date : " + monthStartDate);
        System.out.println("Last Date :" + monthLastDate);

        List<MonthlyBudget> monthlyBudget = monthlyBudgetRepository.findMonthlyBudgetsByUserAndDateBetween(user, monthStartDate, monthLastDate);

        if (monthlyBudget.size() == 0) {
            
            System.out.println(monthlyBudget);
        }
        return 0.0;
    }
    
}
