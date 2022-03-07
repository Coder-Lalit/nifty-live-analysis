package com.wealth.stock.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("OptionChain")
public class OptionChain implements Comparable<OptionChain>{
    public int strikePrice;
    public Price callOption;
    public Price putOption;
    public double pcr;
    public Date timeStamp;

    public Date getTimeStamp() {
        return timeStamp;
    }


    @Override
    public int compareTo(OptionChain o) {
        return getTimeStamp().compareTo(o.timeStamp);
    }
}
