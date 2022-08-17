package com.wealth.stock.controller;

import com.wealth.stock.bean.*;
import com.wealth.stock.controller.Indicator.RSI;
import com.wealth.stock.repository.impl.FuturesRepository;
import com.wealth.stock.repository.impl.OptionChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@RestController
@Profile("grow")
public class optionGrowController {
    final String BaseURI_GROW = "https://groww.in/v1/api/option_chain_service/v1/option_chain/derivatives/nifty";

    final String GET_HISTORICAL_DATA ="https://groww.in/v1/api/charting_service/v2/chart/exchange/NSE/segment/CASH/NIFTY?endTimeInMillis=%s&intervalInMinutes=%s&startTimeInMillis=%s";
    final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";

    @Autowired
    private OptionChainRepository optionChainRepository;

    @Autowired
    private FuturesRepository futuresRepository;

    @Autowired
    private RSI rsi;

    @Scheduled(fixedDelay = 60000)
    public void getOptionDataFromGrow() {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(new Date());
        ZonedDateTime istTime = dateTime.atZone(ZoneId.of("Asia/Kolkata"));
        System.out.println(istTime.getHour() + " : " + istTime.getMinute());
        if ((istTime.getHour() >= 9 && istTime.getHour() < 16) && (istTime.getDayOfWeek().getValue() < 6)) {
            if (istTime.getHour() == 9 && istTime.getMinute() <= 14) {
                System.out.println("Not Now");
            } else if (istTime.getHour() == 15 && istTime.getMinute() >= 31) {
                System.out.println("Not a valid time");
            } else {
                System.out.println("Requesting the service for Data");
                NiftyData as = given()
                        .relaxedHTTPSValidation()
                        .accept("application/json")
                        .header("User-Agent", USER_AGENT)
                        .when()
                        .get(BaseURI_GROW)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(NiftyData.class);
                System.out.println("Got the Data from Service");
                Date date = new Date(istTime.getYear() - 1900, istTime.getMonthValue() - 1, istTime.getDayOfMonth()
                        , istTime.getHour(), istTime.getMinute(), istTime.getSecond());
                as.optionChain.optionChains.forEach(optionChain -> {
                    optionChain.timeStamp = date;
                    if (optionChain.putOption != null && optionChain.callOption != null)
                        optionChain.pcr = (double) optionChain.putOption.openInterest / optionChain.callOption.openInterest;
                    optionChainRepository.save(optionChain);
                });
                int sumOfPE_OI = as.optionChain.optionChains.stream()
                        .filter(x -> (x.putOption!=null && x.callOption!=null))
                        .mapToInt(i -> i.putOption.openInterest).sum();
                int sumOfCE_OI = as.optionChain.optionChains.stream()
                        .filter(x -> (x.putOption!=null && x.callOption!=null))
                        .mapToInt(i -> i.callOption.openInterest).sum();

                Future future = new Future();
                future.livePrice = as.livePrice;
                future.livePrice.ltp = as.livePrice.value;
                future.contract = "Nifty";
                future.expiry = "NA";
                as.futures.add(future);
                Futures temp = new Futures(date, as.futures);
                temp.currentPrice=future.livePrice.value;
                temp.ceOI=sumOfCE_OI;
                temp.peOI=sumOfPE_OI;
                temp.pcr = (double) sumOfPE_OI/sumOfCE_OI;
                futuresRepository.save(temp);
                System.out.println("Nifty current price :"+temp.currentPrice + " with pcr :"+temp.pcr );
                System.out.println("Pushed to mongoDB at :" + istTime);
            }
        } else {
            System.out.println("Not a valid time");
        }
    }

    @GetMapping("/api/stock/{price}")
    public List<OptionChain> getPrice(@PathVariable int price) {
        return optionChainRepository.findByStrikePrice(price);
    }

    @GetMapping("/api/count")
    public long getCount() {
        return optionChainRepository.count();

    }

    @GetMapping("/rsi/{mins}")
    public List<List<Integer>> getRSI(@PathVariable String mins){
        ArrayList<List<Number>>  as = given()
                .relaxedHTTPSValidation()
                .accept("application/json")
                .header("User-Agent", USER_AGENT)
                .when()
                .get(String.format(GET_HISTORICAL_DATA, Instant.now().getEpochSecond()*1000,mins, Instant.now().getEpochSecond()*1000-2592000000L))
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("candles");

        List<Candlestick> candlestickList = as.stream().map(a ->
                Candlestick.builder()
                        .openTime((Integer) a.get(0))
                        .open((Float) a.get(1))
                        .close((Float) a.get(4))
                        .build()
        ).collect(Collectors.toList());
        Candlestick[] candlesticks = new Candlestick[candlestickList.size()];
        for(int i =0; i<candlestickList.size();i++)
            candlesticks[i]=candlestickList.get(i);
        return rsi.calculateRSIValues(candlesticks, 14);



    }
}
