package com.wealth.stock.controller.Indicator;

import com.wealth.stock.bean.Candlestick;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RSI {
    public double calcSmmaUp(Candlestick[] candlesticks, double n, int i, double avgUt1) {

        if (avgUt1 == 0) {
            double sumUpChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks[i - j].getClose() - candlesticks[i - j].getOpen();

                if (change > 0) {
                    sumUpChanges += change;
                }
            }
            return sumUpChanges / n;
        } else {
            double change = candlesticks[i].getClose() - candlesticks[i].getOpen();
            if (change < 0) {
                change = 0;
            }
            return ((avgUt1 * (n - 1)) + change) / n;
        }

    }

    public double calcSmmaDown(Candlestick[] candlesticks, double n, int i, double avgDt1) {
        if (avgDt1 == 0) {
            double sumDownChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks[i - j].getClose() - candlesticks[i - j].getOpen();

                if (change < 0) {
                    sumDownChanges -= change;
                }
            }
            return sumDownChanges / n;
        } else {
            double change = candlesticks[i].getClose() - candlesticks[i].getOpen();
            if (change > 0) {
                change = 0;
            }
            return ((avgDt1 * (n - 1)) - change) / n;
        }

    }

    public List<List<Integer>>  calculateRSIValues(Candlestick[] candlesticks, int n) {

        List<List<Integer>> res = new ArrayList<>();
        double ut1 = 0;
        double dt1 = 0;
        for (int i = 0; i < candlesticks.length; i++) {
            if (i < (n)) {
                continue;
            }
            List<Integer> t = new ArrayList<>();

            ut1 = calcSmmaUp(candlesticks, n, i, ut1);
            dt1 = calcSmmaDown(candlesticks, n, i, dt1);
            t.add(candlesticks[i].getOpenTime());
            t.add((int) (100.0 - 100.0 / (1.0 + (ut1/dt1))));
            res.add(t);
        }

        return res;
    }

}
