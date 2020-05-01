package lavka.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "ingr_seq", allocationSize = 1)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingr_seq")
    private Long id;

    private String name;

    private boolean rare;

//    @ManyToMany(mappedBy = "composition")
//    private List<Product> products;

    public Ingredient(Long id, String name) {
        this.id = id;
        this.name = name;
        this.rare = false;
    }

    public Ingredient(String name) {
        this.name = name;
        this.rare = false;
    }
}
