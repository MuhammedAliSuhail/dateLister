package com.example.date.service;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DateService {
    public List<Date> getDates(String startDate, String endDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<Date> dates = new ArrayList<>();
        try{
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            System.out.println("Start Date : " + start);
            System.out.println("End Date : " + end);
            for (Date date = start; date.before(end); date = new Date(date.getTime() + 24 * 60 * 60 * 1000)) {
                System.out.println(date);
                dates.add(date);
            }
        } catch (Exception e) {
            System.out.println("There is an error");
            e.printStackTrace();
        }
        return dates;
    }
}
