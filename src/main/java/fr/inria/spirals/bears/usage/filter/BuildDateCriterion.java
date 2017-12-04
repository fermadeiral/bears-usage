package fr.inria.spirals.bears.usage.filter;

import fr.inria.spirals.bears.usage.model.Bug;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fermadeiral on 27/12/17.
 */
public class BuildDateCriterion implements Criterion {

    private Date lookFromDate;
    private Date lookToDate;

    private Pattern patternToGetDateFromBranchName = Pattern.compile("(.*)[-]\\d*[-](\\d*)[-]\\d*");

    public BuildDateCriterion(Date lookFromDate, Date lookToDate) {
        this.lookFromDate = lookFromDate;
        this.lookToDate = lookToDate;
    }

    public List<Bug> meets(List<Bug> bugs) {
        List<Bug> filteredBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            Matcher matcher = patternToGetDateFromBranchName.matcher(bug.getBranchName());
            if (matcher.find()) {
                String dateStr = matcher.group(2);
                DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date;
                try {
                    date = formatter.parse(dateStr);
                    if (this.lookFromDate != null && date.before(lookFromDate)) {
                        continue;
                    }
                    if (this.lookToDate != null && date.after(lookToDate)) {
                        continue;
                    }
                    filteredBugs.add(bug);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return filteredBugs;
    }

}
