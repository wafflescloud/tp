---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Furiends Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the [AddressBook-Level3](https://se-education.org/addressbook-level3/) project created by the [SE-EDU initiative](https://se-education.org).

* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

* The undo/redo implementation was inspired by the proposed undo/redo feature in [AddressBook-Level3](https://se-education.org/addressbook-level3/), adapted to use a stack-based approach with `State` snapshots instead of the `VersionedAddressBook` pattern

* The UUID-based entity relationship design for managing Person-Animal-FeedingSession relationships was inspired by database design principles to avoid circular dependencies

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](https://ay2526s1-cs2103t-w14-3.github.io/tp/SettingUp.html).

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-before: always;"></div>

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete person John`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="600" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.)

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300"/>

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete person Jack")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`, `DeletePersonCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeletePersonCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes (e.g. `AddPPersonCommandParser`) shown above to parse the specific user command directed at person/animal and create a `XYZCommand` object (e.g.`ListCommand`, `AddPersonCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.
* All `XYZTypeCommand` inherit from their respective `XYZCommand` so that they can be treated similarly where possible

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelOverallClassDiagram.puml" width="500"/>

The `Model` component manages three main entity types: Person, Animal, and FeedingSession.

* `AddressBook` contains three unique lists: `UniquePersonList`, `UniqueAnimalList`, and `UniqueFeedingSessionList`
* `ModelManager` maintains the `AddressBook` and manages undo/redo functionality with `undoStack` and `redoStack`

<div style="page-break-before: always;"></div>

#### Person Model

<puml src="diagrams/ModelPersonClassDiagram.puml" />

The Person model stores:
* `UUID id` - Unique identifier for each person
* `Name name` - The person's name
* `Phone phone` - Contact phone number
* `Email email` - Email address
* `Set<Tag> tags` - Tags for categorization
* `Set<UUID> feedingSessionIds` - References to feeding sessions by UUID

<div style="page-break-before: always;"></div>

#### Animal Model

<puml src="diagrams/ModelAnimalClassDiagram.puml" />

The Animal model stores:
* `UUID id` - Unique identifier for each animal
* `Name name` - The animal's name
* `Description description` - Details about the animal
* `Location location` - Where the animal is located
* `Set<Tag> tags` - Tags for categorization
* `Set<UUID> feedingSessionIds` - References to feeding sessions by UUID

<div style="page-break-before: always;"></div>

#### FeedingSession Model

<puml src="diagrams/ModelFeedingSessionClassDiagram.puml"/>

The FeedingSession model links Person and Animal entities:
* `UUID id` - Unique identifier for each feeding session
* `UUID animalId` - Reference to the Animal being fed
* `UUID personId` - Reference to the Person feeding
* `LocalDateTime dateTime` - When the feeding occurred

<div style="page-break-before: always;"></div>

#### Undo/Redo Implementation

The `Model` component implements undo/redo functionality using two stacks that store snapshots of the address book state.

**The undo/redo mechanism stores:**
* `Stack<State> undoStack` - Stores previous address book states for undo operations
* `Stack<State> redoStack` - Stores undone states for redo operations
* Each `State` object contains a complete snapshot of the `AddressBook` at a particular point in time

<br>

**How Undo/Redo Works:**

1. **State Management**: Before any modifying command executes, it calls `Model#saveState()` which:
   * Pushes the current `AddressBook` state onto the `undoStack`
   * Clears the `redoStack` (executing a new command invalidates redo history)

<puml src="diagrams/ExecuteCommandActivityDiagram.puml" alt="ExecuteCommandActivityDiagram" width="400"/>

2. **Undo Operation**: When the user executes `undo`, the system:
   * Pushes the current state to `redoStack`
   * Pops the previous state from `undoStack`
   * Restores the address book to that previous state

<puml src="diagrams/UndoActivityDiagram.puml" alt="UndoActivityDiagram" width="400" />

<div style="page-break-before: always;"></div>

3. **Redo Operation**: When the user executes `redo`, the system:
   * Pushes the current state to `undoStack`
   * Pops the next state from `redoStack`
   * Restores the address book to that next state

<puml src="diagrams/RedoActivityDiagram.puml" alt="RedoActivityDiagram" width="400"/>

<br>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model"/>

Commands that modify the address book and thus support undo/redo:
* `add person` / `add animal` / `feed` - Add entities
* `edit person` / `edit animal` - Edit entities
* `delete person` / `delete animal` / `delete feed` - Delete entities
* `clear` - Clear all entries

Commands that do not modify the address book and thus do not support undo/redo:
* `list` - Lists all persons/animals
* `find` - Finds persons/animals
* `help` - Shows help information
* `exit` - Exits the application

<box type="info" seamless>

**Note:** If a command fails its execution (e.g., deleting a non-existent person), it will not call `Model#saveState()`, so the address book state will not be saved into the `undoStack`.

</box>

#### How the Model Works

The `Model` component:

* **Stores address book data** in three unique lists:
  * `UniquePersonList` - Contains all Person objects
  * `UniqueAnimalList` - Contains all Animal objects
  * `UniqueFeedingSessionList` - Contains all FeedingSession objects

<br>

* **Maintains filtered views** of persons and animals:
  * Stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
  * Stores the currently 'selected' `Animal` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Animal>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.

<br>

* **Uses UUID-based references** to link entities:
  * Person and Animal objects store `Set<UUID> feedingSessionIds` containing feeding session IDs
  * FeedingSession objects store `UUID animalId` and `UUID personId` referencing Person and Animal
  * This loose coupling allows for flexible relationships without circular dependencies

<br>

* **Stores user preferences** in a `UserPref` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.

<br>

* **Does not depend on other components** as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components.


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-W14-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="700" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Proposed Enhancements**
This section describes some details on how certain planned enhancement features can be implemented in future iterations.

### Flexibility for name and tag
* Allow special characters like apostrophes `/` and `s/o` in names.

* Allow valid characters like `'` in tags.

* Ensures commands and storage handle more natural user input.

### Duplicate Names with Unique Contact Identifiers

* Names can be duplicated.

* Phone numbers and emails must remain unique.

* Commands may need indices or IDs to specify contacts unambiguously.

### Index-Based Commands for Fast Typing

* Use contact/animal indices instead of full names for commands.

* Reduces typing effort and ambiguity.

* Works in conjunction with duplicate names support.

### More convenient closing of `help` and `view` windows
* Enable users to close contact or help cards using a command or shortcut key (e.g. `Esc`).

* Improves navigation and user experience.

### Real time update of all windows
* Opened help/profile windows do not automatically reflect edits occur in main window.

* Requires live data binding, observer pattern, or refresh mechanism to stay updated.

### Adding of profile pictures for contacts
* Users are able to add/edit contacts' images

<div style="page-break-before: always;"></div> 

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* is a stray animal feeder
* has a need to manage a significant number of contacts
* will like to manage a significant number of contacts of other animal caretakers
* will like to keep track of the animals present around campus
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps


**Value proposition**:
* manage contacts faster than a typical mouse/GUI driven app
* keep track of animals' details around campus



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                              | I want to …​                                                                                 | So that I can…​                                                                            |
|----------|------------------------------------------------------|----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| `* * *`  | new user                                             | view a general guide on how to use the application                                           | learn how to get started quickly                                                           |
| `* * *`  | general user                                         | access the guide anytime                                                                     | refer to it whenever I forget how to use certain features                                  |
| `* * *`  | general user                                         | view the list of added contact entries                                                       | easily keep track of entries I’ve added                                                    |
| `* * *`  | general user                                         | add a contact entry                                                                          | keep track of their information for future references                                      |
| `* * *`  | general user                                         | delete a contact entry                                                                       | remove contact entries that are no longer relevant                                         |
| `* * *`  | general user                                         | edit an existing contact entry                                                               | keep my contact information accurate and up to date                                        |
| `* * *`  | general animal lover in NUS                          | keep track of the animals I have encountered in NUS                                          | revisit or check on them later                                                             |
| `* * *`  | general animal feeder in NUS                         | add contact entry of an existing feeder of NUS                                               | keep track of their information for future references and stay in contact with them        |
| `* * *`  | general animal feeder in NUS                         | delete contact entry of an existing feeder of NUS                                            | remove irrelevant contacts that I do not need to keep track of anymore                     |
| `* * *`  | general animal feeder in NUS                         | edit contact entry of an existing feeder of NUS                                              | keep their information updated so as to stay in contact with them                          |
| `* * *`  | general animal feeder in NUS                         | view the list of added contact entries of Cats of NUS members                                | keep track of all contact entries of added members                                         |
| `* * *`  | general animal feeder in NUS                         | view the list of added contact entries of cats                                               | keep track of all contract entries of added cats and monitor them easily                   |
| `* *`    | new user                                             | want to browse a preset list of caretakers found in NUS                                      | learn more about the existing caretakers in campus and get acquainted with them            |
| `* *`    | new user                                             | want to browse a preset list of animals found in NUS                                         | understand more about the diversity of animals around campus                               |
| `* *`    | new user                                             | view detailed instructions on how to use a specific command quickly                          | understand how to use each command quickly and effectively                                 |
| `* *`    | general user                                         | find a contact entry by name                                                                 | get information of contact entries easily                                                  |
| `* *`    | general animal lover in NUS                          | indicate a small note of the cat’s personality traits (e.g., friendly, shy, playful)         | remember its behaviour and interact appropriately in future encounters                     |
| `* *`    | member of Cats of NUS                                | find the contact entry of a Cats of NUS member by name                                       | locate details of members without having to go through the entire list                     |
| `* *`    | member of Cats of NUS                                | find the contact entry of a cat by name                                                      | locate details of cats without having to go through the entire list                        |
| `* *`    | member of Cats of NUS                                | add quick notes or tags ("looked sick", "shy today")                                         | remember to follow up or inform others                                                     |
| `*`      | general user                                         | view other feeders’ contact details quickly                                                  | obtain relevant contact details of a person quickly without having to go through the list  |
| `*`      | general user                                         | view animals’ contact details quickly                                                        | obtain relevant contact details of an animal quickly without having to go through the list |
| `*`      | organised user                                       | categorise contacts (Cats of NUS members or existing feeder of NUS, animals encountered etc) | differentiate person and animal contact entries easily                                     |
| `*`      | member of Cats of NUS                                | view a specific contact entry of a Cats of NUS member                                        | access contact details of the member quickly                                               |
| `*`      | member of Cats of NUS                                | view a specific contact entry of a cat                                                       | access details of the cat quickly                                                          |
| `*`      | Person-In-Charge (PIC) of the members of Cats of NUS | add feeding sessions                                                                         | assign cats and feeders to a specific feeding session for better organisation              |
| `*`      | PIC of the members of Cats of NUS                    | remove feeding sessions                                                                      | keep up to date with existing and upcoming feeding sessions                                |

### Use cases

(For all use cases below, the **System** is `Furiends` and the **Actor** is the `user`, where `user` refers to a cat caretaker using the Furiends app to manage contacts, unless specified otherwise)

**Use case: UC01 - Add a person**

**MSS**

1. User requests to add a new person by providing the person’s name, phone number, and email together
2. Furiends validates the details
3. Furiends adds the person to the list
4. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. One or more details are missing or invalid.

    * 2a1. Furiends shows an error message.

        Use case ends
* 3a. A person with the same name already exists in the list.

    * 3a1. Furiends shows a duplicate warning.

        Use case ends

<div style="page-break-before: always;"></div>

**Use case: UC02 - Delete a person**

**MSS**

1. User requests to delete a person by specifying the person’s name
2. Furiends checks if the person exists
3. Furiends deletes the person
4. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any person.

    * 2b1. Furiends shows an error message.

        Use case ends


**Use case: UC03 - Edit a person**

**MSS**

1. User requests to edit a person by providing the person’s name together with the updated details (phone number, email, animal being fed, and/or date and time of feeding)
2. Furiends checks if the person exists
3. Furiends validates the new details
4. Furiends updates the person’s information
5. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any person.

    * 2b1. Furiends shows an error message.

        Use case ends
* 3a. One or more new details are invalid.

    * 3a1. Furiends shows an error message.

        Use case ends
* 4a. The updated details duplicate another person’s name.

    * 4a1. Furiends shows a duplicate warning.

        Use case ends

<div style="page-break-before: always;"></div>

**Use case: UC04 - Find a person**

**MSS**

1. User requests to find a person by specifying the person’s name or substring of their name, or a tag name
2. Furiends searches the contact list for a matching persons
3. Furiends shows the list of persons that match the search criteria

    Use case ends

**Extensions**

* 2a. No person matches the given name or substring of name.

    * 2a1. Furiends shows an empty result message.

        Use case ends
* 2b. No person matches the tag provided.

    * 2b1. Furiends shows an empty result message.

        Use case ends


**Use case: UC05 - Add an animal**

**MSS**

1. User requests to add a new animal by providing the animal’s name, description, and location together
2. Furiends validates the details
3. Furiends adds the animal to the list
4. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. One or more details are missing or invalid.

    * 2a1. Furiends shows an error message.

        Use case ends
* 3a. An animal with the same name already exists in the list.

    * 3a1. Furiends shows a duplicate warning.

        Use case ends


**Use case: UC06 - Delete an animal**

**MSS**

1. User requests to delete an animal by specifying the animal’s name
2. Furiends checks if the animal exists
3. Furiends deletes the animal
4. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any animal.

    * 2b1. Furiends shows an error message.

        Use case ends


**Use case: UC07 - Edit an animal**

**MSS**

1. User requests to edit an animal by providing the animal’s name together with the updated description and/or location
2. Furiends checks if the animal exists
3. Furiends validates the new details
4. Furiends updates the animal’s information
5. Furiends shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any animal.

    * 2b1. Furiends shows an error message.

        Use case ends
* 3a. One or more new details are invalid.

    * 3a1. Furiends shows an error message.

        Use case ends
* 4a. The updated details duplicate another animal’s name.

    * 4a1. Furiends shows a duplicate warning.

        Use case ends


**Use case: UC08 - Find an animal**

**MSS**

1. User requests to find an animal by specifying the animal’s name or substring of their name, or a tag name
2. Furiends searches the animal list for a matching entry
3. Furiends shows a list of animals that match the search criteria

    Use case ends

**Extensions**

* 2a. No animal matches the given name or substring of name.

    * 2a1. Furiends shows an empty result message.

        Use case ends
* 2b. No animal matches the tag provided.

    * 2b1. Furiends shows an empty result message.

        Use case ends

<div style="page-break-before: always;"></div>

**Use case: UC09 - Add a feeding session**

**MSS**

1. User requests to add a new feeding session by providing the person’s name, animal’s name, date and time of feeding together
2. Furiends checks whether the person and animal exists, and that the date and time provided is valid
3. Furiends adds the feeding session to the list
4. Furiends shows a confirmation message and shows a feeding session at the involved person and animal's record

    Use case ends

**Extensions**

* 2a. The person or animal does not exist.

    * 2a1. Furiends shows an error message.

        Use case ends

* 2b. The date and time provided is invalid.

    * 2b1. Furiends shows an error message.

        Use case ends

**Use case: UC10 - Delete a feeding session**

**MSS**
1. User requests to delete a feeding session by specifying the person’s name, animal’s name, date and time of feeding together
2. Furiends checks whether the details provided are valid
3. Furiends deletes the feeding session with the provided details from the list
4. Furiends shows a confirmation message and removes the feeding session from the involved person and animal's record

    Use case ends

**Extensions**

* 2a. The feeding session with the provided details does not exist.

    * 2a1. Furiends shows an error message.

        Use case ends


### Non-Functional Requirements
1. The software should work on any mainstream OS as long as it has Java 17 or above installed.
2. The software should be able to hold up to 1000 entries without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using typed commands in a command-line interface (CLI) than using a graphical user interface (GUI)
4. The data written by the software (as a result of user input) should be stored locally and should be in a human editable text file.
5. The software should be for a single user, whereby it should only be running locally in the user’s computer and that the data file should not be able to be accessed by other users.
6. The software should work without requiring an installer.
7. The graphical user interface (GUI) should support standard screen resolutions 1920x1080 and higher and for screen scales 100% and 125%, and be usable for resolutions 1280x720 and higher and for screen scale 150%.
8. The software should be packaged into a single JAR file.
9. This product is not required to handle messaging/calling other users.
10. This product is not required to handle sending notifications to users about feeding times.

*{More to be added}*

<div style="page-break-before: always;"></div>

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Cats of NUS**: A society in NUS for cat lovers and care takers
* **PIC**: Person in charge of the members of the Cats of NUS
* **General User**: Users that fits the target audience of this address book

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder.

   2. Open terminal and run `cd path_to_folder` to change directory to the location of the jar file.
   3. Run `java -jar furiends.jar`. <br>
   Expected: Shows the GUI with sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a person or animal

1. Valid test case - Adding a person <br>
   Command: `add person n/John Doe p/98765432 e/JohnDoe@example.com t/friend` <br>
   Expected: Person `John Doe` is added to the list. Details of the added contact are shown in `John Doe`'s entry. The tag `friend` is shown in the entry as well. <br>
2. Valid test case - Adding an animal <br>
   Command: `add animal n/Whiskers d/Gray tabby cat l/NUS Library t/shy` <br>
   Expected: Animal `Whiskers` is added to the list. Details of the added contact are shown in `Whiskers`'s entry. The tag `shy` is shown in the entry as well. <br>
3. Invalid test case - Adding a person with missing details <br>
   Command: `add person n/John Doe p/98765432` <br>
   Expected: No person added. Error details shown in the status message: `Invalid command format! ...` <br>
4. Invalid test case - Adding an animal with missing details <br>
   Command: `add animal n/Whiskers l/NUS Library` <br>
   Expected: No animal added. Error details shown in the status message: `Invalid command format! ...` <br>
5. Invalid test case - Adding a duplicate person <br>
   Prerequisite: Person `Alex Yeoh` already exists in the list. (Should be present from pre-loaded data) <br>
   Command: `add person n/Alex Yeoh p/87438807 e/alexyeoh@example.com t/friend` <br>
   Expected: No person added. Error details shown in the status message: `This person already exists in the address book.` <br>
6. Invalid test case - Adding a duplicate animal <br>
   Prerequisite: Animal `Max` already exists in the list. (Should be present from pre-loaded data) <br>
   Command: `add animal n/Max d/Golden Retriever, friendly and well-trained l/Block A, Kennel 1 t/Shy` <br>
   Expected: No person added. Error details shown in the status message: `This animal already exists in the address book.` <br>

### Adding a feeding session

1. Valid test case - Adding a feeding session <br>
2. Prerequisite: Person `Alex Yeoh` and animal `Max` exist in the list (Should be present from pre-loaded data) <br>
   Command: `feed n/Max f/Alex Yeoh dt/2025-12-25 09:00` <br>
   Expected: Person `Alex Yeoh` is linked to animal `Max` with a feeding session on `2025-12-25 09:00`. Details of the feeding session are shown in both `Max` and `Alex Yeoh`'s entries. <br>
2. Invalid test case - Adding a feeding session with missing details <br>
   Command: `feed n/Max f/Alex Yeoh` <br>
   Expected: No feeding session added. Error details shown in the status message: `Invalid command format! ...` <br>

<div style="page-break-before: always;"></div>

### Editing a person or animal

1. Valid test case - Editing a person's details <br>
   Prerequisite: Person `Alex Yeoh` exists in the list (Should be present from pre-loaded data) <br>
   Command: `edit person Alex Yeoh n/John Doe` <br>
   Expected: Person `Alex Yeoh` is updated to `John Doe`. Details of the updated contact are shown in `John Doe`'s entry. <br>
   Extension: If a feeding session with `Alex Yeoh` exists (before `edit` was used), the feeding session is updated to reflect the new name. <br>
2. Valid test case - Editing an animal's details <br>
   Prerequisites: Animal `Max` exists in the list (Should be present from pre-loaded data) <br>
   Command: `edit animal Max d/Golden Retriever, extremely friendly and well-trained` <br>
   Expected: Animal `Max`'s description is updated. Details of the updated contact are shown in `Max`'s entry. <br>
3. Invalid test case - Editing a person that does not exist in the list <br>
   Prerequisite: Person `Jane Doe` does not exist in the list. <br>
   Command: `edit person Jane Doe n/Jane Smith` <br>
   Expected: No person updated. Error details shown in the status message: `The person's name provided is invalid` <br>
4. Invalid test case - Editing an animal that does not exist in the list <br>
   Prerequisite: Animal `Fluffy` does not exist in the list. <br>
   Command: `edit animal Fluffy d/White Persian cat` <br>
   Expected: No animal updated. Error details shown in the status message: `The animal's name provided is invalid` <br>
5. Invalid test case - Editing a person's name to another person's name who already exists in the address book <br>
   Prerequisite: Person `Bernice Yu` already exists in the list. (Should be present from pre-loaded data) <br>
   Command: `edit person John Doe n/Bernice Yu` <br>
   Expected: No person updated. Error details shown in the status message: `This person already exists in the address book.` <br>

### Finding a person or animal

<box type="info" seamless>

**Note:** After performing a `find` command, the filtered list of persons/animals is updated to show only the found entries. To show all entries again, use the `list` command.

</box>

1. Valid test case - Finding a person by full name <br>
    Prerequisite: Person `Bernice Yu` exists in the list. (Should be present from pre-loaded data) and there no other persons with names containing the substring "bernice yu" <br>
    Command: `find person n/Bernice Yu` <br>
    Expected: Only person `Bernice Yu` is shown in the list. <br>
2. Valid test case - Finding a person by substring of name <br>
    Prerequisite: Persons `Charlotte Oliveiro` and `David Li` exist in the list. (Should be present from pre-loaded data) and there are no other persons with names containing the substring "li" <br>
    Command: `find person n/li` <br>
    Expected: Persons `Charlotte Oliveiro` and `David Li` are shown in the list. <br>
3. Valid test case - Finding a person by tag <br>
    Prerequisite: Only persons `Alex Yeoh` and `Bernice Yu` exist in the list with tag `friend`. (Should be present from pre-loaded data) <br>
    Command: `find person t/friend` <br>
    Expected: Persons `Alex Yeoh` and `Bernice Yu` are shown in the list. <br>
4. Valid test case - Finding a person by name and tag <br>
    Prerequisite: Person `Bernice Yu` exists in the list with tag `friends`. (Should be present from pre-loaded data) <br>
    Command: `find person n/Bernice t/friends` <br>
    Expected: Out of all persons tagged as `friends`, only person `Bernice Yu` is shown in the list. <br>
5. Valid test case - Finding an animal by full name <br>
   Prerequisite: Animal `Max` exists in the list. (Should be present from pre-loaded data), and there are no other animals with names containing the substring "max" <br>
   Command: `find animal n/Max` <br>
   Expected: Only animal `Max` is shown in the list. <br>
6. Valid test case - Finding an animal by substring of name <br>
   Prerequisite: Animal `Max` exists in the list (Should be present from pre-loaded data), and there are no other animals with names containing the substring "ma" <br>
   Command: `find animal n/Ma` <br>
   Expected: Only animal `Max` is shown in the list <br>
7. Valid test case - Finding an animal by tag <br>
   Prerequisite: Only animal `Max` exist in the list with tag `Shy`. (Should be present from pre-loaded data) <br>
   Command: `find animal t/shy` <br>
   Expected: Only animal `Max` is shown in the list <br>
8. Valid test case - Finding an animal by name and tag <br>
   Prerequisite: Animal `Max` exists in the list with tag `Shy`. (Should be present from pre-loaded data) <br>
   Command: `find animal n/Max t/shy` <br>
   Expected: Out of all animals tagged as `shy`, only animal `Max` is shown in the list. <br>

<div style="page-break-before: always;"></div>

### Deleting a person or animal

1. Valid test case - Deleting a person <br>
   Prerequisite: Person `David Li` exists in the list. (Should be present from pre-loaded data) <br>
   Command: `delete person n/David Li` <br>
   Expected: Person `David Li` is removed from the list. <br>
   Extension: If a feeding session with `David Li` exists, the feeding session is also removed from the list. <br>
2. Valid test case - Deleting an animal <br>
   Prerequisite: Animal `Luna` exists in the list. (Should be present from pre-loaded data) <br>
   Command: `delete animal n/Luna` <br>
   Expected: Animal `Luna` is removed from the list. <br>
   Extension: If a feeding session with `Luna` exists, the feeding session is also removed from the list. <br>
3. Invalid test case - Deleting a person that does not exist in the list <br>
   Prerequisite: Person `Jane Doe` does not exist in the list. <br>
   Command: `delete person n/Jane Doe` <br>
   Expected: No person deleted. Error details shown in the status message: `The person's name provided is invalid` <br>
4. Invalid test case - Deleting an animal that does not exist in the list <br>
   Prerequisite: Animal `Fluffy` does not exist in the list. <br>
   Command: `delete animal n/Fluffy` <br>
   Expected: No animal deleted. Error details shown in the status message: `The animal's name provided is invalid` <br>

### Deleting a feeding session

1. Valid test case - Deleting a feeding session <br>
   Prerequisite: Feeding session with person `Alex Yeoh` and animal `Max` on `2025-12-25 09:00` exists in the list. <br>
   Command: `delete feed n/Max f/Alex Yeoh dt/2025-12-25 09:00` <br>
   Expected: Feeding session with person `Alex Yeoh` and animal `Max` on `2025-12-25 09:00` is removed from the list. <br>
2. Invalid test case - Deleting a feeding session that does not exist in the list <br>
   Prerequisite: Feeding session with person `Bernice Yu` and animal `Luna` on `2025-11-20 10:00` does not exist in the list. <br>
   Command: `delete feed n/Luna f/Bernice Yu dt/2025-11-20 10:00` <br>
   Expected: No feeding session deleted. Error details shown in the status message: `No feeding session found with the specified details` <br>

### Saving data

1. Dealing with corrupted data files

   1. To simulate a corrupted data file, remove the `id` field from one of the person entries in `addressbook.json` file located in the `data` folder where the `furiends.jar` file is located.
   2. The application will still launch, but no entries will be loaded.
   3. To solve this, delete the corrupted `addressbook.json` file and relaunch the application.
   4. A new `addressbook.json` file is created with sample data.<br>
<br>

2. Dealing with missing data files

   1. To simulate a missing data file, delete the `addressbook.json` file located in the `data` folder where the `furiends.jar` file is located.
   2. To solve this, relaunch the application.
   3. A new `addressbook.json` file is created with sample data.
