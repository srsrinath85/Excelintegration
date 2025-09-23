package utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class dataexcelmain {

        // Original method (already correct)
        public static List<Map<String, String>> readExcelData(String filePath) throws IOException {
            List<Map<String, String>> excelData = new ArrayList<>();

            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = filePath.endsWith(".xlsx")
                    ? new XSSFWorkbook(fileInputStream)
                    : new HSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = new LinkedHashMap<>();

            for (int columnIndex = 0; columnIndex < headerRow.getPhysicalNumberOfCells(); columnIndex++) {
                Cell headerCell = headerRow.getCell(columnIndex);
                if (headerCell != null) {
                    columnMap.put(headerCell.getStringCellValue(), columnIndex);
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Map<String, String> rowData = new HashMap<>();
                for (String columnName : columnMap.keySet()) {
                    int columnIndex = columnMap.get(columnName);
                    Cell cell = row.getCell(columnIndex);
                    String cellValue = getCellValue(cell);
                    rowData.put(columnName, cellValue);
                }
                excelData.add(rowData);
            }

            workbook.close();
            fileInputStream.close();

            return excelData;
        }

    @DataProvider(name = "excelDataProvider")
    public Object[][] getData() throws IOException {
        List<Map<String, String>> list = readExcelData(new ConfigLoader().getProperty("InputExcelpath"));

        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i); // Each row is a Map<String, String>
        }

        return data;
    }

        // Existing helper
        private static String getCellValue(Cell cell) {
            String value = "";
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING: value = cell.getStringCellValue(); break;
                    case NUMERIC: value = String.valueOf(cell.getNumericCellValue()); break;
                    case BOOLEAN: value = String.valueOf(cell.getBooleanCellValue()); break;
                    case FORMULA: value = cell.getCellFormula(); break;
                    default: value = "";
                }
            }
            return value;
        }
    }
