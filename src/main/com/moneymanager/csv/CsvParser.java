package com.moneymanager.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvParser {
    
    public static class CsvData {
        private final List<String> headers;
        private final List<List<String>> rows;
        private final int columnCount;
        private final int rowCount;
        
        public CsvData(List<String> headers, List<List<String>> rows) {
            this.headers = new ArrayList<>(headers);
            this.rows = new ArrayList<>(rows);
            this.columnCount = headers.size();
            this.rowCount = rows.size();
        }
        
        public List<String> getHeaders() { return new ArrayList<>(headers); }
        public List<List<String>> getRows() { return new ArrayList<>(rows); }
        public int getColumnCount() { return columnCount; }
        public int getRowCount() { return rowCount; }
        
        public String getHeader(int columnIndex) {
            return columnIndex < headers.size() ? headers.get(columnIndex) : "Column " + (columnIndex + 1);
        }
        
        public String getCell(int rowIndex, int columnIndex) {
            if (rowIndex < rows.size() && columnIndex < rows.get(rowIndex).size()) {
                return rows.get(rowIndex).get(columnIndex);
            }
            return "";
        }
    }
    
    public static CsvData parseCsvFile(File csvFile, boolean hasHeaders) throws IOException {
        List<String> headersList = new ArrayList<>();
        List<List<String>> rowsList = new ArrayList<>();
        char delimiter = detectDelimiter(csvFile);
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String firstLine = br.readLine();
            
            if (firstLine != null) {
                if (hasHeaders) {
                    headersList = parseCsvLine(firstLine, delimiter);
                    headersList = headersList.stream()
                            .map(String::trim)
                            .map(s -> s.replaceAll("\\s+", " "))
                            .collect(Collectors.toList());
                } else {
                    List<String> firstRowValues = parseCsvLine(firstLine, delimiter);
                    firstRowValues = firstRowValues.stream()
                            .map(String::trim)
                            .map(s -> s.replaceAll("\\s+", " "))
                            .collect(Collectors.toList());
                    rowsList.add(firstRowValues); // Add the first line as a data row
                    
                    // Generate default headers
                    for (int i = 0; i < firstRowValues.size(); i++) {
                        headersList.add("Column " + (i + 1));
                    }
                }
            }
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                List<String> rowValues = parseCsvLine(line, delimiter);
                rowValues = rowValues.stream()
                        .map(String::trim)
                        .map(s -> s.replaceAll("\\s+", " "))
                        .collect(Collectors.toList());
                rowsList.add(rowValues);
            }
        }
        return new CsvData(headersList, rowsList);
    }
    
    private static List<String> parseCsvLine(String line, char delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            
            if (ch == '"') { // Quote character
                // Check for an escaped double quote ("")
                if (inQuotes && i + 1 < chars.length && chars[i + 1] == '"') {
                    currentField.append('"'); // Append a single quote
                    i++; // Increment i to skip the second quote of the pair
                } else {
                    // It's a non-escaped quote, so it toggles the inQuotes state
                    inQuotes = !inQuotes;
                    // Note: The quote character itself is not added to currentField here,
                    // as it's a delimiter for the quoted field.
                }
            } else if (ch == delimiter && !inQuotes) { // Delimiter, and we are not inside quotes
                result.add(currentField.toString().trim());
                currentField.setLength(0);
            } else { // Regular character, or a delimiter character inside quotes
                currentField.append(ch);
            }
        }
        // Add the last field
        result.add(currentField.toString());
        return result;
    }
    
    public static Map<Integer, String> getHeaderMapFromFile(File csvFile) {
        try {
            char delimiter = detectDelimiter(csvFile);
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String firstLine = br.readLine();
            
            if (firstLine == null) {
                return Collections.emptyMap();
            } else {
                List<String>headersList = parseCsvLine(firstLine, delimiter);
                return IntStream.range(0, headersList.size())
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> i,
                                i -> headersList.get(i).trim()
                        ));
            }
        } catch (IOException e) { System.out.println("Error in CsvParser.getHeaderMapFromFile: " + e.getMessage()); }
        
        return Collections.emptyMap();
    }
    
    private static char detectDelimiter(File csvFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            char[] delimiters = {',', ';', '\t', '|'};
            int[] counts = new int[delimiters.length];
            int linesChecked = 0;
            int maxLinesToCheck = 5; // Check first 5 lines for better accuracy
            
            String line;
            while ((line = br.readLine()) != null && linesChecked < maxLinesToCheck) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Count delimiters, but ignore those inside quotes
                boolean inQuotes = false;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    
                    // Handle quotes
                    if (c == '"') {
                        inQuotes = !inQuotes;
                        continue;
                    }
                    
                    // Count delimiters only when not inside quotes
                    if (!inQuotes) {
                        for (int j = 0; j < delimiters.length; j++) {
                            if (c == delimiters[j]) {
                                counts[j]++;
                            }
                        }
                    }
                }
                linesChecked++;
            }
            
            // Find the delimiter with highest average count per line
            int bestIndex = 0;
            double bestAverage = 0;
            
            for (int i = 0; i < delimiters.length; i++) {
                if (linesChecked > 0) {
                    double average = (double) counts[i] / linesChecked;
                    if (average > bestAverage && counts[i] > 0) {
                        bestAverage = average;
                        bestIndex = i;
                    }
                }
            }
            
            // If no delimiters found or very low count, default to comma
            if (bestAverage < 0.5) { // At least 0.5 delimiters per line on average
                return ',';
            }
            
            return delimiters[bestIndex];
        }
    }

    /*public CsvParser(File csvFile) {
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
    }*/
}
