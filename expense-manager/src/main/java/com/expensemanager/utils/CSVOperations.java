package com.expensemanager.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.expensemanager.repository.UserRepository;
import com.opencsv.CSVWriter;


@Component
public class CSVOperations {

    @Autowired
    private UserRepository userRepository;

    public void writeDataAtCSVFile(List<String[]> list, int userId, String[] profitLossArray) {

        String[] titles = new String[] { "Cost", "Date", "Item" };
        list.add(0, titles);

        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter("expense-reports/"
                    + String.valueOf(userRepository.getUserName(userId))+ "-"
                    + LocalDate.now().minusMonths(1).getMonth().toString() + "-" + LocalDate.now().getYear() + ".csv"));
            csvWriter.writeAll(list);
            csvWriter.writeNext(new String[] {});

            csvWriter.writeNext(new String[] { "Profit/Loss", "Amount", "Total Budget"});
            csvWriter.writeNext(profitLossArray);
            csvWriter.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
