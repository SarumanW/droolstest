package lavka.drools.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lavka.functionalmodel.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "user_seq", allocationSize = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    private String login;

    private String password;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "user_diet",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_id"))
    private List<Diet> followedDiets;

    private boolean supportsSeasonality;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "shown = true")
    private List<RelationUserProduct> userProducts;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    @Where(clause = "forbidden = true")
    private List<RelationUserIngredient> forbiddenIngredients;

    public synchronized void addProductToList(Product product) {
        if (userProducts == null) {
            userProducts = new ArrayList<>();
        }

        RelationUserProduct relationUserProduct = new RelationUserProduct(this, product);

        if (userProducts.indexOf(relationUserProduct) == -1) {
            userProducts.add(relationUserProduct);
        }
    }

    //TODO: add likes

    @Transient
    private Map<Attribute, Object> attributeObjectMap = new HashMap<>();
}
