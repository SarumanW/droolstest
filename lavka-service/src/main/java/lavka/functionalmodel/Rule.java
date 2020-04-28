package lavka.functionalmodel;

import java.util.function.Consumer;

public class Rule<T> {
    private int groupId;
    private int ruleId;

    private Consumer<T> predicate;
    private Consumer<T> assertion;

    private int nextRuleGroupId;
    private int nextRuleId;

    private Consumer<T> fire = input -> predicate.andThen(assertion)
            .andThen(fireNextRule())
            .accept(input);

    private Consumer<T> fireNextRule() {
        if (nextRuleGroupId != 0 && nextRuleId != 0) {
            return RuleEngine.rules.stream()
                    .filter(r -> r.getGroupId() == nextRuleGroupId)
                    .filter(r -> r.getRuleId() == nextRuleId)
                    .findFirst()
                    .orElse(new Rule())
                    .getFire();
        } else {
            return t -> {
            };
        }
    }

    public Rule(int groupId, int ruleId, Consumer<T> predicate, Consumer<T> assertion, int nextRuleGroupId, int nextRuleId) {
        this.groupId = groupId;
        this.ruleId = ruleId;
        this.predicate = predicate;
        this.assertion = assertion;
        this.nextRuleGroupId = nextRuleGroupId;
        this.nextRuleId = nextRuleId;
    }

    public Rule() {
    }

    public int getGroupId() {
        return groupId;
    }

    public int getRuleId() {
        return ruleId;
    }


    public Consumer<T> getFire() {
        return fire;
    }

}
