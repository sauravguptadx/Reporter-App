package com.farzana.reporter;

/**
 * Created by Saurav on 14-Jan-18.
 */

public class Report {
     int report_id;
     String user_id;
     String responsible;
    String submission;
    String description;

    public Report(int report_id, String user_id, String responsible, String submission, String description) {
        this.report_id = report_id;
        this.user_id = user_id;
        this.responsible = responsible;
        this.submission = submission;
        this.description = description;
    }
}
