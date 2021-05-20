package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.model.User;
import com.example.app.model.bookings;
import com.example.app.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class booking extends AppCompatActivity {

    private TextInputEditText Name, Contact, Email,Datetext , timetext;
    private MaterialButton Confirm;
    private Button Cancel;


    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NUM = "contact";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        this.setContentView ( R.layout.activity_booking );
        //crashing

        this.Name = this.findViewById ( R.id.VisitorName );
        this.Contact = this.findViewById ( R.id.VisitorPhone );
        this.Email = this.findViewById ( R.id.VisitorEmailAddress );
        this.Confirm = this.findViewById ( R.id.ConfirmBtn );
        this.Cancel = this.findViewById ( R.id.CancelBtn );
        this.Datetext = this.findViewById ( R.id.date );
        this.timetext = this.findViewById ( R.id.time );

        this.Confirm.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(final View v) {

                booking.this.InsertSchedules ( );
            }
        } );

        this.Cancel.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(final View v) {


                booking.this.startActivity ( new Intent ( booking.this.getApplicationContext ( ) , DisplayAct.class ) );

            }
        } );


    }

    private void InsertSchedules() {

        {
            final bookings Bookings = new DAOClassBookings ( ).setData ( );
            this.stringRequest = new StringRequest ( Request.Method.POST , Constants.bookings_url ,
                    new Response.Listener<String> ( ) {
                        @Override
                        public void onResponse(final String response) {
                            Log.i ( "INFO" , response );
                            if (Bookings != null) {
                                if (response.equals ( "success" )) {
                                    Toast.makeText ( booking.this.getApplicationContext ( ) , "Booked Successfully" , Toast.LENGTH_SHORT ).show ( );
                                    final Intent intent = new Intent ( booking.this.getApplicationContext ( ) , NavAct.class );
                                    intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    booking.this.startActivity ( intent );
                                    booking.this.finish ( );
                                } else {
                                    Toast.makeText ( booking.this.getApplicationContext ( ) , response , Toast.LENGTH_SHORT ).show ( );
                                }
                            }
                        }
                    } , new Response.ErrorListener ( ) {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e ( "ERROR" , error.getMessage ( ) );
                    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            } ) {
                @NotNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    final HashMap<String, String> hashMap = new HashMap<> ( );
                    hashMap.put ( booking.KEY_NAME , String.valueOf ( booking.this.Name.getText ( ) ) );
                    hashMap.put ( booking.KEY_EMAIL , String.valueOf ( booking.this.Email.getText ( ) ) );
                    hashMap.put ( booking.KEY_NUM , String.valueOf ( booking.this.Contact.getText ( ) ) );
                    hashMap.put ( booking.KEY_DATE , String.valueOf ( Datetext.getText () ) );
                    hashMap.put ( booking.KEY_TIME , String.valueOf ( timetext.getText () ) );
                    return hashMap;
                }
            };
            this.singleton = VolleySingleton.getInstance ( this );
            this.singleton.addToRequestQueue ( this.stringRequest );
        }
    }

    private class DAOClassBookings extends bookings {
        public bookings setData() {
            //  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            final String name = booking.this.Name.getText ( ).toString ( ).trim ( );
            final String email = booking.this.Email.getText ( ).toString ( ).trim ( );
             String contact = booking.this.Contact.getText ( ).toString ( ).trim ( );
            String date = booking.this.Datetext.getText ( ).toString ( ).trim ( );
            String time = timetext.getText ().toString ().trim ();

            if (TextUtils.isEmpty ( name ))
                booking.this.Name.setError ( "Please Fill the Field" );
            else if (TextUtils.isEmpty ( email ))
                booking.this.Email.setError ( "Please Fill the Field" );

            else if (TextUtils.isEmpty ( date ))
                booking.this.Datetext.setError ( "Please Fill the Field" );
            else if (TextUtils.isEmpty (time ))
                booking.this.timetext.setError ( "Please Fill the Field" );
             else if (TextUtils.isEmpty ( contact ))
                booking.this.Contact.setError ( "Please Fill the Field" );
            else if (contact.length ( ) != 10)
                booking.this.Contact.setError ( "Contact Number is Incorrect" );
                //   else if (email != emailPattern )
                //    emailEdit.setError ( "Incorrect email" );
            else {
                return new bookings ( name , email , contact , date , time );
            }
            return null;
        }
    }


}
