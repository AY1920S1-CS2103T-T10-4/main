package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.AppSettings;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.SimpleJsonUtil;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;
import seedu.address.model.display.detailwindow.WeekSchedule;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.model.display.sidepanel.PersonDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupDescriptor;
import seedu.address.model.group.GroupId;
import seedu.address.model.group.GroupList;
import seedu.address.model.group.GroupName;
import seedu.address.model.mapping.PersonToGroupMapping;
import seedu.address.model.mapping.PersonToGroupMappingList;
import seedu.address.model.module.AcadYear;
import seedu.address.model.module.DetailedModuleList;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.SemesterNo;
import seedu.address.model.module.exceptions.ModuleNotFoundException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonDescriptor;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.PersonList;
import seedu.address.model.person.schedule.Event;
import seedu.address.websocket.NusModsApi;
import seedu.address.websocket.NusModsParser;


/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    //To Do.
    //private final FilteredList<Group> groupFilteredList;

    private TimeBook timeBook = null;

    private PersonList personList;
    private GroupList groupList;
    private PersonToGroupMappingList personToGroupMappingList;

    private NusModsData nusModsData;

    // UI display
    private DetailWindowDisplay detailWindowDisplay;
    private SidePanelDisplay sidePanelDisplay;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook,
                        PersonList personList,
                        GroupList groupList,
                        PersonToGroupMappingList personToGroupMappingList,
                        ReadOnlyUserPrefs userPrefs) {
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

    public ModelManager(ReadOnlyAddressBook addressBook, TimeBook timeBook,
                        NusModsData nusModsData, ReadOnlyUserPrefs userPrefs) {
        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());

        this.timeBook = timeBook;
        this.personList = timeBook.getPersonList();
        this.groupList = timeBook.getGroupList();
        this.personToGroupMappingList = timeBook.getPersonToGroupMappingList();

        this.nusModsData = nusModsData;

        int personCounter = -1;
        for (int i = 0; i < personList.getPersons().size(); i++) {
            if (personList.getPersons().get(i).getPersonId().getIdentifier() > personCounter) {
                personCounter = personList.getPersons().get(i).getPersonId().getIdentifier();
            }
        }

        int groupCounter = -1;
        for (int i = 0; i < groupList.getGroups().size(); i++) {
            if (groupList.getGroups().get(i).getGroupId().getIdentifier() > groupCounter) {
                groupCounter = groupList.getGroups().get(i).getGroupId().getIdentifier();
            }
        }

        // sets the appropriate counter for person and group constructor
        Person.setCounter(personCounter + 1);
        Group.setCounter(groupCounter + 1);

        this.userPrefs = new UserPrefs(userPrefs);
    }

    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        this(addressBook, new PersonList(), new GroupList(), new PersonToGroupMappingList(), userPrefs);
    }

    public ModelManager(PersonList personList, GroupList groupList, PersonToGroupMappingList personToGroupMappingList) {
        this(new AddressBook(), personList, groupList, personToGroupMappingList, new UserPrefs());
        //Edit addressbook with the details here?
        this.addressBook.setPersons(personList.getPersons());
    }

    public ModelManager(TimeBook timeBook) {
        this(new AddressBook(), timeBook, new NusModsData(), new UserPrefs());
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
    public AppSettings getAppSettings() {
        return userPrefs.getAppSettings();
    }

    @Override
    public void setAppSettings(AppSettings appSettings) {
        requireNonNull(appSettings);
        userPrefs.setAppSettings(appSettings);
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

    @Override
    public AcadYear getDefaultAcadYear() {
        return userPrefs.getAcadYear();
    }

    @Override
    public SemesterNo getDefaultSemesterNo() {
        return userPrefs.getSemesterNo();
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
        this.addressBook.addPerson(isAdded);
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
    public ObservableList<Group> getObservableGroupList() {
        return addressBook.getGroupList();
    }

    @Override
    public Group addGroup(GroupDescriptor groupDescriptor) {
        Group isAdded = this.groupList.addGroup(groupDescriptor);
        if (isAdded != null) {
            this.addressBook.addGroup(isAdded);
        }
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

    //=========== UI Model =============================================================

    @Override
    public DetailWindowDisplay getDetailWindowDisplay() {
        return detailWindowDisplay;
    }

    @Override
    public SidePanelDisplay getSidePanelDisplay() {
        return sidePanelDisplay;
    }

    @Override
    public void updateDetailWindowDisplay(DetailWindowDisplay detailWindowDisplay) {
        this.detailWindowDisplay = detailWindowDisplay;
    }

    @Override
    public void updateDetailWindowDisplay(Name name, LocalDateTime time, DetailWindowDisplayType type) {
        ArrayList<WeekSchedule> weekSchedules = new ArrayList<>();
        WeekSchedule weekSchedule = new WeekSchedule(name.toString(), time, findPerson(name));
        weekSchedules.add(weekSchedule);
        DetailWindowDisplay detailWindowDisplay = new DetailWindowDisplay(weekSchedules, type);
        updateDetailWindowDisplay(detailWindowDisplay);
    }

    @Override
    public void updateDetailWindowDisplay(GroupName groupName, LocalDateTime time, DetailWindowDisplayType type) {
        Group group = groupList.findGroup(groupName);
        ArrayList<PersonId> personIds = findPersonsOfGroup(group.getGroupId());
        ArrayList<WeekSchedule> weekSchedules = new ArrayList<>();
        for (int i = 0; i < personIds.size(); i++) {
            Person person = findPerson(personIds.get(i));
            WeekSchedule weekSchedule = new WeekSchedule(groupName.toString(), time, person);
        }
        DetailWindowDisplay detailWindowDisplay = new DetailWindowDisplay(weekSchedules, type);
        updateDetailWindowDisplay(detailWindowDisplay);
    }

    @Override
    public void updateSidePanelDisplay(SidePanelDisplay sidePanelDisplay) {
        this.sidePanelDisplay = sidePanelDisplay;
    }

    @Override
    public void updateSidePanelDisplay(SidePanelDisplayType type) {
        SidePanelDisplay sidePanelDisplay;
        ArrayList<PersonDisplay> displayPersons = new ArrayList<>();
        ArrayList<GroupDisplay> displayGroups = new ArrayList<>();
        ArrayList<Person> persons = timeBook.getPersonList().getPersons();
        ArrayList<Group> groups = timeBook.getGroupList().getGroups();
        for (int i = 0; i < persons.size(); i++) {
            displayPersons.add(new PersonDisplay(persons.get(i)));
        }
        for (int i = 0; i < groups.size(); i++) {
            displayGroups.add(new GroupDisplay(groups.get(i)));
        }
        sidePanelDisplay = new SidePanelDisplay(displayPersons, displayGroups, type);
        updateSidePanelDisplay(sidePanelDisplay);
    }

    //=========== Suggesters =============================================================

    @Override
    public ArrayList<String> personSuggester(String prefix) {
        ArrayList<String> suggestions = new ArrayList<>();
        ArrayList<Person> persons = timeBook.getPersonList().getPersons();

        for (int i = 0; i < persons.size(); i++) {
            String name = persons.get(i).getName().toString();
            if (name.startsWith(prefix)) {
                suggestions.add(name);
            }
        }
        return suggestions;
    }

    @Override
    public ArrayList<String> personSuggester(String prefix, String groupName) {
        ArrayList<String> suggestions = new ArrayList<>();

        Group group = findGroup(new GroupName(groupName));
        if (group == null) {
            return suggestions;
        }

        ArrayList<PersonId> personIds = findPersonsOfGroup(group.getGroupId());
        for (int i = 0; i < personIds.size(); i++) {
            String name = findPerson(personIds.get(i)).getName().toString();
            if (name.startsWith(prefix)) {
                suggestions.add(name);
            }
        }
        return suggestions;
    }

    @Override
    public ArrayList<String> groupSuggester(String prefix) {
        ArrayList<String> suggestions = new ArrayList<>();
        ArrayList<Group> groups = timeBook.getGroupList().getGroups();

        for (int i = 0; i < groups.size(); i++) {
            String name = groups.get(i).getGroupName().toString();
            if (name.startsWith(prefix)) {
                suggestions.add(name);
            }
        }
        return suggestions;
    }

    //=========== NusModsData ================================================================================

    @Override
    public NusModsData getNusModsData() {
        return nusModsData;
    };

    @Override
    public Module findModuleFromAllSources(AcadYear acadYear, ModuleCode moduleCode) throws ModuleNotFoundException {
        Module module;

        try {
            module = nusModsData.getDetailedModuleList().findModule(acadYear, moduleCode);
        } catch (ModuleNotFoundException ex1) {
            try {
                //TODO: just remove this layer altogether, module list should be small enough to keep in-memory
                Path path = this.userPrefs.getDetailedModuleListFilePath();
                String key = acadYear.toString() + " " + moduleCode.toString();
                Optional<JSONObject> objOptional = SimpleJsonUtil.readJsonFile(path, JSONObject.class);
                if (objOptional.isEmpty()) {
                    throw new ModuleNotFoundException();
                }
                module = NusModsParser.parseModule(objOptional.get());
            } catch (ModuleNotFoundException ex2) {
                Optional<JSONObject> moduleObj = new NusModsApi(acadYear).getModule(moduleCode);
                if (moduleObj.isEmpty()) {
                    throw new ModuleNotFoundException();
                }
                module = NusModsParser.parseModule(moduleObj.get());
                nusModsData.addDetailedModule(module);
            }
        }

        return module;
    };

    @Override
    public DetailedModuleList getDetailedModuleList() {
        return nusModsData.getDetailedModuleList();
    }

    @Override
    public void addDetailedModule(Module module) {
        nusModsData.addDetailedModule(module);
    }

    public String getAcadSemStartDateString(AcadYear acadYear, SemesterNo semesterNo) {
        return nusModsData.getAcadCalendar().getStartDateString(acadYear, semesterNo);
    };

    public List<String> getHolidayDateStrings() {
        return nusModsData.getHolidays().getHolidayDates();
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

    @Override
    public TimeBook getTimeBook() {
        return this.timeBook;
    }

}
