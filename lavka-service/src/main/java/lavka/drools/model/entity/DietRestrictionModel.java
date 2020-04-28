package lavka.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietRestrictionModel {

    private List<Long> restrictedCategoriesIds;

    private Long dietId;

    private List<String> restrictedItems;
}
