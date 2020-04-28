package lavka.functionalmodel;

import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.User;
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
