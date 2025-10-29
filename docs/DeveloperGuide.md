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

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

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

<puml src="diagrams/ModelOverallClassDiagram.puml" width="500" />

The `Model` component manages three main entity types: Person, Animal, and FeedingSession.

* `AddressBook` contains three unique lists: `UniquePersonList`, `UniqueAnimalList`, and `UniqueFeedingSessionList`
* `ModelManager` maintains the `AddressBook` and manages undo/redo functionality with `undoStack` and `redoStack`

#### Person Model

<puml src="diagrams/ModelPersonClassDiagram.puml" />

The Person model stores:
* `UUID id` - Unique identifier for each person
* `PersonName name` - The person's name
* `Phone phone` - Contact phone number
* `Email email` - Email address
* `Set<Tag> tags` - Tags for categorization
* `Set<UUID> feedingSessionIds` - References to feeding sessions by UUID

#### Animal Model

<puml src="diagrams/ModelAnimalClassDiagram.puml" />

The Animal model stores:
* `UUID id` - Unique identifier for each animal
* `AnimalName name` - The animal's name
* `Description description` - Details about the animal
* `Location location` - Where the animal is located
* `Set<Tag> tags` - Tags for categorization
* `Set<UUID> feedingSessionIds` - References to feeding sessions by UUID

#### FeedingSession Model

<puml src="diagrams/ModelFeedingSessionClassDiagram.puml" />

The FeedingSession model links Person and Animal entities:
* `UUID id` - Unique identifier for each feeding session
* `UUID animalId` - Reference to the Animal being fed
* `UUID personId` - Reference to the Person feeding
* `LocalDateTime dateTime` - When the feeding occurred
* `String notes` - Optional notes about the feeding

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

<puml src="diagrams/ExecuteCommandActivityDiagram.puml" alt="ExecuteCommandActivityDiagram" width="400" />

2. **Undo Operation**: When the user executes `undo`, the system:
   * Pushes the current state to `redoStack`
   * Pops the previous state from `undoStack`
   * Restores the address book to that previous state

<puml src="diagrams/UndoActivityDiagram.puml" alt="UndoActivityDiagram" width="400" />

3. **Redo Operation**: When the user executes `redo`, the system:
   * Pushes the current state to `undoStack`
   * Pops the next state from `redoStack`
   * Restores the address book to that next state

<puml src="diagrams/RedoActivityDiagram.puml" alt="RedoActivityDiagram" width="400" />

<br>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

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

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="700" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

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
* is a member of Cats of NUS
* has a need to manage a significant number of contacts
* will like to manage a significant number of contacts of other cat caretakers
* will like to keep track of the cats present around campus
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps


