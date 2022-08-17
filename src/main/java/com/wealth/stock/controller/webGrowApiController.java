package com.wealth.stock.controller;

import com.wealth.stock.bean.Futures;
import com.wealth.stock.bean.OptionChain;
import com.wealth.stock.repository.impl.FuturesRepository;
import com.wealth.stock.repository.impl.OptionChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@Profile("grow")
public class webGrowApiController {

    @Autowired
    private OptionChainRepository optionChainRepository;

    @Autowired
    private FuturesRepository futuresRepository;

    @Autowired
    private optionGrowController optionGrowController;

    @GetMapping("/{price}")
    public String indexPage(Model model, @PathVariable int price){
        List<OptionChain> priceList = optionChainRepository
                .findByStrikePrice(price*100,PageRequest.of(0, 120, Sort.by(Sort.Direction.DESC, "timeStamp")));
        SortedMap<Integer,List<Double>> map = new TreeMap<>();
        List<Double> pcr = new ArrayList<>();
        List<Double> cEPrice = new ArrayList<>();
        List<Double> pEPrice = new ArrayList<>();
        Collections.reverse(priceList);
        for(int i=0;i<7;i++){
            List<OptionChain> priceList2 = optionChainRepository
                    .findByStrikePrice(((price-150)+i*50)*100,PageRequest.of(0, 120, Sort.by(Sort.Direction.DESC, "timeStamp")));
            Collections.reverse(priceList2);
            List<Double> tempPcr = new ArrayList<>();
            priceList2.forEach(p->{
                tempPcr.add(p.pcr);
            });
            map.put((price-150)+i*50,tempPcr);
        }

        priceList.forEach(p->{
            pcr.add(p.pcr);
            cEPrice.add(p.callOption.ltp);
            pEPrice.add(p.putOption.ltp);
        });
        model.addAttribute("startTime",priceList.get(0).timeStamp);
        Collections.reverse(priceList);
        model.addAttribute("priceList",priceList);
        model.addAttribute("pcr",pcr);
        model.addAttribute("pcrMap",map);
        model.addAttribute("cEPrice",cEPrice);
        model.addAttribute("pEPrice",pEPrice);
        model.addAttribute("pMin",optionChainRepository.min());
        model.addAttribute("pMax",optionChainRepository.max());
        model.addAttribute("price",price);
        model.addAttribute("NiftyList",futuresRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "timeStamp"))).getContent().get(0));
        model.addAttribute("NiftyPCR",futuresRepository.findAll2(PageRequest.of(0, 120, Sort.by(Sort.Direction.DESC, "timeStamp"))));
        return "strikePriceGrow";
    }

    @GetMapping("/")
    public String homePage(Model model){
        Futures nifty = (Futures) futuresRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "timeStamp"))).getContent().get(0);
        int ltp = (int) nifty.futureArrayList.stream()
                .filter(future -> future.contract.equalsIgnoreCase("nifty"))
                .findFirst().get().livePrice.ltp;
        ltp/=10;
        ltp = ltp%10 >= 5 ?(ltp/10)*100+50:(ltp/10)*100;
        return indexPage(model,ltp);
    }

    @GetMapping("/error")
    public String errorPage(Model model){
        return "error";
    }

    @GetMapping("/rsi")
    public String getRSI(Model model){
        int x=5;
        List<List<Integer>> rsi5 = optionGrowController.getRSI("5");
        List<List<Integer>> rsi15 = optionGrowController.getRSI("15");
        List<List<Integer>> rsi60 = optionGrowController.getRSI("60");

        model.addAttribute("fiveMins",rsi5.stream().skip(rsi5.size()-x*12).collect(Collectors.toList()));
        model.addAttribute("fifteenMins",rsi15.stream().skip(rsi15.size()-x*4).collect(Collectors.toList()));
        model.addAttribute("sixtyMins",rsi60.stream().skip(rsi60.size()-x).collect(Collectors.toList()));


        return "rsi";
    }
}
