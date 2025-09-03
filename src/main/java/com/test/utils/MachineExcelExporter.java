package com.test.utils;

import com.test.domain.MachineEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class MachineExcelExporter {

    private static final String[] HEADERS = {
            "Model", "Contract Type", "Mark", "Year Model",
            "Worked Hours", "City", "Price", "Images"
    };


    public static void exportToExcel(List<MachineEntity> machines, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Machines");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            int rowIdx = 1;
            for (MachineEntity machine : machines) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(machine.getModel());
                row.createCell(1).setCellValue(machine.getContractType());
                row.createCell(2).setCellValue(machine.getMark());
                row.createCell(3).setCellValue(machine.getYearModel());
                row.createCell(4).setCellValue(machine.getWorkedHours());
                row.createCell(5).setCellValue(machine.getCity());
                row.createCell(6).setCellValue(machine.getPrice());

                String imagesConcatenated = machine.getImages() != null
                        ? String.join(", ", machine.getImages())
                        : "";
                row.createCell(7).setCellValue(imagesConcatenated);
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileStream = new FileOutputStream(filePath)) {
                workbook.write(fileStream);
            }
        }
    }
}
