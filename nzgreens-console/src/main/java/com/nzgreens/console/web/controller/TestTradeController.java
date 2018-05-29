package com.nzgreens.console.web.controller;

import com.nzgreens.console.mt4.api.ConnectorAPI;
import com.nzgreens.console.mt4.api.MT4;
import com.nzgreens.console.mt4.api.bean.TradeRecord;
import com.nzgreens.common.model.ResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/26 14:14
 */
@Controller
@RequestMapping("trade/history")
public class TestTradeController extends BaseController {

    @RequestMapping("list")
    @ResponseBody
    public ResultModel selectTradeHistory() throws Exception{
        ResultModel result = new ResultModel();
        ConnectorAPI mt4 = new MT4();
        mt4.connect("mtdemo1.leverate.com:443");
        if(mt4.isConnected()){
            System.out.println("connected");
        }

        int res = mt4.login(31502, "foryou9980");

        long from = mt4.serverTime()- 60 * 60 * 24 *80;
        System.out.println("from="+from);
        long to = mt4.serverTime() ;
        TradeRecord[] records =  mt4.getTradesUserHistory(611726007, from, to);

        for(TradeRecord record : records){

            System.out.println("record="+record);

        }
        System.out.println("records="+records.length);
        result.setData(records);
        return result;
    }

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        Date start = new Date(currentTime - 24 * 60 * 60 * 1000);
        Date end = new Date(currentTime);
        System.out.println(start);
        System.out.println(end);
    }
}
