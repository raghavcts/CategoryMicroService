package com.demo.category.service

import com.demo.category.api.CategoryApi
import com.demo.category.common.CategoryConstants
import com.demo.category.model.BasicColorsEnum
import com.demo.category.model.ColorSwatches
import com.demo.category.model.Product
import com.demo.category.model.Products
import com.demo.category.util.ProductFormatter
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
        private var categoryApi: CategoryApi) : CategoryService {

    override fun getCategory(categoryId: String, labelType: String?): Products {
        val categoryReponse: JsonObject = categoryApi.getCategoryById(categoryId).execute()
        val products: Products? = parseCategoryResponse(categoryReponse, labelType)
        return products!!
    }

    private fun parseCategoryResponse(categoryReponse: JsonObject, labelType: String?): Products? {
        val productResponse: JsonArray = categoryReponse.getAsJsonArray("products")
        val prodcuctList: MutableList<Product>? = createProducts(productResponse, labelType)
        return Products(prodcuctList!!)
    }

    private fun createProducts(productResponse: JsonArray, labelType: String?): MutableList<Product>? {
        val productList: MutableList<Product>? = arrayListOf()
        productResponse.map { ele ->
            val price: JsonElement? = ele.asJsonObject["price"]
            val currencyCode: String = price!!.asJsonObject["currency"].asString
            val nowPrice: String? = ProductFormatter.calculateNowPrice(price.asJsonObject["now"])
            val wasPrice: String? = when {
                price.asJsonObject["was"]!!.asString.isNullOrEmpty() -> CategoryConstants.DFAULT_ZERO_VAL
                else -> price.asJsonObject["was"].asString
            }
            val priceReduction = ProductFormatter.calculatePriceReduction(wasPrice, nowPrice)
            if (priceReduction > 0) {
                val title = ele.asJsonObject["title"].asString
                val productId = ele.asJsonObject["productId"].asString
                val colorSwatchesResp = ele.asJsonObject["colorSwatches"].asJsonArray
                val colorSwatches: MutableList<ColorSwatches> = createColorSwatches(colorSwatchesResp)
                val priceLabel: String? = ProductFormatter.calculatePricelabel(wasPrice, nowPrice,
                        price.asJsonObject["then1"], price.asJsonObject["then2"], currencyCode, labelType)
                val product = Product(productId, title, ProductFormatter.formatPriceLabel(nowPrice!!, currencyCode)!!,
                        priceLabel!!, colorSwatches)
                product.maxReduction = priceReduction
                productList!!.add(product)
            }
        }
        ProductFormatter.sortProducts(productList)
        return productList
    }

    private fun createColorSwatches(colorSwatchesResp: JsonArray?): MutableList<ColorSwatches> {
        val colorSwatches: MutableList<ColorSwatches> = arrayListOf()
        colorSwatchesResp?.map { col ->
            val basicColor = col.asJsonObject["basicColor"].asString.toUpperCase()
            colorSwatches.add(ColorSwatches(col.asJsonObject["color"].asString,
                    BasicColorsEnum.valueOf(basicColor).hexaCode,
                    col.asJsonObject["skuId"].asString))
        }
        return colorSwatches
    }


}