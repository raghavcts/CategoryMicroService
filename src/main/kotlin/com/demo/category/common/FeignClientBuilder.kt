package com.demo.category.common

import feign.Feign
import feign.Logger
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger


class FeignClientBuilder(private val apiHost: String) {

    fun <T> getFeignClient(apiClass: Class<T>): T {
        return Feign.builder()
                .client(OkHttpClient())
                .encoder(GsonEncoder())
                .decoder(GsonDecoder())
                .logger(Slf4jLogger(apiClass))
                .logLevel(Logger.Level.BASIC)
                .errorDecoder(CategoryApiErrorDecoder)
                .target(apiClass, apiHost)
    }
}

