package com.wealth.stock.controller;

import com.wealth.stock.bean.Data;
import com.wealth.stock.bean.OCData;
import com.wealth.stock.repository.impl.DailyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;


@RestController
public class Option {
    final String BaseURI = "https://www.nseindia.com/api/option-chain-indices?symbol=NIFTY";
    final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";

    @Autowired
    private DailyDataRepository dailyDataRepository;

    @Scheduled(fixedDelay = 60000)
    public void getOption() {
        LocalDateTime now = LocalDateTime.now();
        now.atOffset(ZoneOffset.ofHoursMinutes(5,30));
        if (now.getHour() >= 9 && now.getHour() < 16) {
            if(now.getHour()==9 && now.getMinute()<=14){
                System.out.println("Not Now");
            }else if(now.getHour()==15 && now.getMinute()>=31){
                System.out.println("Not Now");
            }else{
                Data as = given().log().all()
                        .relaxedHTTPSValidation()
                        .accept("application/json")
                        .header("User-Agent", USER_AGENT)
                        .when()
                        .get(BaseURI)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Data.class);
                Date myTimeStamp = (new Date());
                as.filtered.data.forEach(datum -> {
                    datum.timeStamp = myTimeStamp;
                    dailyDataRepository.save(datum);
                });
                System.out.println("Pushed to mongoDB at :" + myTimeStamp);
            }
        }else{
            System.out.println("Not now");
        }
    }

    @GetMapping("/api/stock/{price}")
    public List<OCData> getPrice(@PathVariable int price) {
        return dailyDataRepository.findByStrikePrice(price);
    }

    @GetMapping("/api/count")
    public long getCount() {
        return dailyDataRepository.count();

    }

}
