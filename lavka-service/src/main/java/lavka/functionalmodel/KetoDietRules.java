package lavka.functionalmodel;


import lavka.drools.model.entity.Diet;
import lavka.drools.utils.RestrictionImport;

import java.util.List;
import java.util.function.Consumer;

public class KetoDietRules {
    private static Consumer<UserProductPair> ketoDietIdIndicatePredicate = userProductPair -> {
        if (userProductPair.getUser().getFollowedDiets()
                .stream()
                .filter(d -> d.getId().equals(4L))
                .findAny()
                .orElse(null) != null) {
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aDI, 4);
        }
    };

    private static Consumer<UserProductPair> ketoDietIndicateAssertion = userProductPair -> userProductPair.getUser()
            .getAttributeObjectMap().put(Attribute.aDIE, Diet.DietType.KETO);

    private static Rule<UserProductPair> ketoDietIndicateRule = new Rule(1, 1,
            ketoDietIdIndicatePredicate, ketoDietIndicateAssertion, 2, 2);

    //-----

    private static Consumer<UserProductPair> ketoDietRestrictionPredicate = userProductPair -> {

    };

    private static Consumer<UserProductPair> ketoDietRestrictionAssertion = userProductPair -> {
        if (userProductPair.getUser()
                .getAttributeObjectMap().get(Attribute.aDIE).equals(Diet.DietType.KETO)) {

            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aRC,
                    RestrictionImport.getRestrictionsByDietId(Diet.DietType.KETO.getId()).getRestrictedCategoriesIds());

            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aRW,
                    RestrictionImport.getRestrictionsByDietId(Diet.DietType.KETO.getId()).getRestrictedItems());
        }
    };

    private static Rule<UserProductPair> ketoDietRestrictionRule = new Rule(2, 2,
            ketoDietRestrictionPredicate, ketoDietRestrictionAssertion, 3, 3);

    public static List<Rule> getRules() {
        return List.of(ketoDietIndicateRule, ketoDietRestrictionRule);
    }
}
