package lavka.drools.model.integration.usda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsdaFoodsSearchCriteria {

    private List<Integer> fdcIds;

    private String format;

    private Integer[] nutrients;
}
