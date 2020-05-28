package lavka.drools.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lavka.functionalmodel.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
import org.springframework.security.core.parameters.P;

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
    @JsonManagedReference
    private List<RelationUserProduct> userProducts;

    @Lob
    private String forbiddenIngredients;

    @Transient
    @JsonIgnore
    private List<Product> ruleProducts;

    public synchronized void addProductToList(Product product) {
        if (ruleProducts == null) {
            ruleProducts = new ArrayList<>();
        }

        if (!ruleProducts.contains(product)) {
            ruleProducts.add(product);
        } else {
            System.out.println("Worked!");
        }
    }

    public synchronized void deleteProductFromList(Product product) {
        if (ruleProducts != null) {

            if (ruleProducts.contains(product)) {
                ruleProducts.remove(product);
            } else {
                System.out.println("Worked Worked!");
            }
        }
    }

    //TODO: add likes

    @Transient
    private Map<Attribute, Object> attributeObjectMap = new HashMap<>();
}