**Value proposition**:
* manage contacts faster than a typical mouse/GUI driven app
* keep track of cats' details around campus



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                              | I want to …​                                                                                | So that I can…​                                                                |
|----------|------------------------------------------------------|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `* * *`  | new user                                             | view a guide to using this application                                                      | refer to the guide when I forget how to use the application                    |
| `* * *`  | general user                                         | view the list of added contact entries                                                      | keep track of all contact entries I added                                      |
| `* * *`  | general user                                         | add a contact entry                                                                         | keep track of their information for later use                                  |
| `* * *`  | general user                                         | delete a contact entry                                                                      | remove irrelevant contacts that I do not need to keep track of anymore         |
| `* * *`  | general user                                         | edit a contact entry                                                                        | ensure that my contact information is up to date                               |
| `* * *`  | general animal lover in NUS                          | keep track of the animals I encountered in NUS                                              | visit them whenever I am free                                                  |
| `* * *`  | general animal feeder in NUS                         | add contact entry of an existing feeder of NUS                                              | keep track of their information for later use                                  |
| `* * *`  | general animal feeder in NUS                         | delete contact entry of an existing feeder of NUS                                           | remove irrelevant contacts that I do not need to keep track of anymore         |
| `* * *`  | general animal feeder in NUS                         | update contact entry of an existing feeder of NUS                                           | keep their information updated so as to stay in contact with them              |
| `* * *`  | member of Cats of NUS                                | view the list of added contact entries of Cats of NUS members                               | keep track of all contact entries of added members                             |
| `* * *`  | member of Cats of NUS                                | view the list of added contact entries of cats                                              | keep track of all contract entries of added cats                               |
| `* * *`  | member of Cats of NUS                                | add a contact entry of a Cats of NUS member                                                 | keep track of his/her information for later use                                |
| `* * *`  | member of Cats of NUS                                | add a contact entry of a cat                                                                | keep track of its information for later use                                    |
| `* * *`  | member of Cats of NUS                                | delete a contact entry of a Cats of NUS member                                              | remove irrelevant entries that I do not need to keep track of anymore          |
| `* * *`  | member of Cats of NUS                                | delete a contact entry of a cat                                                             | remove irrelevant entries that I do not need to keep track of anymore          |
| `* * *`  | member of Cats of NUS                                | edit a contact entry of a Cats of NUS member                                                | update his/her information when necessary                                      |
| `* * *`  | member of Cats of NUS                                | edit a contact entry of a cat                                                               | update its information when necessary                                          |
| `* *`    | new user                                             | search the contact list of caretakers of animals in NUS                                     | get acquainted with these caretakers                                           |
| `* *`    | new user                                             | search the list of animals found in NUS                                                     | get to understand the diversity of animals found around the NUS Campus         |
| `* *`    | general user                                         | find a contact entry by name                                                                | get information of contact entries easily                                      |
| `* *`    | general animal lover in NUS                          | filter out various organisations of people                                                  | learn about various animal care groups in NUS                                  |
| `* *`    | general animal lover in NUS                          | favourite certain animals                                                                   | visit them more often                                                          |
| `* *`    | general animal lover in NUS                          | store photographs of animals I have taken in NUS                                            | consolidate these photographs of animals on campus, and view them in one place |
| `* *`    | member of Cats of NUS                                | find the contact entry of a Cats of NUS member by name                                      | locate details of members without having to go through the entire list         |
| `* *`    | member of Cats of NUS                                | find the contact entry of a cat by name                                                     | locate details of cats without having to go through the entire list            |
| `* *`    | member of Cats of NUS                                | add a quick note ("looked sick", "shy today")                                               | remind myself to inform the people in charge                                   |
| `* *`    | member of Cats of NUS                                | obtain the photo of each of the cats on campus                                              | recognise them when feeding them                                               |
| `* *`    | Person In Charge (PIC) of the members of Cats of NUS | assign roles (member, shift lead, logistics, moderator)                                     | know who is in my team and what they are in charge of                          |
| `* *`    | organised user                                       | categorise contacts (Cats of NUS members, existing feeder of NUS, animals encountered etc)  | differentiate contact entries easily                                           |
| `*`      | PIC of the members of Cats of NUS                    | add fixed feeding stations and assign cats                                                  | keep track of feeding locations and cats assigned to each locations            |
| `*`      | PIC of the members of Cats of NUS                    | edit fixed feeding stations and its assigned cats                                           | update feeding locations and cats assigned to each location                    |
| `*`      | member of Cats of NUS                                | view a list/map of fixed feeding stations and the cats assigned to each                     | go straight to the right spot to feed the cats                                 |
| `*`      | member of Cats of NUS                                | set special reminders to myself (special diet for the station I am feeding for on that day) | be reminded of what I have to do, that is out of my routine                    |
| `*`      | member of Cats of NUS                                | edit special reminders I set before                                                         | change timing/date/frequency of each reminder                                  |
| `*`      | member of Cats of NUS                                | be reminded of when to feed the cats                                                        | feed the cats on time                                                          |



### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, where `user` refers to a cat caretaker using the Furiends app to manage contacts, unless specified otherwise)

