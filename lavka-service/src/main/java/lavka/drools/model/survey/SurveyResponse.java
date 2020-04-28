package lavka.drools.model.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponse {
    private Long userId;
    private Map<String, List<String>> questionAnswers;
}
