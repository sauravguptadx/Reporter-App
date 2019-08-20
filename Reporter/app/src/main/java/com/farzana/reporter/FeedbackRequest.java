package com.farzana.reporter;

/**
 * Created by chhet on 1/22/2018.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FeedbackRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://172.20.42.82/project/setfeedback.php";
    private Map<String, String> params;

    public FeedbackRequest(int report_id, String responsible, String feedback, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("report_id", report_id +"");
        params.put("responsible", responsible);
        params.put("feedback", feedback);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }
}
