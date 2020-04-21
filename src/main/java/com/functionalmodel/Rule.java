package com.functionalmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule<T> {
    private int groupId;
    private int ruleId;

    private Consumer<T> predicate;
    private Consumer<T> assertion;

    private int nextRuleGroupId;
    private int nextRuleId;

    private Consumer<T> fire = input -> predicate.andThen(assertion).andThen(new Rule().getFire()).accept(input);

    public Rule(int groupId, int ruleId, Consumer<T> predicate, Consumer<T> assertion, int nextRuleGroupId, int nextRuleId) {
        this.groupId = groupId;
        this.ruleId = ruleId;
        this.predicate = predicate;
        this.assertion = assertion;
        this.nextRuleGroupId = nextRuleGroupId;
        this.nextRuleId = nextRuleId;
    }
}
