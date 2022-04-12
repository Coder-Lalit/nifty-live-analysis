package com.wealth.stock.repository.impl;

import com.wealth.stock.bean.OCData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DailyDataRepository extends MongoRepository<OCData, String> {
    public long count();

    @Query("{strikePrice:?0,pE:{$ne:null},cE:{$ne:null}}")
    List<OCData> findByStrikePrice(int price);

    @Query("{strikePrice:?0,pE:{$ne:null},cE:{$ne:null}}")
    List<OCData> findByStrikePriceSort(int price, Sort sort);

}
