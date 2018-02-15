package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class PatchedBuildDateCriterion implements Criterion {

    private Date lookFromDate;
    private Date lookToDate;

    public PatchedBuildDateCriterion(Date lookFromDate, Date lookToDate) {
        this.lookFromDate = lookFromDate;
        if (lookToDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lookToDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            this.lookToDate = calendar.getTime();
        }
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (this.lookFromDate != null && bug.getPatchedBuildDate().before(lookFromDate)) {
                continue;
            }
            if (this.lookToDate != null && bug.getPatchedBuildDate().after(lookToDate)) {
                continue;
            }
            filteredBugs.add(bug);
        }

        return filteredBugs;
    }

}
