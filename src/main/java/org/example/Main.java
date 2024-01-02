package org.example;

import org.example.data.SQLiteDataRetrieval;
import org.example.repo.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        new ReportGenerator().generateReports();
        SQLiteDataRetrieval.closeConnection();
    }
}