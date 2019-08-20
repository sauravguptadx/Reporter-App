package com.farzana.reporter;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://172.20.42.82/project/comment.php";
    private Map<String, String> params;

    public CommentRequest(int report_id, String user_id, String comments, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("report_id", report_id +"");
        params.put("user_id", user_id);
        params.put("comments", comments);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }
}
