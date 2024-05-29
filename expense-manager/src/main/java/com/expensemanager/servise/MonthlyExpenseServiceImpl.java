package com.expensemanager.servise;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanager.entity.Expenses;
import com.expensemanager.entity.MonthlyBudget;
import com.expensemanager.entity.User;
import com.expensemanager.exception.MonthlyBudgetException;
import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.repository.MonthlyBudgetRepository;
import com.expensemanager.repository.UserRepository;
import com.expensemanager.utils.Month;

@Service
public class MonthlyExpenseServiceImpl implements MonthlyExpenseService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Override
    public double setMonthlyBudget(double budget, String token)
            throws UsernameNotFoundException, MonthlyBudgetException {

        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        Optional<MonthlyBudget> optionalBudget = monthlyBudgetRepository.findMonthlyBudgetsByUserIdAndDateBetween(user.getUserId(),
                monthStartDate, monthLastDate);

        if (optionalBudget.isPresent()) {

            throw new MonthlyBudgetException("This month budget already added");
        }
        MonthlyBudget monthlyBudget = new MonthlyBudget();
        monthlyBudget.setAmount(budget);
        monthlyBudget.setDate(LocalDate.now());
        monthlyBudget.setUserId(user.getUserId());
        monthlyBudget.setMonth(Month.values()[LocalDate.now().getMonthValue() - 1]);

        
        user.getMonthsList().add(monthlyBudget);
        userRepository.save(user);
        return budget;
    }

    @Override
    public Set<Expenses> findExpensesByYearAndMonth(int year, int month, String token) throws UsernameNotFoundException {

        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        Set<Expenses> optionalBudget = monthlyBudgetRepository.findExpensesBetweenDatesByUser(monthStartDate, monthLastDate, user.getUserId());

        if (optionalBudget.size() == 0) {
            
            return new HashSet<>();
        }
        return optionalBudget;
    }

    @Override
    public Expenses updateExpanses(int year, int month, Expenses expenses, String token) throws UsernameNotFoundException, MonthlyBudgetException {

        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        Optional<MonthlyBudget> optionalBudget = monthlyBudgetRepository.findMonthlyBudgetsByUserIdAndDateBetween(user.getUserId(),
                monthStartDate, monthLastDate);

        if (optionalBudget.isEmpty()) {
            
            throw new MonthlyBudgetException("Monthly Budget Not Added");
        }
        expenses.setMonthlyBudgetId(optionalBudget.get().getId());
        
        MonthlyBudget monthlyBudget = optionalBudget.get();
        double budget = monthlyBudget.getAmount() - expenses.getCost();
        if (budget < 0) {
            
            throw new MonthlyBudgetException("Low Budget");
        }
        monthlyBudget.setAmount(budget);
        monthlyBudget.getExpenses().add(expenses);
        monthlyBudgetRepository.save(monthlyBudget);
        return expenses;
    }

    @Override
    public Set<Expenses> getExpensesByDate(LocalDate date, String token) throws UsernameNotFoundException {
        
        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        Set<Expenses> expensesByDate = monthlyBudgetRepository.findExpensesByDate(optionalUser.get().getUserId(), date);
        return expensesByDate;
    }
}
