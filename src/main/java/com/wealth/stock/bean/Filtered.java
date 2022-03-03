package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Filtered {
    public ArrayList<OCData> data;
    @JsonProperty("CE")
    public Info cE;
    @JsonProperty("PE")
    public Info pE;
}
