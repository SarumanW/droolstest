package com.drools.model.usda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodsSearchCriteria {

    private List<Integer> fdcIds;

    private String format;

    private Integer[] nutrients;
}
