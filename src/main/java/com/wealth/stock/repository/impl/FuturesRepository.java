package com.wealth.stock.repository.impl;

import com.wealth.stock.bean.Futures;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FuturesRepository extends MongoRepository<Futures, String> {
    public long count();

    @Query("{strikePrice:?0,callOption:{$ne:null},putOption:{$ne:null}}")
    List<Futures> findAll2(PageRequest pageRequest);
}
