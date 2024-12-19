
package org.navitrace.reports.common;

import org.navitrace.storage.StorageException;

import java.io.IOException;
import java.io.OutputStream;

public interface ReportExecutor {
    void execute(OutputStream stream) throws StorageException, IOException;
}
