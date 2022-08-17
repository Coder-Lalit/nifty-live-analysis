package com.wealth.stock.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Candlestick {
    private Integer openTime;
    private Float open;
    private Float close;
}
