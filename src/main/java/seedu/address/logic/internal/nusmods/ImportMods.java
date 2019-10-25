package seedu.address.logic.internal.nusmods;

import java.util.Optional;
import java.util.logging.Logger;

import org.json.simple.JSONArray;

import seedu.address.commons.core.AppSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.module.AcadYear;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleList;
import seedu.address.model.module.ModuleSummary;
import seedu.address.model.module.ModuleSummaryList;
import seedu.address.model.module.exceptions.ModuleNotFoundException;
import seedu.address.websocket.Cache;
import seedu.address.websocket.NusModsApi;
import seedu.address.websocket.NusModsParser;

/**
 * Internal class to be executed as a standalone program to import all NUSMods detailed module data.
 */
public class ImportMods {
    private static final Logger logger = LogsCenter.getLogger(Cache.class);

    /**
     * Main driver.
     */
    public static void main(String[] args) {
        importMods(AppSettings.DEFAULT_ACAD_YEAR);
    }

    /**
     * Imports detailed data of all nus modules for the given academic year.
     * Incrementally caches each module into the detailed modules file.
     * To re-import all modules, delete the existing detailed modules file before executing this method.
     */
    public static void importMods(AcadYear year) {
        NusModsApi api = new NusModsApi(year);
        ModuleSummaryList moduleSummaries;

        // try to get module summaries from api, then local file
        Optional<JSONArray> moduleSummaryJsonOptional = api.getModuleList();
        if (moduleSummaryJsonOptional.isPresent()) {
            moduleSummaries = NusModsParser.parseModuleSummaryList(
                    moduleSummaryJsonOptional.get(), year);
        } else {
            Optional<ModuleSummaryList> moduleSummaryListOptional = Cache.loadModuleSummaryList();
            if (!moduleSummaryListOptional.isPresent()) {
                logger.severe("No module summaries, can't scrape all detailed modules.");
                return;
            }
            moduleSummaries = moduleSummaryListOptional.get();
        }

        int total = moduleSummaries.getModuleSummaries().size();
        int foundFromFile = 0;
        int foundFromApi = 0;
        int failed = 0;
        int curr = 0;

        Optional<ModuleList> moduleListOptional = Cache.loadModuleList();
        ModuleList moduleList = moduleListOptional.get();

        // Cache module if missing from local file
        for (ModuleSummary modSummary : moduleSummaries.getModuleSummaries()) {
            curr += 1;
            try {
                moduleList.findModule(modSummary.getModuleId());
                foundFromFile += 1;
                logger.info("[" + curr + "/" + total + "] Found in file: " + modSummary);
            } catch (ModuleNotFoundException e) {
                Optional<Module> moduleOptional = Cache.loadModule(modSummary.getModuleId());
                if (!moduleOptional.isPresent()) {
                    failed += 1;
                    logger.severe("[" + curr + "/" + total + "] Hmm could not get detailed data for this module: "
                            + modSummary);
                    break;
                } else {
                    foundFromApi += 1;
                    logger.info("[" + curr + "/" + total + "] Found from API: " + modSummary);
                }
            }

        }
        logger.info("Modules foundFromFile/foundFromApi/failed/total: [" + foundFromFile + "/"
                + foundFromApi + "/" + failed + "/" + total + "]");
    }
}
