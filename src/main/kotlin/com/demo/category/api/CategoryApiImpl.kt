package com.demo.category.api

import com.demo.category.common.FeignClientBuilder
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class CategoryApiImpl : CategoryApi {
    @Value("\${CATEGORY_API_HOST}")
    val apiHost: String = ""
    var categoryApi: CategoryApi? = null
    @Autowired
    fun CategoryApiImpl() {
        categoryApi = FeignClientBuilder(apiHost).getFeignClient(CategoryApi::class.java)
    }

    override fun getCategoryById(categoryId: String, apiKey: String): JsonObject {
        return categoryApi!!.getCategoryById(categoryId, apiKey)
    }

}