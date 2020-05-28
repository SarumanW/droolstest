package responsemodel;

import lavka.drools.model.entity.Diet;
import lavka.drools.model.entity.RelationUserProduct;
import lavka.drools.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String login;

    private String password;

    private List<ProductResponse> products;

    private List<String> restrictedItems;

    private List<String> diets;

    public UserResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();

        this.restrictedItems = Arrays.asList(user.getForbiddenIngredients().split(","));

        this.diets = user.getFollowedDiets().stream()
                .map(Diet::getName)
                .collect(Collectors.toList());

        this.products = user.getUserProducts().stream()
                .map(RelationUserProduct::getProduct)
                .distinct()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
