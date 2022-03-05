package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Option")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    public double open;
    public double high;
    public double low;
    public double close;
    public double ltp;
    public double dayChange;
    public double dayChangePerc;
    public double lowPriceRange;
    public double highPriceRange;
    public int volume;
    public double totalBuyQty;
    public double totalSellQty;
    public int openInterest;
    public int prevOpenInterest;
    public int lastTradeQty;
    public int lastTradeTime;
    public String growwContractId;
    public String contractDisplayName;
    public String longDisplayName;
    public double value;
}
