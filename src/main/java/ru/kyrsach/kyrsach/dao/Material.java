package ru.kyrsach.kyrsach.dao;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class Material {
    private int materialId;
    private String name;
    private int quantityOnHand;
}
