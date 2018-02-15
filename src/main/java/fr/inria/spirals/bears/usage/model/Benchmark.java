package fr.inria.spirals.bears.usage.model;

import java.util.*;

/**
 * Created by fermadeiral on 31/08/17.
 */
public class Benchmark {

    private List<Bug> bugs;
    private List<Project> projects;
    private List<ExceptionType> allExceptionTypes;
    private List<ExceptionType> exceptionTypesIn;
    private Map<String, Integer> exceptionTypesOutToCounterMap;
    private Map<String, Integer> bugTypeToCounterMap;

    public Benchmark() {
        this.bugs = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.allExceptionTypes = new ArrayList<>();
        this.exceptionTypesIn = new ArrayList<>();
        this.exceptionTypesOutToCounterMap = new HashMap<>();
        this.exceptionTypesOutToCounterMap.put("skipped", 0);
        this.exceptionTypesOutToCounterMap.put("Wanted but not invoked", 0);
        this.exceptionTypesOutToCounterMap.put("Condition not satisfied", 0);
        this.bugTypeToCounterMap = new HashMap<>();
    }

    public Bug addBug(String branchName, Project project, Date buggyBuildDate) {
        Bug bug = new Bug(branchName, project, buggyBuildDate);
        this.bugs.add(bug);
        project.addBug(bug);
        return bug;
    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public Project addProject(String projectName) {
        for (Project project : this.projects) {
            if (project.getName().equals(projectName)) {
                return project;
            }
        }
        Project project = new Project(projectName);
        this.projects.add(project);
        return project;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public ExceptionType addExceptionType(String exceptionTypeDescription) {
        for (ExceptionType exceptionType : this.allExceptionTypes) {
            if (exceptionType.getDescription().equals(exceptionTypeDescription)) {
                if (this.exceptionTypesOutToCounterMap.keySet().contains(exceptionTypeDescription)) {
                    this.exceptionTypesOutToCounterMap.put(exceptionTypeDescription, this.exceptionTypesOutToCounterMap.get(exceptionTypeDescription) + 1);
                }
                return exceptionType;
            }
        }
        ExceptionType exceptionType = new ExceptionType(exceptionTypeDescription);
        if (this.exceptionTypesOutToCounterMap.keySet().contains(exceptionTypeDescription)) {
            this.exceptionTypesOutToCounterMap.put(exceptionTypeDescription, 1);
        } else {
            this.exceptionTypesIn.add(exceptionType);
        }
        this.allExceptionTypes.add(exceptionType);
        return exceptionType;
    }

    public List<ExceptionType> getExceptionTypes() {
        return exceptionTypesIn;
    }

    public Map<String, Integer> getExceptionTypesOutToCounterMap() {
        return this.exceptionTypesOutToCounterMap;
    }

    public void addBugType(String bugTypeDescription) {
        if (!this.bugTypeToCounterMap.keySet().contains(bugTypeDescription)) {
            this.bugTypeToCounterMap.put(bugTypeDescription, 0);
        }
        this.bugTypeToCounterMap.put(bugTypeDescription, this.bugTypeToCounterMap.get(bugTypeDescription) + 1);
    }

    public Map<String, Integer> getBugTypeToCounterMap() {
        return this.bugTypeToCounterMap;
    }

}
