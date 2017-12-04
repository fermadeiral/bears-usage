package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class Criteria implements Criterion {

    private List<Criterion> criteria;

    public Criteria() {
        this.criteria = new ArrayList<>();
    }

    public void add(Criterion criterion) {
        this.criteria.add(criterion);
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = bugs;

        for (Criterion criterion : this.criteria) {
            filteredBugs = criterion.meets(filteredBugs);
        }

        return filteredBugs;
    }
}
