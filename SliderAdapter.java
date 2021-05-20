package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app.model.SliderItem;
import com.example.app.model.home;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private ArrayList<SliderItem> sLiderItems;
    private ViewPager2 viewPager2;

     SliderAdapter( ArrayList<SliderItem> sLiderItems ,  ViewPager2 viewPager2) {
        this.sLiderItems = sLiderItems;
        this.viewPager2 = viewPager2;
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        return new SliderViewHolder
                (
                LayoutInflater.from(parent.getContext ()).inflate ( R.layout.slide_item_container,
                        parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder , int position) {



      holder.setImage ( sLiderItems.get(position ) );
        if(position == this.sLiderItems.size() - 2) {
            this.viewPager2.post( this.runnable );
        }

    }

    @Override
    public int getItemCount() {
        return this.sLiderItems.size ();
    }



    static class SliderViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;
        public SliderViewHolder(@NonNull final View itemView ) {
            super ( itemView );
            imageView = itemView.findViewById ( R.id.imageSlide );


        }

        void setImage(SliderItem sliderItem)
        {
            Picasso.get ( ).load ( sliderItem.getImg1 () ).centerCrop ( ).fit ( ).noFade ( ).into ( imageView);
            Picasso.get ( ).load (sliderItem.getImg2 ()).centerCrop ( ).fit ( ).noFade ( ).into ( imageView);
            Picasso.get ( ).load ( sliderItem.getImg3 ( ) ).centerCrop ( ).fit ( ).noFade ( ).into ( imageView  );
            Picasso.get ( ).load ( sliderItem.getImg4 ( ) ).centerCrop ( ).fit ( ).noFade ( ).into ( imageView );
            Picasso.get ( ).load ( sliderItem.getImg5 ( ) ).centerCrop ( ).fit ( ).noFade ( ).into ( imageView );
            //fetching images using piccasso here
        }
    }

    private Runnable runnable = new Runnable ( ) {
        @Override
        public void run() {
            sLiderItems.addAll ( sLiderItems );
            notifyDataSetChanged ();

        }
    };
}
