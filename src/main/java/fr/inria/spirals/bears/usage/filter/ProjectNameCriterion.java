package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class ProjectNameCriterion implements Criterion {

    private String projectName;

    public ProjectNameCriterion(String projectName) {
        this.projectName = projectName;
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (bug.getProject().getName().equals(this.projectName)) {
                filteredBugs.add(bug);
            }
        }

        return filteredBugs;
    }

}
