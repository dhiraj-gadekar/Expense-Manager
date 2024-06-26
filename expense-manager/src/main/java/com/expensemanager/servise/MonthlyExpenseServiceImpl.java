package com.expensemanager.servise;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanager.entity.Expenses;
import com.expensemanager.entity.MonthlyBudget;
import com.expensemanager.entity.User;
import com.expensemanager.exception.MonthlyBudgetException;
import com.expensemanager.exception.UsernameNotFoundException;
import com.expensemanager.models.ExpenseResponse;
import com.expensemanager.models.ProfileLossResponse;
import com.expensemanager.repository.MonthlyBudgetRepository;
import com.expensemanager.repository.UserRepository;
import com.expensemanager.utils.CSVOperations;
import com.expensemanager.utils.Month;

@Service
public class MonthlyExpenseServiceImpl implements MonthlyExpenseService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CSVOperations csvOperations;

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

        Optional<MonthlyBudget> optionalBudget = monthlyBudgetRepository.findMonthlyBudgetsByUserIdAndDateBetween(
                user.getUserId(),
                monthStartDate, monthLastDate);

        if (optionalBudget.isPresent()) {

            throw new MonthlyBudgetException("This month budget already added");
        }
        MonthlyBudget monthlyBudget = new MonthlyBudget();
        monthlyBudget.setMonthlyBudget(budget);
        monthlyBudget.setUsedBudget(0);
        monthlyBudget.setDate(LocalDate.now());
        monthlyBudget.setUserId(user.getUserId());
        monthlyBudget.setMonth(Month.values()[LocalDate.now().getMonthValue() - 1]);

        user.getMonthsList().add(monthlyBudget);
        userRepository.save(user);
        return budget;
    }

    @Override
    public Set<Expenses> findExpensesByYearAndMonth(int year, int month, String token)
            throws UsernameNotFoundException {

        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        Set<Expenses> optionalBudget = monthlyBudgetRepository.findExpensesBetweenDatesByUser(monthStartDate,
                monthLastDate, user.getUserId()).get();

        if (optionalBudget.size() == 0) {

            return new HashSet<>();
        }
        return optionalBudget;
    }

    @Override
    public ExpenseResponse updateExpanses(Expenses expenses, String token)
            throws UsernameNotFoundException, MonthlyBudgetException {

        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User Not Found");

        User user = optionalUser.get();
        LocalDate monthStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthLastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        if (!((expenses.getDate().isAfter(monthStartDate) ||
                expenses.getDate().isEqual(monthStartDate)) &&
                (expenses.getDate().isBefore(LocalDate.now()) ||
                        expenses.getDate().equals(LocalDate.now())))) {

            throw new MonthlyBudgetException("Invalid Date " + expenses.getDate());
        }
        Optional<MonthlyBudget> optionalBudget = monthlyBudgetRepository.findMonthlyBudgetsByUserIdAndDateBetween(
                user.getUserId(),
                monthStartDate, monthLastDate);

        if (optionalBudget.isEmpty()) {

            throw new MonthlyBudgetException("Monthly Budget Not Added for " + expenses.getDate());
        }
        expenses.setMonthlyBudgetId(optionalBudget.get().getId());

        ExpenseResponse expenseResponse = new ExpenseResponse();

        MonthlyBudget monthlyBudget = optionalBudget.get();
        double budget = monthlyBudget.getUsedBudget() + expenses.getCost();
        monthlyBudget.setUsedBudget(budget);
        monthlyBudget.getExpenses().add(expenses);
        monthlyBudgetRepository.save(monthlyBudget);

        if (budget > monthlyBudget.getMonthlyBudget())
            expenseResponse.setMessage("Low Budget");
        else
            expenseResponse.setMessage("");

        expenseResponse.setCost(expenses.getCost());
        expenseResponse.setDate(expenses.getDate());
        expenseResponse.setItem(expenses.getItem());

        return expenseResponse;
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

    @Override
    public ProfileLossResponse getProfiteOrLoss(int userId, LocalDate date) {

        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

        ProfileLossResponse profileLossResponse = new ProfileLossResponse();

        MonthlyBudget monthlyBudget = monthlyBudgetRepository
                .findMonthlyBudgetsByUserIdAndDateBetween(userId, firstDayOfMonth, lastDayOfMonth).get();

        double mb = monthlyBudget.getMonthlyBudget();
        double ub = monthlyBudget.getUsedBudget();

        if (mb >= ub) {

            profileLossResponse.setAmount(mb - ub);
            profileLossResponse.setProfileOrLoss("Profit");
        } else {

            profileLossResponse.setAmount(ub - mb);
            profileLossResponse.setProfileOrLoss("Loss");
        }

        return profileLossResponse;
    }

    @Override
    public void expenseReportGenerator() {

        List<Integer> allUserIds = userServiceImpl.getAllUserIds();

        allUserIds.forEach((userId) -> {
            LocalDate date = LocalDate.now().minusMonths(1);
            LocalDate startDateOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastDateOfMoth = date.with(TemporalAdjusters.lastDayOfMonth());

            Optional<Set<Expenses>> optionalExpensesList = monthlyBudgetRepository
                    .findExpensesBetweenDatesByUser(startDateOfMonth, lastDateOfMoth, userId);
            
            Optional<MonthlyBudget> monthlyBudgets = monthlyBudgetRepository.findMonthlyBudgetsByUserIdAndDateBetween(userId, startDateOfMonth, date);
            if (optionalExpensesList.isPresent() && monthlyBudgets.isPresent()) {

                Set<Expenses> expensesList = optionalExpensesList.get();

                ArrayList<String[]> arrayList = expensesList.stream()
                        .map(expense -> new String[] { String.valueOf(expense.getCost()),
                                String.valueOf(expense.getDate()), expense.getItem() })
                        .collect(Collectors.toCollection(ArrayList::new));

                ProfileLossResponse profiteOrLoss = getProfiteOrLoss(userId, date);
                String[] profitLossArray = new String[] { profiteOrLoss.getProfileOrLoss(),
                        String.valueOf(profiteOrLoss.getAmount()),  String.valueOf(monthlyBudgets.get().getMonthlyBudget())};
                csvOperations.writeDataAtCSVFile(arrayList, userId, profitLossArray);
            }
        });

    }
}
