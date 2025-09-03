package com.test.domain;

import com.test.interfaces.Machine;
import lombok.Data;

import java.util.List;

@Data
public class MachineEntity implements Machine {

    private String model;
    private String contractType;
    private String mark;
    private String yearModel;
    private String workedHours;
    private String city;
    private String price;
    private List<String> images;

}
