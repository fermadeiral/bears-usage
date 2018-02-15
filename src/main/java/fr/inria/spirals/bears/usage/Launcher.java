package fr.inria.spirals.bears.usage;

import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.DateStringParser;
import com.martiansoftware.jsap.stringparsers.EnumeratedStringParser;
import com.martiansoftware.jsap.stringparsers.FileStringParser;
import fr.inria.spirals.bears.usage.filter.*;
import fr.inria.spirals.bears.usage.model.Benchmark;
import fr.inria.spirals.bears.usage.model.Bug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by fermadeiral on 02/05/17.
 */
public class Launcher {

    private static Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    private JSAP jsap;
    private JSAPResult arguments;
    private OutputMode outputMode;
    private String outputPath;
    private String jsonFileFolderPath = "./bug-metadata";

    // Criteria
    private String projectNameCriterion;
    private String exceptionTypeCriterion;
    private Date initialDateCriterion;
    private Date finalDateCriterion;
    private int maxEstimatedBuildAndTestDurationCriterion;
    private int maxNumberOfFailingTestsCriterion;

    public Launcher(String[] args) throws JSAPException {
        this.defineArgs();
        this.arguments = jsap.parse(args);
        this.checkAndInitArgs();
    }

    private void defineArgs() throws JSAPException {
        this.jsap = new JSAP();

        Switch sw = new Switch("help");
        sw.setShortFlag('h');
        sw.setLongFlag("help");
        sw.setDefault("false");
        this.jsap.registerParameter(sw);

        sw = new Switch("debug");
        sw.setShortFlag('d');
        sw.setLongFlag("debug");
        sw.setDefault("false");
        this.jsap.registerParameter(sw);

        FlaggedOption opt = new FlaggedOption("outputPath");
        opt.setLongFlag("outputPath");
        opt.setDefault("./output");
        opt.setStringParser(FileStringParser.getParser().setMustBeDirectory(true).setMustExist(true));
        opt.setHelp("Specify the path to output the results.");
        this.jsap.registerParameter(opt);

        String outputModeValues = "";
        for (OutputMode outputMode : OutputMode.values()) {
            outputModeValues += outputMode.name() + ";";
        }
        outputModeValues = outputModeValues.substring(0, outputModeValues.length() - 1);
        opt = new FlaggedOption("outputMode");
        opt.setShortFlag('o');
        opt.setLongFlag("outputMode");
        opt.setRequired(true);
        opt.setStringParser(EnumeratedStringParser.getParser(outputModeValues));
        opt.setUsageName(outputModeValues.replace(";", "|"));
        opt.setHelp("Specify if the output is metrics (METRICS) or branch names (BRANCHES).");
        this.jsap.registerParameter(opt);

        // Search criteria
        opt = new FlaggedOption("projectNameCriterion");
        opt.setLongFlag("projectNameCriterion");
        opt.setStringParser(JSAP.STRING_PARSER);
        opt.setHelp("Search criterion: specify the name of the project (e.g. INRIA/spoon).");
        this.jsap.registerParameter(opt);

        opt = new FlaggedOption("exceptionTypeCriterion");
        opt.setLongFlag("exceptionTypeCriterion");
        opt.setStringParser(JSAP.STRING_PARSER);
        opt.setHelp("Search criterion: specify the exception type (e.g. java.lang.NullPointerException).");
        this.jsap.registerParameter(opt);

        DateStringParser dateStringParser = DateStringParser.getParser();
        dateStringParser.setProperty("format", "dd/MM/yyyy");

        opt = new FlaggedOption("initialDateCriterion");
        opt.setLongFlag("initialDateCriterion");
        opt.setStringParser(dateStringParser);
        opt.setUsageName("format dd/MM/yyyy");
        opt.setHelp("Search criterion: specify the initial date to analyze branches (e.g. 01/01/2017). Note that the search starts from 00:00:00 of the specified date.");
        this.jsap.registerParameter(opt);

        opt = new FlaggedOption("finalDateCriterion");
        opt.setLongFlag("finalDateCriterion");
        opt.setStringParser(dateStringParser);
        opt.setUsageName("format dd/MM/yyyy");
        opt.setHelp("Search criterion: specify the final date to analyze branches (e.g. 31/01/2017). Note that the search is until 23:59:59 of the specified date.");
        this.jsap.registerParameter(opt);

        opt = new FlaggedOption("maxEstimatedBuildAndTestDurationCriterion");
        opt.setLongFlag("maxEstimatedBuildAndTestDurationCriterion");
        opt.setStringParser(JSAP.INTEGER_PARSER);
        opt.setHelp("Search criterion: specify the maximum duration that is estimated to build and test a bug (e.g. 10000).");
        this.jsap.registerParameter(opt);

        opt = new FlaggedOption("maxNumberOfFailingTestsCriterion");
        opt.setLongFlag("maxNumberOfFailingTestsCriterion");
        opt.setStringParser(JSAP.INTEGER_PARSER);
        opt.setHelp("Search criterion: specify the maximum number of failing tests of a bug (e.g. 1).");
        this.jsap.registerParameter(opt);
    }

