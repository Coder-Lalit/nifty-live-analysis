package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NiftyData {
    public OptionData optionChain;
    public Price livePrice;
    public ArrayList<Future> futures;
}
