package utilities;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class DBfetchdataexcel {

        // Database config
        private static final String JDBC_URL = "jdbc:mysql://192.168.1.136:3306/kstagedb";
        private static final String DB_USER = "kstagedbuser";
        private static final String DB_PASSWORD = "kstagedbuser";

    public void exportFilteredDataByNames(String inputExcelPath, String outputExcelPath) throws IOException, SQLException {

        // Step 1: Read names from Excel
        List<String> names = readNamesFromExcel(inputExcelPath);
        if (names.isEmpty()) {
            System.out.println("❌ No names found in input Excel: " + inputExcelPath);
            return;
        }

        // Step 2: Connect to DB and fetch matching records
        try (
                Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                Workbook outputWorkbook = new XSSFWorkbook()
        ) {
            Sheet outputSheet = outputWorkbook.createSheet("Filtered Records");
            int rowIndex = 0;
            boolean headerWritten = false;

            String query = "SELECT * FROM btob_customer WHERE NAME = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                for (String name : names) {
                    ps.setString(1, name);
                    try (ResultSet rs = ps.executeQuery()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        while (rs.next()) {
                            // Write header only once
                            if (!headerWritten) {
                                Row headerRow = outputSheet.createRow(rowIndex++);
                                for (int i = 1; i <= columnCount; i++) {
                                    headerRow.createCell(i - 1).setCellValue(metaData.getColumnName(i));
                                }
                                headerWritten = true;
                            }

                            // Write data row
                            Row row = outputSheet.createRow(rowIndex++);
                            for (int i = 1; i <= columnCount; i++) {
                                row.createCell(i - 1).setCellValue(rs.getString(i));
                            }
                        }
                    }
                }
            }

            // Step 3: Write to output Excel
            try (FileOutputStream fos = new FileOutputStream(outputExcelPath)) {
                outputWorkbook.write(fos);
            }

            System.out.println("✅ Filtered data written to: " + outputExcelPath);
        }
    }


    private List<String> readNamesFromExcel(String excelPath) throws IOException {
            List<String> names = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(excelPath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        names.add(cell.getStringCellValue().trim());
                    }
                }
            }

            return names;
        }

        public void ExceltoLocalhost(){

                    // Path to Excel file
                    String excelFilePath = "src//test//resources//ExcelFile//filtered_customer_data.xlsx";

                    // MySQL connection details
                    String jdbcURL = "jdbc:mysql://localhost:3306/testcustomer";
                    String username = "root";
                    String password = "pass";

                    try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
                         Workbook workbook = new XSSFWorkbook(fis);
                         Connection conn = DriverManager.getConnection(jdbcURL, username, password)) {

                        Sheet sheet = workbook.getSheetAt(0);

                        // Read header row (column names)
                        Row headerRow = sheet.getRow(0);
                        int colCount = headerRow.getLastCellNum();

                        // Build dynamic SQL
                        StringBuilder sql = new StringBuilder("INSERT INTO testcustomer.btob_customer (");
                        for (int i = 0; i < colCount; i++) {
                            sql.append(headerRow.getCell(i).getStringCellValue());
                            if (i < colCount - 1) sql.append(", ");
                        }
                        sql.append(") VALUES (");
                        for (int i = 0; i < colCount; i++) {
                            sql.append("?");
                            if (i < colCount - 1) sql.append(", ");
                        }
                        sql.append(")");

                        PreparedStatement stmt = conn.prepareStatement(sql.toString());

                        // Loop rows (skip header row, start at 1)
                        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                            Row row = sheet.getRow(i);
                            if (row == null) continue;

                            for (int j = 0; j < colCount; j++) {
                                Cell cell = row.getCell(j);

                                if (cell == null) {
                                    stmt.setNull(j + 1, java.sql.Types.NULL);
                                } else {
                                    String value = cell.toString().trim();
                                    if (value.isEmpty()) {
                                        stmt.setNull(j + 1, java.sql.Types.NULL);
                                    } else {
                                        stmt.setString(j + 1, value);
                                    }
                                }
                            }

                            stmt.executeUpdate();
                        }

                        System.out.println("✅ Data inserted successfully into btob_customer!");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }