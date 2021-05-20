package com.example.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class DisplayAct extends AppCompatActivity {

    private AppCompatImageView   ownercard;
    private AppCompatTextView housename, ratingtext, noofbed , noofbath , description , statictext ,range ,
    livingpref,allowedpeople;
    private MaterialButton VirtualTourBtn , Book ;
    private AppCompatRatingBar ratingBar;
    private RoundedImageView houseimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        houseimage = findViewById ( R.id.houseimage );
        housename = findViewById ( R.id.housename );
        noofbath = findViewById ( R.id.noofbath );
        noofbed = findViewById ( R.id.noofbed );
        description = findViewById ( R.id.description );
        ratingBar = findViewById ( R.id.ratingbar );
        ownercard = findViewById ( R.id.owner );
        ratingtext = findViewById ( R.id.ratingtext );
        statictext = findViewById ( R.id.statictext );
        livingpref = findViewById ( R.id.livingpreftext );
        allowedpeople = findViewById ( R.id.peopleallowedtext );
        range = findViewById ( R.id.pricerangetext );
        VirtualTourBtn = findViewById ( R.id.gallerybtn );


        VirtualTourBtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                startActivity(new Intent (getApplicationContext(), VirtualTourAct.class));



            }
        } );
        Book = findViewById ( R.id.BookBtn );
        Book.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                startActivity(new Intent (getApplicationContext(), booking.class));

            }
        } );


    }


    @Override
    protected void onResume() {
        super.onResume ( );

        Bundle bundle = getIntent ().getExtras ();
        if (bundle != null)
        {

            housename.setText ( bundle.getCharSequence ( "name" ) );
            description.setText ( bundle.getCharSequence ( "description" ) );
            noofbed.setText ( bundle.getCharSequence ( "bedroom" ) );
            noofbath.setText ( bundle.getCharSequence ( "restroom" ) );
            ratingtext.setText ( bundle.getCharSequence ( "rating" ) );
            livingpref.setText ( bundle.getCharSequence ( "livingpref" ) );
            allowedpeople.setText ( bundle.getCharSequence ( "people_allowed" ) );
            range.setText ( bundle.getCharSequence ( "pricerange" ) );
            Picasso.get().load ( String.valueOf ( bundle.getCharSequence("image" ) ) ).fit ().into ( houseimage );
            Picasso.get().load ( String.valueOf ( bundle.getCharSequence("owner_details" ) ) ).fit ().into ( ownercard );

        }

        Bundle filterbundle = getIntent ().getExtras ();

        if(filterbundle != null)
        {
            housename.setText ( bundle.getCharSequence ( "name" ) );
            description.setText ( bundle.getCharSequence ( "description" ) );
            noofbed.setText ( bundle.getCharSequence ( "bedroom" ) );
            noofbath.setText ( bundle.getCharSequence ( "restroom" ) );
            ratingtext.setText ( bundle.getCharSequence ( "rating" ) );
            livingpref.setText ( bundle.getCharSequence ( "livingpref" ) );
            allowedpeople.setText ( bundle.getCharSequence ( "people_allowed" ) );
            range.setText ( bundle.getCharSequence ( "pricerange" ) );
            Picasso.get().load ( String.valueOf ( bundle.getCharSequence("image" ) ) ).fit ().into ( houseimage );
            Picasso.get().load ( String.valueOf ( bundle.getCharSequence("owner_details" ) ) ).fit ().into ( ownercard );

        }


    }

}
