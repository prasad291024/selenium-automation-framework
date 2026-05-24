package com.prasad_v.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class TestDataGenerator {

    public static void main(String[] args) throws IOException {
        generateVWOTestData();
        generateOrangeHRMTestData();
        generateKatalonTestData();
        System.out.println("All test data files generated successfully.");
    }

    private static void generateVWOTestData() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // InvalidLogin sheet - columns: username, password, expectedError
        Sheet invalidSheet = workbook.createSheet("InvalidLogin");
        createRow(invalidSheet, 0, "username", "password", "expectedError");
        createRow(invalidSheet, 1, "invalid.user.1@example.test", "invalid-value-1", "Your email, password, IP address or location did not match");
        createRow(invalidSheet, 2, "invalid.user.2@example.test", "invalid-value-2", "Your email, password, IP address or location did not match");
        createRow(invalidSheet, 3, "invalid.user.3@example.test", "invalid-value-3", "Your email, password, IP address or location did not match");

        // ValidLogin sheet - columns: username, password
        Sheet validSheet = workbook.createSheet("ValidLogin");
        createRow(validSheet, 0, "username", "password");
        createRow(validSheet, 1, "${VWO_USERNAME}", "${VWO_PASSWORD}");

        writeFile(workbook, "src/test/resources/testdata/VWOTestData.xlsx");
    }

    private static void generateOrangeHRMTestData() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // InvalidLogin sheet - columns: username, password, expectedError
        Sheet invalidSheet = workbook.createSheet("InvalidLogin");
        createRow(invalidSheet, 0, "username", "password", "expectedError");
        createRow(invalidSheet, 1, "invalid-user-1", "invalid-value-1", "Invalid credentials");
        createRow(invalidSheet, 2, "invalid-user-2", "invalid-value-2", "Invalid credentials");
        createRow(invalidSheet, 3, "invalid-user-3", "invalid-value-3", "Invalid credentials");

        writeFile(workbook, "src/test/resources/testdata/OrangeHRMTestData.xlsx");
    }

    private static void generateKatalonTestData() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // InvalidLogin sheet - columns: username, password, expectedError
        Sheet invalidSheet = workbook.createSheet("InvalidLogin");
        createRow(invalidSheet, 0, "username", "password", "expectedError");
        createRow(invalidSheet, 1, "invalid-user-1", "invalid-value-1", "Login failed! Please ensure the username and password are valid.");
        createRow(invalidSheet, 2, "invalid-user-2", "invalid-value-2", "Login failed! Please ensure the username and password are valid.");
        createRow(invalidSheet, 3, "invalid-user-3", "invalid-value-3", "Login failed! Please ensure the username and password are valid.");

        writeFile(workbook, "src/test/resources/testdata/KatalonTestData.xlsx");
    }

    private static void createRow(Sheet sheet, int rowNum, String... values) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < values.length; i++) {
            row.createCell(i).setCellValue(values[i]);
        }
    }

    private static void writeFile(Workbook workbook, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
