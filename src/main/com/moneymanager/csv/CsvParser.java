package com.moneymanager.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvParser {

    public CsvParser(File csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            char delimiter = ',';
            line = br.readLine();
            String[] headers = line.split(String.valueOf(delimiter));
            System.out.println("Number of Collumns: " + headers.length);
            System.out.println("Headers: ");
            int i = 1;
            for (String header : headers) {
                System.out.printf("Column %s: %s\n", i, header);
                i++;
            }
            System.out.println();
            while ((line = br.readLine()) != null) {
                String[] rowValues = line.split(String.valueOf(delimiter));
                int j = 1;
                for (String rowValue : rowValues) {
                    System.out.printf("%s: %s\n", headers[j - 1], rowValue);
                    j++;
                }
                System.out.println();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
