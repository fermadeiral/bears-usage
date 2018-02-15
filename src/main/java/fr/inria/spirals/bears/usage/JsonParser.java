package fr.inria.spirals.bears.usage;

import fr.inria.spirals.bears.usage.model.Benchmark;
import fr.inria.spirals.bears.usage.model.Bug;
import fr.inria.spirals.bears.usage.model.ExceptionType;
import fr.inria.spirals.bears.usage.model.Project;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by fermadeiral on 02/05/17.
 */
public class JsonParser {

    private String jsonFileFolderPath;

    JsonParser(String jsonFileFolderPath) {
        this.jsonFileFolderPath = jsonFileFolderPath;
    }

    public Benchmark run() {
        Benchmark benchmark = new Benchmark();

        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".json");
            }
        };
        File dir = new File(this.jsonFileFolderPath);
        File[] files = dir.listFiles(filter);

        System.out.println("# Files found: " + files.length);

        try {
            for (int i = 0; i < files.length; i++) {
                String branchName = files[i].getName().replace(".json", "");

                JSONParser parser = new JSONParser();

                JSONObject jsonBug = (JSONObject) parser.parse(new FileReader(files[i]));

                String projectName = (String) jsonBug.get("repo");

                Project project = benchmark.addProject(projectName);

                JSONObject metrics = (JSONObject) jsonBug.get("metrics");

                String patchedBuildDateStr = metrics.get("PatchedBuildDate").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                Date patchedBuildDate = null;
                try {
                  patchedBuildDate = sdf.parse(patchedBuildDateStr);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                Bug bug = benchmark.addBug(branchName, project, patchedBuildDate);

                String bugTypeDescription = (String) jsonBug.get("bugType");
                benchmark.addBugType(bugTypeDescription);

                JSONArray failingTestCases = (JSONArray) jsonBug.get("failing-test-cases");
                Iterator failingTestCasesIterator = failingTestCases.iterator();
                while (failingTestCasesIterator.hasNext()) {
                    JSONArray failures = (JSONArray) ((JSONObject) failingTestCasesIterator.next()).get("failures");
                    Iterator failuresIterator = failures.iterator();
                    while (failuresIterator.hasNext()) {
                        String exceptionTypeDescription = ((JSONObject) failuresIterator.next()).get("failureName").toString().replace(":", "");
                        ExceptionType exceptionType = benchmark.addExceptionType(exceptionTypeDescription);
                        bug.addExceptionType(exceptionType);
                    }
                }

                int numberOfFailingTests = Integer.parseInt(metrics.get("NbFailingTests").toString());
                bug.setNumberOfFailingTests(numberOfFailingTests);

                JSONObject stepDurations = (JSONObject) metrics.get("StepsDurationsInSeconds");

                int cloneRepositoryDuration = Integer.parseInt(stepDurations.get("CloneRepository").toString());
                int checkoutBuggyBuildDuration;
                int checkoutPatchedBuildDuration = Integer.parseInt(stepDurations.get("CheckoutPatchedBuild").toString());
                int computeClasspathDuration = Integer.parseInt(stepDurations.get("ComputeClasspath").toString());
                int computeSourceDirDuration = Integer.parseInt(stepDurations.get("ComputeSourceDir").toString());
                int computeTestDirDuration = Integer.parseInt(stepDurations.get("ComputeTestDir").toString());
                int resolveDependencyDuration = Integer.parseInt(stepDurations.get("ResolveDependency").toString());
                int buildProjectBuildDuration = Integer.parseInt(stepDurations.get("BuildProjectBuild").toString());
                int buildProjectPreviousBuildDuration;
                int testProjectBuildDuration = Integer.parseInt(stepDurations.get("TestProjectBuild").toString());
                int testProjectPreviousBuildDuration;
                int gatherTestInformationBuildDuration = Integer.parseInt(stepDurations.get("GatherTestInformationBuild").toString());
                int gatherTestInformationPreviousBuildDuration;
                int nopolRepairDuration = Integer.parseInt(stepDurations.get("NopolRepair").toString());
                int initRepoToPushDuration = Integer.parseInt(stepDurations.get("InitRepoToPush").toString());
                int pushIncriminatedBuildDuration = Integer.parseInt(stepDurations.get("PushIncriminatedBuild").toString());
                int commitPatchDuration = Integer.parseInt(stepDurations.get("CommitPatch").toString());

                if (bugTypeDescription.equals("failing_passing")) {
                    checkoutBuggyBuildDuration = Integer.parseInt(stepDurations.get("CheckoutBuggyBuild").toString());
                    buildProjectPreviousBuildDuration = Integer.parseInt(stepDurations.get("BuildProjectPreviousBuild").toString());
                    testProjectPreviousBuildDuration = Integer.parseInt(stepDurations.get("TestProjectPreviousBuild").toString());
                    gatherTestInformationPreviousBuildDuration = Integer.parseInt(stepDurations.get("GatherTestInformationPreviousBuild").toString());
                } else {
                    checkoutBuggyBuildDuration = Integer.parseInt(stepDurations.get("CheckoutBuggyBuildSourceCode").toString());
                    buildProjectPreviousBuildDuration = Integer.parseInt(stepDurations.get("BuildProjectPreviousBuildSourceCode").toString());
                    testProjectPreviousBuildDuration = Integer.parseInt(stepDurations.get("TestProjectPreviousBuildSourceCode").toString());
                    gatherTestInformationPreviousBuildDuration = Integer.parseInt(stepDurations.get("GatherTestInformationPreviousBuildSourceCode").toString());
                }

                bug.addStepToDuration("cloneRepository", cloneRepositoryDuration);
                bug.addStepToDuration("checkoutBuggyBuild", checkoutBuggyBuildDuration);
                bug.addStepToDuration("checkoutPatchedBuild", checkoutPatchedBuildDuration);
                bug.addStepToDuration("computeClasspath", computeClasspathDuration);
                bug.addStepToDuration("computeSourceDir", computeSourceDirDuration);
                bug.addStepToDuration("computeTestDir", computeTestDirDuration);
                bug.addStepToDuration("resolveDependency", resolveDependencyDuration);
                bug.addStepToDuration("buildProjectBuild", buildProjectBuildDuration);
                bug.addStepToDuration("buildProjectPreviousBuild", buildProjectPreviousBuildDuration);
                bug.addStepToDuration("testProjectBuild", testProjectBuildDuration);
                bug.addStepToDuration("testProjectPreviousBuild", testProjectPreviousBuildDuration);
                bug.addStepToDuration("gatherTestInformationBuild", gatherTestInformationBuildDuration);
                bug.addStepToDuration("gatherTestInformationPreviousBuild", gatherTestInformationPreviousBuildDuration);
                bug.addStepToDuration("nopolRepair", nopolRepairDuration);
                bug.addStepToDuration("initRepoToPush", initRepoToPushDuration);
                bug.addStepToDuration("pushIncriminatedBuild", pushIncriminatedBuildDuration);
                bug.addStepToDuration("commitPatch", commitPatchDuration);

                int reproductionTime = cloneRepositoryDuration + checkoutBuggyBuildDuration + checkoutPatchedBuildDuration
                        + computeClasspathDuration + computeSourceDirDuration + computeTestDirDuration
                        + resolveDependencyDuration + buildProjectBuildDuration + buildProjectPreviousBuildDuration
                        + testProjectBuildDuration + testProjectPreviousBuildDuration + gatherTestInformationBuildDuration
                        + gatherTestInformationPreviousBuildDuration + nopolRepairDuration + initRepoToPushDuration
                        + pushIncriminatedBuildDuration + commitPatchDuration;

                bug.setReproductionDuration(reproductionTime);

                int estimatedBuildAndTestTime = resolveDependencyDuration + buildProjectBuildDuration + testProjectBuildDuration;

                bug.setEstimatedBuildAndTestDuration(estimatedBuildAndTestTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return benchmark;
    }

}
