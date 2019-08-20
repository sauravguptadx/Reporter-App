package com.farzana.reporter;

/**
 * Created by Saurav on 15-Jan-18.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://172.20.42.82/project/submit.php";
    private Map<String, String> params;

    public RegisterRequest(String user_id, String responsible, String description, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("responsible", responsible);
        params.put("description", description);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

