package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.testutil.AnimalBuilder;
import seedu.address.testutil.PersonBuilder;

public class ContactContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstNameKeywords = Collections.singletonList("first");
        List<String> secondNameKeywords = Arrays.asList("first", "second");
        List<String> firstTagKeywords = Collections.singletonList("tag1");
        List<String> secondTagKeywords = Arrays.asList("tag1", "tag2");

        ContactContainsKeywordsPredicate<Person> firstPredicate =
                new ContactContainsKeywordsPredicate<>(firstNameKeywords, firstTagKeywords);
        ContactContainsKeywordsPredicate<Person> secondPredicate =
                new ContactContainsKeywordsPredicate<>(secondNameKeywords, secondTagKeywords);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ContactContainsKeywordsPredicate<Person> firstPredicateCopy =
                new ContactContainsKeywordsPredicate<>(firstNameKeywords, firstTagKeywords);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different name keywords only -> returns false
        ContactContainsKeywordsPredicate<Person> differentNamePredicate =
                new ContactContainsKeywordsPredicate<>(secondNameKeywords, firstTagKeywords);
        assertFalse(firstPredicate.equals(differentNamePredicate));

        // different tag keywords only -> returns false
        ContactContainsKeywordsPredicate<Person> differentTagPredicate =
                new ContactContainsKeywordsPredicate<>(firstNameKeywords, secondTagKeywords);
        assertFalse(firstPredicate.equals(differentTagPredicate));

        // both null keywords -> returns true
        ContactContainsKeywordsPredicate<Person> nullPredicate1 =
                new ContactContainsKeywordsPredicate<>(null, null);
        ContactContainsKeywordsPredicate<Person> nullPredicate2 =
                new ContactContainsKeywordsPredicate<>(null, null);
        assertTrue(nullPredicate1.equals(nullPredicate2));

        // one null name keywords, one non-null -> returns false
        ContactContainsKeywordsPredicate<Person> oneNullNamePredicate =
                new ContactContainsKeywordsPredicate<>(null, firstTagKeywords);
        assertFalse(firstPredicate.equals(oneNullNamePredicate));

        // one null tag keywords, one non-null -> returns false
        ContactContainsKeywordsPredicate<Person> oneNullTagPredicate =
                new ContactContainsKeywordsPredicate<>(firstNameKeywords, null);
        assertFalse(firstPredicate.equals(oneNullTagPredicate));
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        List<String> nameKeywords = Collections.singletonList("Alice");
        List<String> tagKeywords = Collections.singletonList("friend");

        ContactContainsKeywordsPredicate<Person> predicate1 =
                new ContactContainsKeywordsPredicate<>(nameKeywords, tagKeywords);
        ContactContainsKeywordsPredicate<Person> predicate2 =
                new ContactContainsKeywordsPredicate<>(nameKeywords, tagKeywords);

        assertEquals(predicate1.hashCode(), predicate2.hashCode());
    }

    @Test
    public void hashCode_differentValues_returnsDifferentHashCode() {
        ContactContainsKeywordsPredicate<Person> predicate1 =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"),
                        Collections.singletonList("friend"));
        ContactContainsKeywordsPredicate<Person> predicate2 =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Bob"),
                        Collections.singletonList("colleague"));

        assertNotEquals(predicate1.hashCode(), predicate2.hashCode());
    }

    @Test
    public void hashCode_nullKeywords_returnsConsistentHashCode() {
        ContactContainsKeywordsPredicate<Person> predicate1 =
                new ContactContainsKeywordsPredicate<>(null, null);
        ContactContainsKeywordsPredicate<Person> predicate2 =
                new ContactContainsKeywordsPredicate<>(null, null);

        assertEquals(predicate1.hashCode(), predicate2.hashCode());
    }

    @Test
    public void test_nullNameKeywords_matchesAll() {
        // null name keywords should match any name
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(null, Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withTags("friend").build()));
    }

    @Test
    public void test_emptyNameKeywords_matchesAll() {
        // empty name keywords should match any name
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                        Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withTags("friend").build()));
    }

    @Test
    public void test_nullTagKeywords_matchesAll() {
        // null tag keywords should match any tags
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"), null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("enemy").build()));
    }

    @Test
    public void test_emptyTagKeywords_matchesAll() {
        // empty tag keywords should match any tags
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"),
                        Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("enemy").build()));
    }

    @Test
    public void test_bothKeywordsNull_matchesAll() {
        // both null should match everything
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(null, null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withTags("enemy").build()));
    }

    @Test
    public void test_bothKeywordsEmpty_matchesAll() {
        // both empty should match everything
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withTags("enemy").build()));
    }

    @Test
    public void test_personNameContainsKeywords_returnsTrue() {
        // One keyword
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Partial match
        predicate = new ContactContainsKeywordsPredicate<>(Collections.singletonList("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void test_animalNameContainsKeywords_returnsTrue() {
        // One keyword
        ContactContainsKeywordsPredicate<Animal> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Whiskers"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers Cat").build()));

        // Multiple keywords
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("Whiskers", "Luna"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers Luna").build()));

        // Only one matching keyword
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("Luna", "Simba"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers Simba").build()));

        // Mixed-case keywords
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("wHISkers", "lUNA"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers Luna").build()));

        // Partial match
        predicate = new ContactContainsKeywordsPredicate<>(Collections.singletonList("Whisk"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers").build()));
    }

    @Test
    public void test_personTagsContainKeywords_returnsTrue() {
        // One tag
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(), Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));

        // Multiple tags - all must match
        predicate = new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                Arrays.asList("friend", "colleague"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice")
                .withTags("friend", "colleague").build()));

        // Mixed-case tags
        predicate = new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                Collections.singletonList("FrIeNd"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
    }

    @Test
    public void test_animalTagsContainKeywords_returnsTrue() {
        // One tag
        ContactContainsKeywordsPredicate<Animal> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                        Collections.singletonList("friendly"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers").withTags("friendly").build()));

        // Multiple tags - all must match
        predicate = new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                Arrays.asList("friendly", "small"));
        assertTrue(predicate.test(new AnimalBuilder().withName("Whiskers")
                .withTags("friendly", "small").build()));
    }

    @Test
    public void test_nameAndTagsContainKeywords_returnsTrue() {
        // Person: both name and tags match
        ContactContainsKeywordsPredicate<Person> personPredicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"),
                        Collections.singletonList("friend"));
        assertTrue(personPredicate.test(new PersonBuilder().withName("Alice")
                .withTags("friend").build()));

        // Animal: both name and tags match
        ContactContainsKeywordsPredicate<Animal> animalPredicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Whiskers"),
                        Collections.singletonList("friendly"));
        assertTrue(animalPredicate.test(new AnimalBuilder().withName("Whiskers")
                .withTags("friendly").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Arrays.asList("NonExistentName12345"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Different non-matching keyword
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords match phone and email, but does not match name
        predicate = new ContactContainsKeywordsPredicate<>(Arrays.asList("87654321", "alice@email.com"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("91234567")
                .withEmail("alice@email.com").build()));
    }

    @Test
    public void test_tagsDoNotContainKeywords_returnsFalse() {
        // Non-matching tag
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                        Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));

        // Only some tags match (all must match)
        predicate = new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                Arrays.asList("friend", "enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
    }

    @Test
    public void test_nameMatchesButTagsDoNot_returnsFalse() {
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Alice"),
                        Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
    }

    @Test
    public void test_tagsMatchButNameDoesNot_returnsFalse() {
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("Bob"),
                        Collections.singletonList("friend"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friend").build()));
    }

    @Test
    public void testPersonNoTags_tagKeywordsGiven_returnsFalse() {
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                        Collections.singletonList("friend"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void testAnimalNoTags_tagKeywordsGiven_returnsFalse() {
        ContactContainsKeywordsPredicate<Animal> predicate =
                new ContactContainsKeywordsPredicate<>(Collections.emptyList(),
                        Collections.singletonList("friendly"));
        assertFalse(predicate.test(new AnimalBuilder().withName("Whiskers").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("keyword1", "keyword2");
        List<String> tagKeywords = List.of("tag1", "tag2");
        ContactContainsKeywordsPredicate<Person> predicate =
                new ContactContainsKeywordsPredicate<>(nameKeywords, tagKeywords);

        String expected = ContactContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords + ", tagKeywords=" + tagKeywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
