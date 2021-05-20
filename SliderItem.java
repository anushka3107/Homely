package com.example.app.model;

import com.google.gson.annotations.SerializedName;

public final class SliderItem {

    @SerializedName("img1")
    private String img1;
    @SerializedName("img2")
    private String img2;
    @SerializedName("img3")
    private String img3;
    @SerializedName("img4")
    private String img4;
    @SerializedName("img5")
    private String img5;

    public SliderItem(final String img1 , final String img2 , final String img3 , final String img4 , final String img5) {
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
    }

    public SliderItem() {
    }

    public String getImg1() {
        return this.img1;
    }

    public String getImg2() {
        return this.img2;
    }

    public String getImg3() {
        return this.img3;
    }

    public String getImg4() {
        return this.img4;
    }

    public String getImg5() {
        return this.img5;
    }
}
