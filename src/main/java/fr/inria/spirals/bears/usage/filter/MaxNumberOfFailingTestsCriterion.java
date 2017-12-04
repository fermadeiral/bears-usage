package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class MaxNumberOfFailingTestsCriterion implements Criterion {

    private int maxNumberOfFailingTests;

    public MaxNumberOfFailingTestsCriterion(int maxNumberOfFailingTests) {
        this.maxNumberOfFailingTests = maxNumberOfFailingTests;
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (bug.getNumberOfFailingTests() <= this.maxNumberOfFailingTests) {
                filteredBugs.add(bug);
            }
        }

        return filteredBugs;
    }

}
