package com.wealth.stock.controller;

import com.wealth.stock.bean.Futures;
import com.wealth.stock.bean.OCData;
import com.wealth.stock.bean.OptionChain;
import com.wealth.stock.bean.OptionData;
import com.wealth.stock.repository.impl.DailyDataRepository;
import com.wealth.stock.repository.impl.FuturesRepository;
import com.wealth.stock.repository.impl.OptionChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@Profile("grow")
public class webGrowApiController {

    @Autowired
    private OptionChainRepository optionChainRepository;

    @Autowired
    private FuturesRepository futuresRepository;

    @GetMapping("/{price}")
    public String indexPage(Model model, @PathVariable int price){
        List<OptionChain> priceList = optionChainRepository.findByStrikePrice(price*100);
        List<Double> pcr = new ArrayList<>();
        List<Double> cEPrice = new ArrayList<>();
        List<Double> pEPrice = new ArrayList<>();
        priceList.forEach(p->{
            pcr.add((double) (p.putOption.openInterest/p.callOption.openInterest));
            cEPrice.add(p.callOption.ltp);
            pEPrice.add(p.putOption.ltp);
        });
        model.addAttribute("startTime",priceList.get(0).timeStamp);
        model.addAttribute("pcr",pcr);
        model.addAttribute("cEPrice",cEPrice);
        model.addAttribute("pEPrice",pEPrice);
        model.addAttribute("pMin",optionChainRepository.min());
        model.addAttribute("pMax",optionChainRepository.max());
        Collections.sort(priceList, new Comparator<OptionChain>() {
            @Override
            public int compare(OptionChain o1, OptionChain o2) {
                return o2.compareTo(o1);
            }
        });
        model.addAttribute("priceList",priceList);
        model.addAttribute("price",price);
        model.addAttribute("NiftyList",futuresRepository.findAll(Pageable.ofSize(1)).getContent().get(0));
        return "strikePriceGrow";
    }

    @GetMapping("/")
    public String homePage(Model model){
        Futures nifty = (Futures) futuresRepository.findAll(Pageable.ofSize(1)).getContent().get(0);
        int ltp = (int) nifty.futureArrayList.stream()
                .filter(future -> future.contract.equalsIgnoreCase("nifty"))
                .findFirst().get().livePrice.ltp;
        ltp/=100;
        ltp = ltp%10 > 5 ?ltp*100+50:ltp*100;
        return indexPage(model,ltp);
    }

    @GetMapping("/error")
    public String errorPage(Model model){
        return "error";
    }
}
