package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.model.User;
import com.example.app.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {
    private static com.google.android.material.textfield.TextInputEditText emailEdit, passwordEdit;
    private static MaterialButton signInBtn;
    private Button registerText;
    private SharedPreferences preferences;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
//    GoogleSignInClient mGoogleSignInClient;
//    private  static final int RC_SIGN_IN = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail().build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
        init();
    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//    }


//    private void updateUI(GoogleSignInAccount account) {
//        Bundle bundle = new Bundle();
//        Intent intent = new Intent(LoginAct.this, NavAct.class);
//        bundle.putString("display_name", account.getDisplayName());
//        bundle.putString("display_email", account.getEmail());
//        bundle.putString("display_image", String.valueOf(account.getPhotoUrl()));
//        intent.putExtras(bundle);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        signInBtn.setOnClickListener(this);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterAct.class));
            }
        });
    }

    private void init() {
        emailEdit = findViewById(R.id.editTextTextEmailAddress);
        passwordEdit = findViewById(R.id.editTextTextPassword);
        signInBtn = findViewById(R.id.signInBtn);
        registerText = findViewById(R.id.registerText);
    }

    @Override
    public void onClick(View v) {
        if(v == signInBtn)
            loginUser();
    }
    @SuppressLint("CommitPrefEdits")
    private void loginUser() {
        User user = new DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO",response);
                        if(user!=null)
                        {
                            if(response.equals("success")){
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", user.getEmail());
                                editor.putString("password", user.getPasswd());
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), NavAct.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KEY_EMAIL, user.getEmail());
                hashMap.put(KEY_PASSWORD, user.getPasswd());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);

//        stringRequest.setRetryPolicy(new RetryPolicy () {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
    }



    public static class DAOClass
    {
        public User setData()
        {   //String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
            String email = emailEdit.getText ().toString().trim();
            String password = passwordEdit.getText ().toString().trim();
            if(TextUtils.isEmpty(email))
                emailEdit.setError("Please Fill the Field");
            else if(TextUtils.isEmpty(password))
                passwordEdit.setError("Please Fill the Field");
            else if(password.length()<8)
                passwordEdit.setError("Password is too short");
//            else if(email!= emailPattern)
//                emailEdit.setError("Incorrect Email");
            else{
                return new User(email,password);
            }
            return null;
        }

    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
     //  if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount>  completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult( ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//
//            assert account != null;
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("WARN", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
}