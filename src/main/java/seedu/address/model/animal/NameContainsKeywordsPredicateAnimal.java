package seedu.address.model.animal;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicateAnimal implements Predicate<Animal> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicateAnimal(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Animal animal) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(animal.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicateAnimal)) {
            return false;
        }

        NameContainsKeywordsPredicateAnimal otherNameContainsKeywordsPredicateAnimal =
            (NameContainsKeywordsPredicateAnimal) other;
        return keywords.equals(otherNameContainsKeywordsPredicateAnimal.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
