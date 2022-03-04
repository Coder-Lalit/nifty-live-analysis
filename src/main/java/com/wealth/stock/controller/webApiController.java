package com.wealth.stock.controller;

import com.wealth.stock.bean.OCData;
import com.wealth.stock.repository.impl.DailyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
public class webApiController {

    @Autowired
    private DailyDataRepository dailyDataRepository;

    @GetMapping("/{price}")
    public String indexPage(Model model, @PathVariable int price){
        List<OCData> priceList = dailyDataRepository.findByStrikePrice(price);
        List<Double> pcr = new ArrayList<>();
        List<Double> cEPrice = new ArrayList<>();
        List<Double> pEPrice = new ArrayList<>();
        priceList.forEach(p->{
            pcr.add(p.pE.openInterest/p.cE.openInterest);
            cEPrice.add(p.cE.bidprice);
            pEPrice.add(p.pE.bidprice);
        });
        model.addAttribute("startTime",priceList.get(0).getTimeStamp());
        model.addAttribute("pcr",pcr);
        model.addAttribute("cEPrice",cEPrice);
        model.addAttribute("pEPrice",pEPrice);
        Collections.sort(priceList, new Comparator<OCData>() {
            @Override
            public int compare(OCData o1, OCData o2) {
                return o2.compareTo(o1);
            }
        });
        model.addAttribute("priceList",priceList);
        model.addAttribute("price",price);
        return "strikePrice";
    }

    @GetMapping("/graph/{price}")
    public String lineGraphPage(Model model, @PathVariable int price){
        List<OCData> priceList = dailyDataRepository.findByStrikePrice(price);
        List<Double> pcr = new ArrayList<>();
        List<Double> cEPrice = new ArrayList<>();
        List<Double> pEPrice = new ArrayList<>();
        priceList.forEach(p->{
            pcr.add(p.pE.openInterest/p.cE.openInterest);
            cEPrice.add(p.cE.bidprice);
            pEPrice.add(p.pE.bidprice);
        });
        model.addAttribute("startTime",priceList.get(0).timeStamp);
        model.addAttribute("pcr",pcr);
        model.addAttribute("cEPrice",cEPrice);
        model.addAttribute("pEPrice",pEPrice);
        return "basicLine";
    }

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping("/error")
    public String errorPage(){
        return "error";
    }
}
