package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
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
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    private PersonList personList;
    private GroupList groupList;
    private PersonToGroupMappingList personToGroupMappingList;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, PersonList personList, GroupList groupList,
                        PersonToGroupMappingList personToGroupMappingList, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());

        this.personList = personList;
        this.groupList = groupList;
        this.personToGroupMappingList = personToGroupMappingList;
    }

    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        this(addressBook, new PersonList(), new GroupList(), new PersonToGroupMappingList(), userPrefs);
    }

    public ModelManager() {
        this(new AddressBook(), new PersonList(), new GroupList(), new PersonToGroupMappingList(), new UserPrefs());
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }


    //=========== Person Accessors =============================================================

    @Override
    public PersonList getPersonList() {
        return personList;
    }

    @Override
    public Person addPerson(PersonDescriptor personDescriptor) {
        Person isAdded = this.personList.addPerson(personDescriptor);
        return isAdded;
    }

    @Override
    public Person findPerson(Name name) {
        Person person = personList.findPerson(name);
        if (person != null) {
            return person;
        } else {
            return null;
        }
    }

    @Override
    public Person findPerson(PersonId personId) {
        Person person = personList.findPerson(personId);
        if (person != null) {
            return person;
        } else {
            return null;
        }
    }

    @Override
    public boolean addEvent(Name name, Event event) {
        Person p = personList.findPerson(name);
        if (p != null) {
            p.addEvent(event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Person editPerson(Name name, PersonDescriptor personDescriptor) {
        return personList.editPerson(name, personDescriptor);
    }

    @Override
    public boolean deletePerson(PersonId personId) {
        deletePersonFromMapping(personId);
        return personList.deletePerson(personId);
    }

    @Override
    public ArrayList<GroupId> findGroupsOfPerson(PersonId personId) {
        return personToGroupMappingList.findGroupsOfPerson(personId);
    }

    //=========== Group Accessors =============================================================

    @Override
    public GroupList getGroupList() {
        return groupList;
    }

    @Override
    public Group addGroup(GroupDescriptor groupDescriptor) {
        Group isAdded = this.groupList.addGroup(groupDescriptor);
        return isAdded;
    }

    @Override
    public Group editGroup(GroupName groupName, GroupDescriptor groupDescriptor) {
        return groupList.editGroup(groupName, groupDescriptor);
    }

    @Override
    public Group findGroup(GroupName groupName) {
        Group group = groupList.findGroup(groupName);
        if (group != null) {
            return group;
        } else {
            return null;
        }
    }

    @Override
    public Group findGroup(GroupId groupId) {
        Group group = groupList.findGroup(groupId);
        if (group != null) {
            return group;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteGroup(GroupId groupId) {
        deleteGroupFromMapping(groupId);
        return groupList.deleteGroup(groupId);
    }

    @Override
    public ArrayList<PersonId> findPersonsOfGroup(GroupId groupId) {
        return personToGroupMappingList.findPersonsOfGroup(groupId);
    }

    //=========== Mapping Accessors =============================================================

    @Override
    public PersonToGroupMappingList getPersonToGroupMappingList() {
        return personToGroupMappingList;
    }

    @Override
    public boolean addPersonToGroupMapping(PersonToGroupMapping mapping) {
        return personToGroupMappingList.addPersonToGroupMapping(mapping);
    }

    @Override
    public PersonToGroupMapping findPersonToGroupMapping(PersonId personId, GroupId groupId) {
        return personToGroupMappingList.findPersonToGroupMapping(personId, groupId);
    }

    @Override
    public boolean deletePersonToGroupMapping(PersonToGroupMapping mapping) {
        return personToGroupMappingList.deletePersonToGroupMapping(mapping);
    }

    @Override
    public void deletePersonFromMapping(PersonId personId) {
        personToGroupMappingList.deletePersonFromMapping(personId);
    }

    @Override
    public void deleteGroupFromMapping(GroupId groupId) {
        personToGroupMappingList.deleteGroupFromMapping(groupId);
    }


    //=========== Others =============================================================

    @Override
    public String list() {
        String output = "";
        output += "PERSONS:\n";
        output += personList.toString();

        output += "--------------------------------------------\n";
        output += "GROUPS:\n";
        output += groupList.toString();

        output += "--------------------------------------------\n";
        output += "MAPPINGS: \n";
        output += personToGroupMappingList.toString();

        return output;
    }
}
