package com.test.service;

import com.test.domain.MachineEntity;
import com.test.utils.MachineExcelExporter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MachineExcelExporterService {

    public void export(List<MachineEntity> machines, String filePath) throws IOException {
        MachineExcelExporter.exportToExcel(machines, filePath);
    }
}