    private void checkAndInitArgs() {
        if (this.arguments.getBoolean("help")) {
            this.printUsage();
        }

        if (!this.arguments.success()) {
            for (java.util.Iterator<?> errs = arguments.getErrorMessageIterator(); errs.hasNext();) {
                System.err.println("Error: " + errs.next());
            }
            this.printUsage();
        }

        this.outputMode = OutputMode.valueOf(this.arguments.getString("outputMode").toUpperCase());
        this.outputPath = this.arguments.getFile("outputPath").getPath();

        this.projectNameCriterion = this.arguments.getString("projectNameCriterion");
        this.exceptionTypeCriterion = this.arguments.getString("exceptionTypeCriterion");
        this.initialDateCriterion = this.arguments.getDate("initialDateCriterion");
        this.finalDateCriterion = this.arguments.getDate("finalDateCriterion");
        this.maxEstimatedBuildAndTestDurationCriterion = this.arguments.getInt("maxEstimatedBuildAndTestDurationCriterion", -1);
        this.maxNumberOfFailingTestsCriterion = this.arguments.getInt("maxNumberOfFailingTestsCriterion", -1);
    }

    private void printUsage() {
        System.err.println("Usage option:");
        System.err.println();
        System.err.println(jsap.getHelp());
        System.exit(-1);
    }

    private void mainProcess() throws IOException {
        LOGGER.info("Starting...");

        JsonParser jsonParser = new JsonParser(this.jsonFileFolderPath);
        Benchmark benchmark = jsonParser.run();

        Criteria searchCriteria = new Criteria();

        if (this.projectNameCriterion != null) {
            searchCriteria.add(new ProjectNameCriterion(this.projectNameCriterion));
        }
        if (this.exceptionTypeCriterion != null) {
            searchCriteria.add(new ExceptionTypeCriterion(this.exceptionTypeCriterion));
        }
        if (this.initialDateCriterion != null || this.finalDateCriterion != null) {
            searchCriteria.add(new PatchedBuildDateCriterion(this.initialDateCriterion, this.finalDateCriterion));
        }
        if (this.maxEstimatedBuildAndTestDurationCriterion > -1) {
            searchCriteria.add(new MaxEstimatedBuildAndTestDurationCriterion(this.maxEstimatedBuildAndTestDurationCriterion));
        }
        if (this.maxNumberOfFailingTestsCriterion > -1) {
            searchCriteria.add(new MaxNumberOfFailingTestsCriterion(this.maxNumberOfFailingTestsCriterion));
        }

        List<Bug> filteredBugs = searchCriteria.meets(benchmark.getBugs());

        Output output = new Output(this.outputMode, this.outputPath, benchmark);
        output.run(filteredBugs);

        LOGGER.info("Process is finished.");
        System.exit(0);
    }

    public static void main(String[] args) throws IOException, JSAPException {
        Launcher launcher = new Launcher(args);
        launcher.mainProcess();
    }

}
