package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.util.List;

/**
 * Created by fermadeiral on 27/12/17.
 */
public interface Criterion {

    List<Bug> meets(List<Bug> bugs);

}
