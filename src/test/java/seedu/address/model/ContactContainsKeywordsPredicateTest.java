package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
