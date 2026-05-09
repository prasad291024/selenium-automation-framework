package com.prasad_v.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {

    private static Workbook book;
    private static Sheet sheet;

    public static Object[][] getTestData(String filePath, String sheetName) {
        try (FileInputStream file = new FileInputStream(filePath)) {
            book = WorkbookFactory.create(file);
            sheet = book.getSheet(sheetName);
        } catch (IOException e) {
            LoggerUtil.error("Failed to read Excel file: " + filePath, e);
            return new Object[0][0];
        }

        int rows = sheet.getLastRowNum();
        int cols = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
            }
        }
        return data;
    }
}
