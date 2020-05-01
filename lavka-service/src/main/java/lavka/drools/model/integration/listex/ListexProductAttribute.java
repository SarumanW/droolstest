package lavka.drools.model.integration.listex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListexProductAttribute {

    @JsonProperty("attr_id")
    private Long attributeId;

    @JsonProperty("attr_name")
    private String attributeName;

    @JsonProperty("attr_value")
    private String attributeValue;

    @JsonProperty("attr_value_type")
    private String attributeValueType;

    @JsonProperty("attr_group_id")
    private Long attributeGroupId;
}
