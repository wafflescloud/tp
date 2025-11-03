package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Contact}'s {@code Name} and/or {@code Tag}s match any of the keywords given.
 * Works for both Person and Animal since they both extend Contact.
 * Supports searching by name keywords, tag keywords, or both.
 */
public class ContactContainsKeywordsPredicate<T extends Contact> implements Predicate<T> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;

    /**
     * Creates a predicate that tests contacts against name and tag keywords.
     *
     * @param nameKeywords List of keywords to match against contact names (can be null or empty to skip name matching)
     * @param tagKeywords List of keywords to match against contact tags (can be null or empty to skip tag matching)
     */
    public ContactContainsKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
    }

    /**
     * Creates a predicate that tests contacts against name keywords only.
     *
     * @param nameKeywords List of keywords to match against contact names
     */
    public ContactContainsKeywordsPredicate(List<String> nameKeywords) {
        this(nameKeywords, List.of());
    }

    @Override
    public boolean test(T contact) {
        boolean nameMatches = nameKeywords == null || nameKeywords.isEmpty()
                || nameKeywords.stream().anyMatch(keyword ->
                        contact.getName().toString().toLowerCase().contains(keyword.toLowerCase()));

        boolean tagsMatch = tagKeywords == null || tagKeywords.isEmpty()
                || tagKeywords.stream().allMatch(tagKeyword ->
                        contact.getTags().stream()
                                .map(tag -> tag.tagName)
                                .anyMatch(tagName -> tagName.equalsIgnoreCase(tagKeyword)));

        return nameMatches && tagsMatch;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ContactContainsKeywordsPredicate<?>)) {
            return false;
        }

        ContactContainsKeywordsPredicate<?> otherPredicate = (ContactContainsKeywordsPredicate<?>) other;
        return (nameKeywords == null ? otherPredicate.nameKeywords == null
                : nameKeywords.equals(otherPredicate.nameKeywords))
                && (tagKeywords == null ? otherPredicate.tagKeywords == null
                : tagKeywords.equals(otherPredicate.tagKeywords));
    }

    @Override
    public int hashCode() {
        int result = nameKeywords != null ? nameKeywords.hashCode() : 0;
        result = 31 * result + (tagKeywords != null ? tagKeywords.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}
