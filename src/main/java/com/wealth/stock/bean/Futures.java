package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document("Futures")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Futures {
    public Date timeStamp;
    public double pcr;
    public double currentPrice;
    public ArrayList<Future> futureArrayList;

    public Futures(Date timeStamp, ArrayList<Future> futureArrayList) {
        this.timeStamp = timeStamp;
        this.futureArrayList = futureArrayList;
    }
}
