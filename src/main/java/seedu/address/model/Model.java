package seedu.address.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupDescriptor;
import seedu.address.model.group.GroupId;
import seedu.address.model.group.GroupList;
import seedu.address.model.group.GroupName;
import seedu.address.model.mapping.PersonToGroupMapping;
import seedu.address.model.mapping.PersonToGroupMappingList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonDescriptor;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.PersonList;
import seedu.address.model.person.schedule.Event;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    //=========== UserPrefs ==================================================================================

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    //=========== AddressBook ================================================================================

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the filtered person list
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    //=========== Person Accessors =============================================================

    /**
     * Returns the list of Persons.
     */
    PersonList getPersonList();

    /**
     * Adds a person with personDescriptor to the list of Persons.
     */
    Person addPerson(PersonDescriptor personDescriptor);

    /**
     * Finds a person with a given Name.
     */
    Person findPerson(Name name);

    /**
     * Finds a person with a given PersonId.
     */
    Person findPerson(PersonId personId);

    /**
     * Adds an Event to the schedule of a Person with the given Name.
     */
    boolean addEvent(Name name, Event event);

    /**
     * Edits the person with given Name with given PersonDescriptor.
     */
    Person editPerson(Name name, PersonDescriptor personDescriptor);

    /**
     * Deletes a person with given PersonId.
     */
    boolean deletePerson(PersonId personId);

    /**
     * Returns the list of GroupIds which person with PersonId is in.
     */
    ArrayList<GroupId> findGroupsOfPerson(PersonId personId);

    //=========== Group Accessors =============================================================

    /**
     * Returns the list of Groups.
     */
    GroupList getGroupList();

    /**
     * Adds a Group with groupDescriptor into the list of Groups.
     */
    Group addGroup(GroupDescriptor groupDescriptor);

    /**
     * Edits the person with given Name with given PersonDescriptor.
     */
    Group editGroup(GroupName groupName, GroupDescriptor groupDescriptor);

    /**
     * Finds a Group with given GroupName.
     */
    Group findGroup(GroupName groupName);

    /**
     * Finds a Group with given GroupId.
     */
    Group findGroup(GroupId groupId);

    /**
     * Deletes a Group with given GroupId.
     */
    boolean deleteGroup(GroupId groupId);

    /**
     * Returns a list of PersonId that is in a Group with given GroupId.
     */
    ArrayList<PersonId> findPersonsOfGroup(GroupId groupId);

    //=========== Mapping Accessors =============================================================

    /**
     * Returns the list of PersonToGroupMappings.
     */
    PersonToGroupMappingList getPersonToGroupMappingList();

    /**
     * Adds a person to group mapping to the list of mappings.
     */
    boolean addPersonToGroupMapping(PersonToGroupMapping mapping);

    /**
     * Returns a mapping with given PersonId and GroupId.
     */
    PersonToGroupMapping findPersonToGroupMapping(PersonId personId, GroupId groupId);

    /**
     * Deletes a mapping with given PersonId and GroupId.
     */
    boolean deletePersonToGroupMapping(PersonToGroupMapping mapping);

    /**
     * Deletes all mappings with PersonId.
     */
    void deletePersonFromMapping(PersonId personId);

    /**
     * Deletes all mappings with GroupId.
     */
    void deleteGroupFromMapping(GroupId groupId);

    //=========== Others =============================================================

    /**
     * Returns a summary of all Persons, Groups, and Mappings.
     */
    String list();


    TimeBook getTimeBook();


}
