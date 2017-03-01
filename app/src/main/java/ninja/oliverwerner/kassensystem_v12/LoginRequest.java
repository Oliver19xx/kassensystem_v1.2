package ninja.oliverwerner.kassensystem_v12;

import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Oliver on 21.02.2017.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = ("/reader.php");
    private Map<String, String> params;

    public LoginRequest(String email, String pw, String server, Response.Listener<String> listener){
        super(Method.POST, ("http://" + server + LOGIN_REQUEST_URL), listener, null);
        String password = Base64.encodeToString(pw.getBytes(), Base64.DEFAULT);
        password = password.replace("\n","");
        params = new HashMap<>();
        params.put("method", "login");
        params.put("authkey", "test321");
        params.put("user", email);
        params.put("password", password);

        Log.d("LoginRequest","http://" + server + LOGIN_REQUEST_URL);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
