package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("OCData")
public class OCData implements Comparable<OCData>{
    public int strikePrice;
    public String expiryDate;
    public Date timeStamp;
    @JsonProperty("PE")
    public Info pE;
    @JsonProperty("CE")
    public Info cE;

    public Date getTimeStamp() {
        return timeStamp;
    }


    @Override
    public int compareTo(OCData o) {
        return getTimeStamp().compareTo(o.getTimeStamp());
    }
}