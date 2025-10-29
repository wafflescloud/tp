package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";

    private UUID id;
    private PersonName name;
    private Phone phone;
    private Email email;
    private Set<Tag> tags;
    private Set<UUID> feedingSessionIds;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        id = null; // Will auto-generate when build() is called
        name = new PersonName(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
        feedingSessionIds = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = getPersonNameSafely(personToCopy);
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        tags = new HashSet<>(personToCopy.getTags());
        id = personToCopy.getId();
        feedingSessionIds = new HashSet<>(personToCopy.getFeedingSessionIds());
    }

    /**
     * Safely extracts PersonName from a Person object.
     * This method handles the type conversion from Name to PersonName.
     */
    private PersonName getPersonNameSafely(Person person) {
        if (person.getName() instanceof PersonName) {
            return (PersonName) person.getName();
        }
        throw new IllegalStateException("Person should have PersonName, but got: " + person.getName().getClass());
    }

    /**
     * Sets the {@code PersonName} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new PersonName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Set<UUID>} of the {@code Person} that we are building.
     */
    public PersonBuilder withFeedingSessionIds(Set<UUID> feedingSessionIds) {
        this.feedingSessionIds = new HashSet<>(feedingSessionIds);
        return this;
    }

    /**
     * Builds and returns a {@link Person} using the values configured in this builder.
     * <p>
     * If this builder was initialized from an existing person (or explicitly provided an ID),
     * the same identifier will be used. Otherwise, the constructed person will have an
     * auto-generated identifier.
     * <p>
     * The returned person will include the configured {@code name}, {@code phone}, {@code email},
     * {@code tags}, and {@code feedingSessionIds}.
     *
     * @return a new {@link Person} instance based on the current builder state
     */
    public Person build() {
        if (id != null) {
            return new Person(id, name, phone, email, tags, feedingSessionIds);
        }
        return new Person(name, phone, email, tags, feedingSessionIds);
    }
}
