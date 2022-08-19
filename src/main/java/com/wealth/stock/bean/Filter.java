package com.wealth.stock.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Filter{
    public ListFilters listFilters;
    public String size;
    public String sortBy;
    public String sortType;
}