package com.wealth.stock.bean;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("NIFTY100RSI")
@Builder
public class NIFTY100RSI {
    public String name;
    public Long rsiDay;
    public Long rsiWeek;
    public Long rsiMonth;
    public Date timeStamp;
}
