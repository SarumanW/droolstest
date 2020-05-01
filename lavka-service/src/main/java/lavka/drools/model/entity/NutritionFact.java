package lavka.drools.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class NutritionFact {

    @Id
    private Long id;

    private String name;

    public enum NutritionFacts {
        PROTEIN("1", "Protein"),
        FAT("2", "Fat"),
        CARBS("3", "Carbs"),
        ENERGY("4", "Energy");

        private String code;
        private String name;

        NutritionFacts(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public static List<NutritionFact> getNutritionFacts() {
            return Arrays.stream(NutritionFacts.values())
                    .map(facts -> new NutritionFact(Long.valueOf(facts.code), facts.name))
                    .collect(Collectors.toList());
        }
    }

    public NutritionFact() {
    }

    public NutritionFact(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
