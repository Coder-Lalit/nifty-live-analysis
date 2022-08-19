package com.wealth.stock.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class ListFilters{
    @JsonProperty("INDUSTRY")
    public ArrayList<String> iNDUSTRY;
    @JsonProperty("INDEX")
    public ArrayList<String> iNDEX;
}

