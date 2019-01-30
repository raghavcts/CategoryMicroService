package com.demo.category.api

import com.demo.category.common.ApiConfiguration
import com.demo.category.common.FeignClientBuilder
import com.google.gson.JsonObject
import org.springframework.stereotype.Component


@Component
class CategoryApiImpl(private val feignClientBuilder: FeignClientBuilder, private val categoryApiConfiguration: ApiConfiguration) : CategoryApi {

    val categoryApi: CategoryApi by lazy { feignClientBuilder.getFeignClient(categoryApiConfiguration, CategoryApi::class.java) }
    override fun getCategoryById(categoryId: String): JsonObject {
        return categoryApi.getCategoryById(categoryId)
    }

}