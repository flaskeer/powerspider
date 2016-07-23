package com.hao.spider.http;

import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.io.IOException;

/**
 * Created by donghao on 16/7/23.
 */
public class HttpClient {


    private Encoder encoder;
    private Decoder decoder;
    private Logger.ErrorLogger errorLogger = new Logger.ErrorLogger();
    private ClazzErrorDecoder errorDecoder = new ClazzErrorDecoder(decoder);

    public HttpClient(Encoder encoder, Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public HttpClient() {
        this.encoder = new GsonEncoder();
        this.decoder = new GsonDecoder();
    }

    public <T> T action(Class<T> clazz, String url) {
        T body = Feign.builder()
                .logger(errorLogger)
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .logLevel(Logger.Level.BASIC)
                .target(clazz,url);
        return body;
    }


    public static HttpClient getInstance() {
        return new HttpClient();
    }

    public static HttpClient getInstance(Encoder encoder,Decoder decoder) {
        return new HttpClient(encoder,decoder);
    }


    static class ClazzErrorDecoder implements ErrorDecoder {
        final Decoder decoder;
        final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

        ClazzErrorDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                return (Exception) decoder.decode(response,ClazzErrorDecoder.class);
            } catch (IOException e) {
                return defaultDecoder.decode(methodKey,response);
            }
        }
    }
}
