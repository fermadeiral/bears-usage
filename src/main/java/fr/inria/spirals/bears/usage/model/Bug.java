package fr.inria.spirals.bears.usage.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fermadeiral on 31/08/17.
 */
public class Bug {

    private String branchName;
    private Project project;
    private Date patchedBuildDate;
    private int numberOfFailingTests;
    private Map<ExceptionType, Integer> exceptionTypeToOccurrenceCounterMap;
    private Map<String, Integer> stepToDurationMap;
    private int reproductionDuration;
    private int estimatedBuildAndTestDuration;

    Bug(String branchName, Project project, Date patchedBuildDate) {
        this.branchName = branchName;
        this.project = project;
        this.patchedBuildDate = patchedBuildDate;
        this.exceptionTypeToOccurrenceCounterMap = new HashMap<>();
        this.stepToDurationMap = new HashMap<>();
    }

    public String getBranchName() {
        return this.branchName;
    }

    public Project getProject() {
        return this.project;
    }

    public Date getPatchedBuildDate() {
        return this.patchedBuildDate;
    }

    public void setNumberOfFailingTests(int numberOfFailingTests) {
        this.numberOfFailingTests = numberOfFailingTests;
    }

    public int getNumberOfFailingTests() {
        return this.numberOfFailingTests;
    }

    public void addExceptionType(ExceptionType exceptionType) {
        if (!this.exceptionTypeToOccurrenceCounterMap.keySet().contains(exceptionType)) {
            this.exceptionTypeToOccurrenceCounterMap.put(exceptionType, 0);
        }
        this.exceptionTypeToOccurrenceCounterMap.put(exceptionType, this.exceptionTypeToOccurrenceCounterMap.get(exceptionType) + 1);
    }

    public boolean containsExceptionType(String exceptionTypeDescription) {
        for (ExceptionType exceptionType : this.exceptionTypeToOccurrenceCounterMap.keySet()) {
            if (exceptionType.getDescription().equals(exceptionTypeDescription)) {
                return true;
            }
        }
        return false;
    }

    public int getOccurrenceCounterOfExceptionType(String exceptionTypeDescription) {
        for (ExceptionType exceptionType : this.exceptionTypeToOccurrenceCounterMap.keySet()) {
            if (exceptionType.getDescription().equals(exceptionTypeDescription)) {
                return this.exceptionTypeToOccurrenceCounterMap.get(exceptionType);
            }
        }
        return 0;
    }

    public int getOccurrenceCounterOfAllExceptionTypes() {
        int occurrenceCounter = 0;
        for (Map.Entry<ExceptionType, Integer> exceptionTypeToOccurrenceCounter : this.exceptionTypeToOccurrenceCounterMap.entrySet()) {
            occurrenceCounter += exceptionTypeToOccurrenceCounter.getValue();
        }
        return occurrenceCounter;
    }

    public void addStepToDuration(String stepName, int duration) {
        this.stepToDurationMap.put(stepName, duration);
    }

    public void setReproductionDuration(int reproductionDuration) {
        this.reproductionDuration = reproductionDuration;
    }

    public int getReproductionDuration() {
        return this.reproductionDuration;
    }

    public void setEstimatedBuildAndTestDuration(int estimatedBuildAndTestDuration) {
        this.estimatedBuildAndTestDuration = estimatedBuildAndTestDuration;
    }

    public int getEstimatedBuildAndTestDuration() {
        return this.estimatedBuildAndTestDuration;
    }

}
