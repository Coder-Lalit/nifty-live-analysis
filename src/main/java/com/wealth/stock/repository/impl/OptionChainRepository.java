package com.wealth.stock.repository.impl;

import com.wealth.stock.bean.OptionChain;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OptionChainRepository extends MongoRepository<OptionChain, String> {
    public long count();

    @Query("{strikePrice:?0,callOption:{$ne:null},putOption:{$ne:null}}")
    List<OptionChain> findByStrikePrice(int price);

    @Query("{strikePrice:?0,callOption:{$ne:null},putOption:{$ne:null}}")
    List<OptionChain> findByStrikePrice(int price, PageRequest pageRequest);

    @Aggregation(pipeline = { "{$group: { _id: '', total: {$max: $strikePrice}}}"})
    public int max();

    @Aggregation(pipeline = { "{$group: { _id: '', total: {$min: $strikePrice}}}"})
    public int min();




}
