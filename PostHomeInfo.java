package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PostHomeInfo extends AppCompatActivity implements View.OnClickListener {

    private AppCompatSpinner BudgetSpinner;
    private com.google.android.material.textfield.TextInputEditText HouseName, Location, Price,
            description, Bedroom, Restroom, ratings;
    private AppCompatTextView upload , ownercardupload;


    private AppCompatImageView ownercardimage ,houseimage;

    private AppCompatCheckBox couples, singles, family;
    private static MaterialButton PostBtn;
    private static final String KEY_HOUSENAME = "name";
    private static final String KEY_LOCATION = "place";
    private static final String KEY_PRICE = "price";
    private static final String KEY_HOUSEIMAGE = "image";
    private static final String KEY_DESC = "description";
    private static final String KEY_BEDROOM= "bedroom";
    private static final String KEY_RESTROOM = "restroom";
    private static final String KEY_RATING = "rating";
    private static final String KEY_OWNERDETAILS = "owner_details";

    private  static final int PICK_IMAGE_REQUEST=1;
    private static final int PICK_OWNER_REQUEST =2;
    private String BudgetArrayprice[] , user_id;
    private Bitmap bitmap;
    private String budgetstring;


    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_home_info );
        preferences = getSharedPreferences ( "mypref" , MODE_PRIVATE );
        user_id= preferences.getString("email","");
        BudgetArrayprice =getResources ().getStringArray ( R.array.BudgetArray );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> ( PostHomeInfo.this ,
                android.R.layout.simple_spinner_dropdown_item , BudgetArrayprice );


//        //BudgetSpinner.setAdapter ( arrayAdapter );
//        BudgetSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
//            @Override
//            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
//               budgetstring = (String) parent.getItemAtPosition ( position );
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        } );



     //  requestWindowFeature( Window.FEATURE_NO_TITLE);//will hide the title.
        init ( );
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        PostBtn.setOnClickListener ( this );
        upload.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        } );

        ownercardupload.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pickcardImage();
            }

            private void pickcardImage() {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        PICK_OWNER_REQUEST);


            }
        } );

    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void init() {
        HouseName = findViewById ( R.id.hname );
        Location = findViewById ( R.id.hplace );
        Price = findViewById ( R.id.hprice );
        ratings = findViewById ( R.id.hrating );
        description = findViewById ( R.id.hdescription );
        Bedroom = findViewById ( R.id.hbed );
        Restroom = findViewById ( R.id.hbath );
        couples = findViewById ( R.id.selectcouples );
        singles = findViewById ( R.id.selectsingles );
        family = findViewById ( R.id.selectfamily );
        upload = findViewById ( R.id.uploadbtn );
        ownercardupload = findViewById ( R.id.ownercardupload );
        PostBtn = findViewById ( R.id.PostBtn );
        houseimage = findViewById ( R.id.himage );
        ownercardimage = findViewById ( R.id.ownercard );
        BudgetSpinner = findViewById ( R.id.budgetspinner );
        
    }

    @Override
    public void onClick(View v) {

        if (v == PostBtn) {
            PostData ( );
        }

    }

    private void PostData() {

        String imgurl = getStringImage(bitmap);

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , Constants.POST_URL ,
                new Response.Listener<String> ( ) {
                    @Override
                    public void onResponse(String response) {
                        Log.i ( "INFO " , response );
                        if (response.equals ( "sucess" ))
                        {
                            Toast.makeText ( getApplicationContext ( ) , "Data Posted Successfully" , Toast.LENGTH_SHORT ).show ( );
                            Intent intent = new Intent ( getApplicationContext ( ) , NavAct.class );
                            intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity ( intent );
                            finish ( );
                        }
                        else {
                            Toast.makeText ( getApplicationContext ( ) , response , Toast.LENGTH_SHORT ).show ( );
                        }
                    }


                } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e ( "ERROR" , error.getMessage ( ) );
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        } )

        {
        @NotNull
        @Override
        protected Map<String, String> getParams() throws AuthFailureError{

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(KEY_HOUSENAME,HouseName.getText().toString().trim());
            hashMap.put(KEY_LOCATION, Location.getText().toString().trim());
            hashMap.put(KEY_PRICE, Price.getText().toString().trim());
            // hashMap.put ( KEY_HOUSE IMAGE, )
            hashMap.put ( KEY_DESC, description.getText ().toString ().trim () );
            hashMap.put ( KEY_BEDROOM, Bedroom.getText ().toString ().trim () );
            hashMap.put (KEY_RESTROOM , Restroom.getText ().toString ().trim ());
            hashMap.put ( KEY_RATING , ratings.getText ().toString ().trim () );
           //hashMap.put(KEY_OWNER DETAILS , owner_details.getText().toString().trim() );
            return hashMap;
        }
        };

        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

}

