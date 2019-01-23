package com.demo.category.service

import com.demo.category.api.CategoryApi
import com.demo.category.common.CategoryNotFoundException
import com.demo.category.service.CategoryService
import com.demo.category.service.CategoryServiceImpl
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import org.springframework.util.ResourceUtils
import spock.lang.Specification

import static org.mockito.Mockito.*

class CategoryServiceImplTest extends Specification {

    CategoryService categoryService;
    CategoryApi categoryApi;
    JsonObject jsonObject;

    def setup() {
        categoryApi = mock(CategoryApi.class);
        buildJsonObject()
        categoryService = spy(new CategoryServiceImpl(categoryApi));
    }

    def buildJsonObject() {
        File file = ResourceUtils.getFile("classpath:categoryresponse.json")
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(file))
        jsonObject = gson.fromJson(reader, JsonObject.class);
    }

    def "calling getCategory with categoryId AND LabelType:ShowWasThenNow"() {
        given: "CategoryId and LableType"
        def categoryId = "600001506"
        def labelType = "ShowWasThenNow"
        //doReturn(jsonObject).when(categoryApi.getCategoryById(categoryId,"apiKey"))
        when(categoryApi.getCategoryById(categoryId, "")).thenReturn(jsonObject);
        when: "Getting expected products"
        def products = categoryService.getCategory(categoryId, labelType);
        then: "expecting that price label displays the was,then and now values"
        products.products.get(1).getPriceLabel().contains("then £68")
    }

    def "calling getCategory with categoryId AND LabelType:ShowWasNow"() {
        given: "CategoryId and LableType"
        def categoryId = "600001506"
        def labelType = "ShowWasNow"
        //doReturn(jsonObject).when(categoryApi.getCategoryById(categoryId,"apiKey"))
        when(categoryApi.getCategoryById(categoryId, "")).thenReturn(jsonObject);
        when: "Getting expected products"
        def products = categoryService.getCategory(categoryId, labelType);
        then: "expecting that price label displays the was and now values"
        !products.products.get(1).getPriceLabel().contains("then £68")
    }

    def "calling getCategory with categoryId AND LabelType:ShowPercDscount"() {
        given: "CategoryId and LableType"
        def categoryId = "600001506"
        def labelType = "ShowPercDscount"
        //doReturn(jsonObject).when(categoryApi.getCategoryById(categoryId,"apiKey"))
        when(categoryApi.getCategoryById(categoryId, "")).thenReturn(jsonObject);
        when: "Getting expected products"
        def products = categoryService.getCategory(categoryId, labelType);
        then: "expecting that price label displays the percentage value"
        products.products.get(0).getPriceLabel().contains("£1.32 off - now £99")
    }

}
