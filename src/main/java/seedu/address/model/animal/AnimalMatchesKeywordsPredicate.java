package seedu.address.model.animal;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that an {@code Animal} matches the provided name substrings (any-match, case-insensitive)
 * and/or has all of the provided tags (all-match, case-insensitive exact match).
 * If a list is empty, that criterion is ignored.
 */
public class AnimalMatchesKeywordsPredicate implements Predicate<Animal> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;

    /**
     * Constructs an AnimalMatchesKeywordsPredicate with the specified name and tag keywords.
     *
     * @param nameKeywords List of name keywords to match against animal names (substring match, case-insensitive).
     *                     Can be null or empty to ignore name filtering.
     * @param tagKeywords List of tag keywords to match against animal tags (exact match, case-insensitive).
     *                    All provided tags must be present in the animal. Can be null or empty to ignore tag filtering.
     */
    public AnimalMatchesKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Animal animal) {
        boolean nameOk = nameKeywords == null || nameKeywords.isEmpty()
                || nameKeywords.stream().anyMatch(k ->
                    animal.getName().fullName.toLowerCase().contains(k.toLowerCase()));

        boolean tagsOk = tagKeywords == null || tagKeywords.isEmpty()
                || tagKeywords.stream().allMatch(tk -> animal.getTags().stream()
                        .map(t -> t.tagName)
                        .anyMatch(existing -> existing.equalsIgnoreCase(tk)));

        return nameOk && tagsOk;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AnimalMatchesKeywordsPredicate)) {
            return false;
        }
        AnimalMatchesKeywordsPredicate o = (AnimalMatchesKeywordsPredicate) other;
        return Objects.equals(nameKeywords, o.nameKeywords)
                && Objects.equals(tagKeywords, o.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}

