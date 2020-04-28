package lavka.drools.model.integration.listex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListexCategory {

    @JsonProperty("cat_id")
    private Long categoryId;

    @JsonProperty("cat_name")
    private String categoryName;

    @JsonProperty("cat_parent_id")
    private Long parentCategoryId;

    @JsonProperty("cat_level")
    private int categoryLevel;
}
