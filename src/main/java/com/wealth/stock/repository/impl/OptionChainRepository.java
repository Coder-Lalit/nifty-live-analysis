package com.wealth.stock.repository.impl;

import com.wealth.stock.bean.OCData;
import com.wealth.stock.bean.Option;
import com.wealth.stock.bean.OptionChain;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OptionChainRepository extends MongoRepository<OptionChain, String> {
    public long count();

    @Query("{strikePrice:?0,callOption:{$ne:null},putOption:{$ne:null}}")
    List<OptionChain> findByStrikePrice(int price);

    @Query("{strikePrice:?0,callOption:{$ne:null},putOption:{$ne:null}}")
    List<OptionChain> findByStrikePriceSort(int price, Sort sort);

    @Aggregation(pipeline = { "{$group: { _id: '', total: {$max: $strikePrice}}}"})
    public int max();

    @Aggregation(pipeline = { "{$group: { _id: '', total: {$min: $strikePrice}}}"})
    public int min();



}
