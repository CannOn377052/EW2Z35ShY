// 代码生成时间: 2025-09-04 07:32:20
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.List;

public class ExcelGenerator {

    // Method to create an Excel file
    public void createExcelFile(String filePath, List<List<Object>> data) {
        // Check if the file path is not null or empty
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a new sheet
            Sheet sheet = workbook.createSheet("Sheet1");
            int rowNum = 0;

            // Iterate over the data to create rows in the Excel sheet
            for (List<Object> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 0;

                for (Object cellData : rowData) {
                    Cell cell = row.createCell(cellNum++);
                    cell.setCellValue(cellData.toString());
                }
            }

            // Write the workbook to the file system
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write Excel file.", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel workbook.", e);
        }
    }

    // Main method to demonstrate the usage of ExcelGenerator
    public static void main(String[] args) {
        ExcelGenerator generator = new ExcelGenerator();
        String filePath = "example.xlsx";
        List<List<Object>> data = List.of(
            List.of("Header1", "Header2", "Header3"),
            List.of("Data1", 123, true),
            List.of("Data2", 456, false)
        );

        try {
            generator.createExcelFile(filePath, data);
            System.out.println("Excel file created successfully: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating Excel file: " + e.getMessage());
        }
    }
}
