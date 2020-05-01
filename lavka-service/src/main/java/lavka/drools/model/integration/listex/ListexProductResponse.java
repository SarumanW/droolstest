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
public class ListexProductResponse {

    @JsonProperty("apiversion")
    private int apiVersion;

    @JsonProperty("result")
    private List<ListexProduct> result;
}
