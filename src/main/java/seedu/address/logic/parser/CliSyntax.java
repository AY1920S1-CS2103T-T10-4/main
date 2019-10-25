package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("em/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("tag/");
    public static final Prefix PREFIX_REMARK = new Prefix("r/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_LINK = new Prefix("l/");
    public static final Prefix PREFIX_SEMESTER = new Prefix("s/");
    public static final Prefix PREFIX_ACAD_YEAR = new Prefix("ay/");
    public static final Prefix PREFIX_MODULE_CODE = new Prefix("m/");
    public static final Prefix PREFIX_LESSON_NOS = new Prefix("class/");
    public static final Prefix PREFIX_EVENTNAME = new Prefix("e/");
    public static final Prefix PREFIX_GROUPNAME = new Prefix("g/");
    public static final Prefix PREFIX_TIMING = new Prefix("t/");
    public static final Prefix PREFIX_EDIT = new Prefix("ed/");
    public static final Prefix PREFIX_LOCATIONS = new Prefix("l/");
}
