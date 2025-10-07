package com.uchamod.estore.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountUpdater {
    private UUID productId;
    private Integer productCount;
    private Boolean isIncrease;
}
