package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class MaxEstimatedBuildAndTestDurationCriterion implements Criterion {

    private int maxEstimatedBuildAndTestDuration;

    public MaxEstimatedBuildAndTestDurationCriterion(int maxEstimatedBuildAndTestDuration) {
        this.maxEstimatedBuildAndTestDuration = maxEstimatedBuildAndTestDuration;
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (bug.getEstimatedBuildAndTestDuration() <= this.maxEstimatedBuildAndTestDuration) {
                filteredBugs.add(bug);
            }
        }

        return filteredBugs;
    }

}
