package com.wealth.stock.controller;

import com.wealth.stock.bean.Data;
import com.wealth.stock.bean.OCData;
import com.wealth.stock.bean.OptionChain;
import com.wealth.stock.bean.OptionData;
import com.wealth.stock.repository.impl.DailyDataRepository;
import com.wealth.stock.repository.impl.OptionChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;

@RestController
@Profile("grow")
public class optionGrowController {
    final String BaseURI_GROW = "https://groww.in/v1/api/option_chain_service/v1/option_chain/nifty";
    final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";

    @Autowired
    private OptionChainRepository optionChainRepository;

    @Scheduled(fixedDelay = 60000)
    public void getOptionDataFromGrow() {
        LocalDateTime dateTime = LocalDateTime.now();
        ZonedDateTime istTime = dateTime.atZone(ZoneId.of("Asia/Kolkata"));
        if ((istTime.getHour() >= 9 && istTime.getHour() < 16) && (istTime.getDayOfWeek().getValue()<6)) {
            if(istTime.getHour()==9 && istTime.getMinute()<=14){
                System.out.println("Not Now");
            }else if(istTime.getHour()==15 && istTime.getMinute()>=31){
                System.out.println("Not a valid time");
            }else{
                OptionData as = given().log().all()
                        .relaxedHTTPSValidation()
                        .accept("application/json")
                        .header("User-Agent", USER_AGENT)
                        .when()
                        .get(BaseURI_GROW)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .as(OptionData.class);

                as.optionChains.forEach(optionChain -> {
                    optionChain.timeStamp = new Date(istTime.getYear()-1900,istTime.getMonthValue()-1,istTime.getDayOfMonth()
                            ,istTime.getHour(),istTime.getMinute(),istTime.getSecond());
                    optionChainRepository.save(optionChain);
                });
                System.out.println("Pushed to mongoDB at :" + istTime);
            }
        }else{
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

}
