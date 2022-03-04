package com.wealth.stock.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Date;

@Document("OptionChain")
public class OptionChain implements Comparable<OptionChain>{
    public int strikePrice;
    public Option callOption;
    public Option putOption;
    public Date timeStamp;

    public Date getTimeStamp() {
        return timeStamp;
    }


    @Override
    public int compareTo(OptionChain o) {
        return getTimeStamp().compareTo(o.timeStamp);
    }
}
