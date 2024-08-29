package com.hun.market.core.web;

import java.io.IOException;
import java.io.Writer;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;

public class CustomErrorRepValve extends ErrorReportValve {
    @Override
    protected void report(Request request, Response response, Throwable throwable) {
        if  (!response.setErrorReported())
            return;
        try {
            response.setContentType("text/html;charset=UTF-8");
            Writer writer = response.getReporter();
            writer.write("<h1>400 Fatal error.  Could not process request. /h1>");
            response.finishResponse();
        } catch (IOException e) {
        }
    }
}
