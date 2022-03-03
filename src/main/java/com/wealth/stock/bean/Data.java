package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    public Filtered filtered;
}
