package com.farzana.reporter;

/**
 * Created by Saurav on 15-Jan-18.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest2 extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://172.20.42.82/Register.php";
    private Map<String, String> params;

    public RegisterRequest2(String user_id,String full_name, String contact_no, String password, String user_type, String responsible, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("full_name", full_name);
        params.put("contact_no", contact_no);
        params.put("password", password);
        params.put("user_type", user_type);
        params.put("responsible", responsible);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}