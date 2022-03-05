package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Future")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Future{
    public String contract;
    public String expiry;
    public Price livePrice;
}
