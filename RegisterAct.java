package com.example.app;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.VolleySingleton;
import com.example.app.model.User;
import com.example.app.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterAct extends AppCompatActivity  implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static com.google.android.material.textfield.TextInputEditText nameEdit, emailEdit, passwordEdit, contactEdit;
    @SuppressLint("StaticFieldLeak")
    private static Button registerBtn;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_UNAME = "uname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONTACT = "contact";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerBtn.setOnClickListener(this);
    }

    private void init()
    {
        nameEdit = findViewById(R.id.editTextTextPersonName);
        emailEdit = findViewById(R.id.editTextTextEmailAddress);
        passwordEdit = findViewById(R.id.editTextTextPassword);
        contactEdit = findViewById(R.id.editTextPhone);
        registerBtn = findViewById(R.id.registerBtn);
    }

    @Override
    public void onClick(View v)
    {
        if(v==registerBtn)
            registerUser();
    }

    private void registerUser()

     {
        User user = new DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.REG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO",response);
                        if(user!=null)
                        {
                            if(response.equals("success")){
                                Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username",user.getUname());
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), LoginAct.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());

            }
        })
        {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_UNAME, user.getUname());
                hashMap.put(KEY_EMAIL, user.getEmail());
                hashMap.put(KEY_PASSWORD, user.getPasswd());
                hashMap.put(KEY_CONTACT, user.getContact());
                return hashMap;
            }
        };

        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
//
//         stringRequest.setRetryPolicy(new RetryPolicy () {
//             @Override
//             public int getCurrentTimeout() {
//                 return 50000;
//             }
//
//             @Override
//             public int getCurrentRetryCount() {
//                 return 50000;
//             }
//
//             @Override
//             public void retry(VolleyError error) throws VolleyError {
//
//             }
//         });
    }
    private static class DAOClass
    {
        public User setData()
        {
           // String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
            String passwordPattern = "^(?=[a-z]*[0-9][a-z]*[0-9])^(?=[0-9]*[a-z][0-9]*[a-z])[a-z0-9]{8,}$";
            String email = emailEdit.getText ().toString().trim();
            String uname = nameEdit.getText().toString().trim();
            String passwd = passwordEdit.getText().toString().trim();
            String contact = contactEdit.getText().toString().trim();
            if(TextUtils.isEmpty(uname))
                nameEdit.setError("Please Fill the Field");
            else if(TextUtils.isEmpty(email))
                emailEdit.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(passwd))
                passwordEdit.setError("Please Fill the Field");
            else if (passwd.length()<8)
                passwordEdit.setError("Password is too Short");

            else if(TextUtils.isEmpty(contact))
                contactEdit.setError("Please Fill the Field");
            else if (contact.length()!=10)
               contactEdit.setError("Contact Number is Incorrect");
//            else if (email != emailPattern )
//                emailEdit.setError ( "Incorrect email" );
            else if(passwd != passwordPattern)
                passwordEdit.setError ( "Password should have at least 2 numbers and it should be alphanumeric" );
            else{
                return new User(uname, email, passwd, contact);
            }
            return null;
        }
    }
}
