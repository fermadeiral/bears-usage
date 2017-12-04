package fr.inria.spirals.bears.usage;

import fr.inria.spirals.bears.usage.model.Benchmark;
import fr.inria.spirals.bears.usage.model.Bug;
import fr.inria.spirals.bears.usage.model.ExceptionType;
import fr.inria.spirals.bears.usage.model.Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class Output {

    private OutputMode outputMode;
    private String outputPath;
    private Benchmark benchmark;

    Output(OutputMode outputMode, String outputPath, Benchmark benchmark) {
        this.outputMode = outputMode;
        this.outputPath = outputPath;
        this.benchmark = benchmark;
    }

    public void run(List<Bug> filteredBugs) {
        System.out.println("# Bugs: " + this.benchmark.getBugs().size());

        System.out.println("# Projects: " + this.benchmark.getProjects().size());

        System.out.println("\nBug types: ");
        for (Map.Entry entry : this.benchmark.getBugTypeToCounterMap().entrySet()) {
            System.out.println("# " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n# Distinct exception types: " + this.benchmark.getExceptionTypes().size());

        System.out.println("\nException types out:");
        for (Map.Entry<String, Integer> exceptionTypeOutToCounter : this.benchmark.getExceptionTypesOutToCounterMap().entrySet()) {
            System.out.println(exceptionTypeOutToCounter.getKey() + ": " + exceptionTypeOutToCounter.getValue());
        }

        if (this.outputMode == OutputMode.METRICS) {
            createNumberOfBugsByProjectsCsvFile();
            createDistributionExceptionTypeOccurrencesByProjectsCsvFile();
            createReproductionDurationByProjectsCsvFile();
        } else {
            createBranchesTextFile(filteredBugs);
        }
    }

    private void createBranchesTextFile(List<Bug> filteredBugs) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputPath + "/branches.txt");

            String line;
            for (Bug bug : filteredBugs) {
                line = bug.getBranchName() + "\n";
                fileWriter.append(line);
            }

            System.out.println("\nBranches text file was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createNumberOfBugsByProjectsCsvFile() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputPath + "/number-of-bugs-by-projects.csv");

            Collections.sort(this.benchmark.getProjects());

            String fileHeader = "project,number of bugs\n";
            fileWriter.append(fileHeader);

            String line;
            for (Project project : this.benchmark.getProjects()) {
                line = project.getName() + "," + project.getNumberOfBugs() + "\n";
                fileWriter.append(line);
            }

            System.out.println("\nNumber of bugs by projects CSV file was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDistributionExceptionTypeOccurrencesByProjectsCsvFile() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputPath + "/distribution-exception-type-occurrences-by-projects.csv");

            Collections.sort(this.benchmark.getProjects());

            String fileHeader = "exception type";
            for (Project project : this.benchmark.getProjects()) {
                fileHeader += "," + project.getName();
            }
            fileHeader += ",sum\n";
            fileWriter.append(fileHeader);

            String line;
            for (ExceptionType exceptionType : this.benchmark.getExceptionTypes()) {
                String exceptionTypeDescription = exceptionType.getDescription();
                line = exceptionTypeDescription + ",";
                int sumForExceptionType = 0;
                for (Project project : this.benchmark.getProjects()) {
                    if (project.containsExceptionType(exceptionTypeDescription)) {
                        int number = project.getOccurrenceCounterOfExceptionType(exceptionTypeDescription);
                        line += number + ",";
                        sumForExceptionType += number;
                    } else {
                        line += "0,";
                    }
                }
                line += sumForExceptionType + "\n";
                fileWriter.append(line);
            }
            line = "sum";
            for (Project project : this.benchmark.getProjects()) {
                line += "," + project.getOccurrenceCounterOfAllExceptionTypes();
            }
            fileWriter.append(line);

            System.out.println("\nDistribution exception type occurrences by projects CSV file was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createReproductionDurationByProjectsCsvFile() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputPath + "/reproduction-duration-by-projects.csv");

            Collections.sort(this.benchmark.getProjects());

            String fileHeader = "project,average,min,max\n";
            fileWriter.append(fileHeader);

            String line;
            for (Project project : this.benchmark.getProjects()) {
                line = project.getName() + ",";
                int sum = 0, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
                for (Bug bug : project.getBugs()) {
                    int reproductionDuration = bug.getReproductionDuration();
                    sum += reproductionDuration;
                    min = (reproductionDuration < min) ? reproductionDuration : min;
                    max = (reproductionDuration > max) ? reproductionDuration : max;
                }
                line += sum / project.getNumberOfBugs() + "," + min + "," + max + "\n";
                fileWriter.append(line);
            }

            System.out.println("\nStep total duration by projects CSV file was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}