**Use case: UC01 - Add a person**

**MSS**

1. User requests to add a new person by providing the person’s name, phone number, and email together
2. AddressBook validates the details
3. AddressBook adds the person to the list
4. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. One or more details are missing or invalid.

    * 2a1. AddressBook shows an error message.

        Use case ends
* 3a. A person with the same name already exists in the list.

    * 3a1. AddressBook shows a duplicate warning.

        Use case ends


**Use case: UC02 - Delete a person**

**MSS**

1. User requests to delete a person by specifying the person’s name
2. AddressBook checks if the person exists
3. AddressBook deletes the person
4. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any person.

    * 2b1. AddressBook shows an error message.

        Use case ends


**Use case: UC03 - Edit a person**

**MSS**

1. User requests to edit a person by providing the person’s name together with the updated details (phone number, email, animal being fed, and/or date and time of feeding)
2. AddressBook checks if the person exists
3. AddressBook validates the new details
4. AddressBook updates the person’s information
5. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any person.

    * 2b1. AddressBook shows an error message.

        Use case ends
* 3a. One or more new details are invalid.

    * 3a1. AddressBook shows an error message.

        Use case ends
* 4a. The updated details duplicate another person’s name.

    * 4a1. AddressBook shows a duplicate warning.

        Use case ends


**Use case: UC04 - Find a person**

**MSS**

1. User requests to find a person by specifying the person’s name or substring of their name
2. AddressBook searches the contact list for a matching person
3. AddressBook shows the person’s details

    Use case ends

**Extensions**

* 2a. No person matches the given name.

    * 2a1. AddressBook shows an empty result message.

        Use case ends


**Use case: UC05 - Add an animal**

**MSS**

1. User requests to add a new animal by providing the animal’s name, description, and location together
2. AddressBook validates the details
3. AddressBook adds the animal to the list
4. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. One or more details are missing or invalid.

    * 2a1. AddressBook shows an error message.

        Use case ends
* 3a. An animal with the same name already exists in the list.

    * 3a1. AddressBook shows a duplicate warning.

        Use case ends


**Use case: UC06 - Delete an animal**

**MSS**

1. User requests to delete an animal by specifying the animal’s name
2. AddressBook checks if the animal exists
3. AddressBook deletes the animal
4. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any animal.

    * 2b1. AddressBook shows an error message.

        Use case ends


**Use case: UC07 - Edit an animal**

**MSS**

1. User requests to edit an animal by providing the animal’s name together with the updated description and/or location
2. AddressBook checks if the animal exists
3. AddressBook validates the new details
4. AddressBook updates the animal’s information
5. AddressBook shows a confirmation message and the updated list

    Use case ends

**Extensions**

* 2a. The list is empty.

    Use case ends
* 2b. The given name does not match any animal.

    * 2b1. AddressBook shows an error message.

        Use case ends
* 3a. One or more new details are invalid.

    * 3a1. AddressBook shows an error message.

        Use case ends
* 4a. The updated details duplicate another animal’s name.

    * 4a1. AddressBook shows a duplicate warning.

        Use case ends


**Use case: UC08 - Find an animal**

**MSS**

1. User requests to find an animal by specifying the animal’s name or substring of their name
2. AddressBook searches the animal list for a matching entry
3. AddressBook shows the animal’s details

    Use case ends

**Extensions**

* 2a. No animal matches the given name.

    * 2a1. AddressBook shows an empty result message.

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
9. This product is not required to handle messaging/calling other Cats of NUS members.
10. This product is not required to handle sending notifications to users about feeding times.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Cats of NUS**: A society in NUS for cats lover and care takers
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

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete person n/Alex Yeoh`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated

   2. Test case: `delete hi` <br>
      Expected: No person deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete people n/NAME`, `delete ani n/NAME`, `delete WORD` (where `WORD` is any word that is not `person` or `animal`)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
