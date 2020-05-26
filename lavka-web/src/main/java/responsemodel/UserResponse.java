package responsemodel;

import lavka.drools.model.entity.RelationUserProduct;
import lavka.drools.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public UserResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();

//        this.products = user.getUserProducts().stream()
//                .map(RelationUserProduct::getProduct)
//                .distinct()
//                .map(ProductResponse::new)
//                .collect(Collectors.toList());
    }
}
