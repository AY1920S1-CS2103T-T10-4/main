package seedu.address.logic.export;

import java.io.IOException;

/**
 * Interface to export schedules.
 */
public interface Exporter {
    public void export() throws IOException;
}
