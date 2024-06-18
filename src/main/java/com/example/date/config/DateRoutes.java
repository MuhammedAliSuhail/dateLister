package com.example.date.config;

import com.example.date.service.DateService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DateRoutes extends RouteBuilder {
    private  final DateService dateService;

    @Autowired
    public DateRoutes(DateService dateService) {
        this.dateService = dateService;
    }
    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").port(9090).host("localhost").bindingMode(RestBindingMode.json);
        rest()
                .get("/hello")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .route()
                .routeId("helloRoute") // Set a route ID for easier identification
                .setBody(constant("Welcome"));

        from("timer:myTimer?period=1000")
                .routeId("route1")
                .log("Timer triggered")
                .to("log:output");

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
                .get("service/date?startDate={startDate}&endDate={endDate}")
                .outType(List.class)
                .to("direct:get-dates");
        from("direct:get-dates")
                .process(this::getDates);
    }

    private void getDates(Exchange exchange) {
        String startDate = exchange.getIn().getHeader("startDate", String.class);
        String endDate = exchange.getIn().getHeader("endDate", String.class);
        List<Date> dates = dateService.getDates(startDate, endDate);
        exchange.getIn().setBody(dates);
    }
}
