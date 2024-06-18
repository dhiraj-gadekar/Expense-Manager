/*
 * The expense manager is a java program that helps the user to keep monthly accounts. Using the program, one can keep track of their personal profits, loss, and their expenses. The user of this program can keep track of their expense, make a note of the date in which their expense is done. Likewise, they can also enter the budget for every month so that they can easily be notified if their expense made exceeds their budget for the month or not. Please see below for the full features of the software.

Features of Expense Manager :
Simple to use monthly expense manager
Keeps a track of the expenses made by the user
Make a note of the date, the item on which expenditure is done, and the cost of related expenses
Users can enter a budget for every month
Every time, when a user makes a new entry, the budget is checked, and a message is shown if expenses made exceeds the budget set by the user
Users can enter a date and view all the entries made on that specific date
User can enter a month to view all the expenses done on the specific month
Users can get the expenses details made from the time, the budget has been reset
 */

package com.expensemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.expensemanager.servise.MonthlyExpenseService;

@SpringBootApplication
@EnableJpaRepositories
public class ExpenseManagerApplication implements CommandLineRunner {

	@Autowired
	private MonthlyExpenseService monthlyExpenseService;
	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		monthlyExpenseService.expenseReportGenerator();
	}
}
