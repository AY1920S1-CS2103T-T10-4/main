package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.TimeBook;
import seedu.address.testutil.TypicalTimeBook;


public class JsonSerializableTimeBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableTimeBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalTimeBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidTimeBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonTimeBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableTimeBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableTimeBook.class).get();
        TimeBook timeBookFromFile = dataFromFile.toModelType();
        TimeBook typicalTimeBook = TypicalTimeBook.get();
        assertTrue(timeBookFromFile.equals(typicalTimeBook));
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTimeBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableTimeBook.class).get();
        assertThrows(NullPointerException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableTimeBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableTimeBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableTimeBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }
}
