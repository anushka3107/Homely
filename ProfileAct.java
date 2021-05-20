package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.model.User;
import com.example.app.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileAct extends AppCompatActivity implements View.OnClickListener {
    private AppCompatImageView profileImage;
    private AppCompatEditText unameEdit, emailEdit;
    private AppCompatButton updateBtn;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private Bitmap bitmap;
    private  static final int PICK_IMAGE_REQUEST=1;
    public static  final  String KEY_UNAME = "uname";
    public static  final  String KEY_EMAIL = "email";
    public static  final  String KEY_IMAGE = "image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        updateBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("mypref",MODE_PRIVATE);
        unameEdit.setText(preferences.getString("uname",""));
        emailEdit.setClickable(false);
        emailEdit.setText(preferences.getString("email", ""));
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                //fetchimage();
            }
        });


    }


    public void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void init() {
        profileImage = findViewById(R.id.profileImage);
        unameEdit = findViewById(R.id.usernameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        updateBtn = findViewById(R.id.updateBtn);
    }

    @Override
    public void onClick(View v) {
        if(v==updateBtn)
            updateUser();
    }

    private void updateUser() {
        String imgurl = getStringImage(bitmap);
        String uname = unameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        if(imgurl!=null && uname!=null && email!=null)
        {
            stringRequest = new StringRequest(Request.Method.POST, Constants.IMAGE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO",response);
                            if(response.equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(KEY_UNAME, uname);
                    hashMap.put(KEY_EMAIL, email);
                    hashMap.put(KEY_IMAGE, imgurl);
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                profileImage.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}