package com.wealth.stock.repository.impl;

import com.wealth.stock.bean.NIFTY100RSI;
import com.wealth.stock.bean.OCData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DailyRsiDataRepository extends MongoRepository<NIFTY100RSI, String> {
    public long count();

//    @Query("{}")
//    List<OCData> findByStrikePrice(int price);
//
//    @Query("{strikePrice:?0,pE:{$ne:null},cE:{$ne:null}}")
//    List<OCData> findByStrikePriceSort(int price, Sort sort);

}
