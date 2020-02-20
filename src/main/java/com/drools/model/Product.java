package com.drools.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private List<String> composition;

    private boolean showToUser;

    private String name;
}
