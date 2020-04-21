package com.functionalmodel;

import com.drools.model.entity.Product;
import com.drools.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProductPair {

    private User user;
    private Product product;
}
