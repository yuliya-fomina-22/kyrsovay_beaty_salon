package ru.kyrsach.kyrsach.dao;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Service {
    private int serviceId;
    private String name;
    private String category;
    private int duration;
    private double basePrice;
}