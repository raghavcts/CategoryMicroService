package com.demo.category.common

import feign.Feign
import feign.Logger
import feign.Request
import feign.Retryer
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Component

@Component
class FeignClientBuilder(private val apiTimeoutProperties: APITimeoutProperties) {

    fun <T> getFeignClient(categoryApiConfiguration: ApiConfiguration, apiClass: Class<T>): T {
        return Feign.builder()
                .options(Request.Options(apiTimeoutProperties.connectMillis.toInt(),
                        apiTimeoutProperties.readMillis.toInt()))
                .client(OkHttpClient())
                .encoder(GsonEncoder())
                .decoder(GsonDecoder())
                .logger(Slf4jLogger(apiClass))
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)
                .errorDecoder(CategoryApiErrorDecoder)
                .target(apiClass, categoryApiConfiguration.apiHost)
    }
}

