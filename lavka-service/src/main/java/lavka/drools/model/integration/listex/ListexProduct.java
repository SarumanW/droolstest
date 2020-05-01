package lavka.drools.model.integration.listex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListexProduct {

    @JsonProperty("good_id")
    private Long productId;

    @JsonProperty("good_img")
    private String imagePath;

    @JsonProperty("categories")
    private List<ListexCategory> categories;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("brand_id")
    private Long brandId;

    @JsonProperty("good_attrs")
    private List<ListexProductAttribute> attributes;

    @JsonProperty("good_name")
    private String productName;
}
