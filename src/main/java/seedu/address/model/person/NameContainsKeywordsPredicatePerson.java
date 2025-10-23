package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicatePerson implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicatePerson(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicatePerson)) {
            return false;
        }

        NameContainsKeywordsPredicatePerson otherNameContainsKeywordsPredicatePerson =
            (NameContainsKeywordsPredicatePerson) other;
        return keywords.equals(otherNameContainsKeywordsPredicatePerson.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
