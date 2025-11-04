package seedu.address.model.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAnimals.MAX;
import static seedu.address.testutil.TypicalAnimals.WHISKERS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.animal.exceptions.AnimalNotFoundException;
import seedu.address.model.animal.exceptions.DuplicateAnimalException;
import seedu.address.testutil.AnimalBuilder;

public class UniqueAnimalListTest {

    private final UniqueAnimalList uniqueAnimalList = new UniqueAnimalList();

    @Test
    public void contains_nullAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.contains(null));
    }

    @Test
    public void contains_animalNotInList_returnsFalse() {
        assertFalse(uniqueAnimalList.contains(WHISKERS));
    }

    @Test
    public void contains_animalInList_returnsTrue() {
        uniqueAnimalList.add(WHISKERS);
        assertTrue(uniqueAnimalList.contains(WHISKERS));
    }

    @Test
    public void contains_animalWithSameIdentityFieldsInList_returnsTrue() {
        uniqueAnimalList.add(WHISKERS);
        Animal editedWhiskers = new AnimalBuilder(WHISKERS).withDescription("Different description")
                .withTags("newTag").build();
        assertTrue(uniqueAnimalList.contains(editedWhiskers));
    }

    @Test
    public void add_nullAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.add(null));
    }

    @Test
    public void add_duplicateAnimal_throwsDuplicateAnimalException() {
        uniqueAnimalList.add(WHISKERS);
        assertThrows(DuplicateAnimalException.class, () -> uniqueAnimalList.add(WHISKERS));
    }

    @Test
    public void setAnimal_nullTargetAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.setAnimal(null, WHISKERS));
    }

    @Test
    public void setAnimal_nullEditedAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.setAnimal(WHISKERS, null));
    }

    @Test
    public void setAnimal_targetAnimalNotInList_throwsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> uniqueAnimalList.setAnimal(WHISKERS, WHISKERS));
    }

    @Test
    public void setAnimal_editedAnimalIsSameAnimal_success() {
        uniqueAnimalList.add(WHISKERS);
        uniqueAnimalList.setAnimal(WHISKERS, WHISKERS);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        expectedUniqueAnimalList.add(WHISKERS);
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimal_editedAnimalHasSameIdentity_success() {
        uniqueAnimalList.add(WHISKERS);
        Animal editedWhiskers = new AnimalBuilder(WHISKERS).withDescription("Different description")
                .withTags("newTag").build();
        uniqueAnimalList.setAnimal(WHISKERS, editedWhiskers);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        expectedUniqueAnimalList.add(editedWhiskers);
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimal_editedAnimalHasDifferentIdentity_success() {
        uniqueAnimalList.add(WHISKERS);
        uniqueAnimalList.setAnimal(WHISKERS, MAX);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        expectedUniqueAnimalList.add(MAX);
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimal_editedAnimalHasNonUniqueIdentity_throwsDuplicateAnimalException() {
        uniqueAnimalList.add(WHISKERS);
        uniqueAnimalList.add(MAX);
        assertThrows(DuplicateAnimalException.class, () -> uniqueAnimalList.setAnimal(WHISKERS, MAX));
    }

    @Test
    public void remove_nullAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.remove(null));
    }

    @Test
    public void remove_animalDoesNotExist_throwsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> uniqueAnimalList.remove(WHISKERS));
    }

    @Test
    public void remove_existingAnimal_removesAnimal() {
        uniqueAnimalList.add(WHISKERS);
        uniqueAnimalList.remove(WHISKERS);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimals_nullUniqueAnimalList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.setAnimals((UniqueAnimalList) null));
    }

    @Test
    public void setAnimals_uniqueAnimalList_replacesOwnListWithProvidedUniqueAnimalList() {
        uniqueAnimalList.add(WHISKERS);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        expectedUniqueAnimalList.add(MAX);
        uniqueAnimalList.setAnimals(expectedUniqueAnimalList);
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimals_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAnimalList.setAnimals((List<Animal>) null));
    }

    @Test
    public void setAnimals_list_replacesOwnListWithProvidedList() {
        uniqueAnimalList.add(WHISKERS);
        List<Animal> animalList = Collections.singletonList(MAX);
        uniqueAnimalList.setAnimals(animalList);
        UniqueAnimalList expectedUniqueAnimalList = new UniqueAnimalList();
        expectedUniqueAnimalList.add(MAX);
        assertEquals(expectedUniqueAnimalList, uniqueAnimalList);
    }

    @Test
    public void setAnimals_listWithDuplicateAnimals_throwsDuplicateAnimalException() {
        List<Animal> listWithDuplicateAnimals = Arrays.asList(WHISKERS, WHISKERS);
        assertThrows(DuplicateAnimalException.class, () -> uniqueAnimalList.setAnimals(listWithDuplicateAnimals));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueAnimalList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueAnimalList.asUnmodifiableObservableList().toString(), uniqueAnimalList.toString());
    }
}
