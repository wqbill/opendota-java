package com.wqbill.opendota.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class Web {
    @RequestMapping("/apps")
    public void apps(String originalUrl, HttpServletResponse httpServletResponse) throws IOException {
        String url = "http://cdn.dota2.com/" + originalUrl;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        IOUtils.copy(response.body().byteStream(), httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }

    @RequestMapping("/ugc")
    public void ugc(String originalUrl, HttpServletResponse httpServletResponse) throws IOException {
        String url = "http://cloud-3.steamusercontent.com/" + originalUrl;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if ("image/png".equals(response.header("content-type"))) {
            IOUtils.copy(response.body().byteStream(), httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        }
    }

    @RequestMapping("/healthz")
    public String healthz() {
        return "ok";
    }
}
