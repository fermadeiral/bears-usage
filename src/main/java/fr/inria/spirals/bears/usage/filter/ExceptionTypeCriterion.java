package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class ExceptionTypeCriterion implements Criterion {

    private String exceptionTypeName;

    public ExceptionTypeCriterion(String exceptionTypeName) {
        this.exceptionTypeName = exceptionTypeName;
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (bug.containsExceptionType(this.exceptionTypeName)) {
                filteredBugs.add(bug);
            }
        }

        return filteredBugs;
    }
}
