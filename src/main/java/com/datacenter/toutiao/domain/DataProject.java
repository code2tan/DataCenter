package com.datacenter.toutiao.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DataProject {
    @SerializedName("Title")
    public String title;
    @SerializedName("Url")
    public String url;
    @SerializedName("HotValue")
    public String hotValue;
    @SerializedName("QueryWord")
    public String queryWord;
}