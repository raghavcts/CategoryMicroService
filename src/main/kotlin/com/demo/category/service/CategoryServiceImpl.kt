package com.demo.category.service

import com.demo.category.api.CategoryApi
import com.demo.category.common.CategoryConstants
import com.demo.category.common.PriceFormatter
import com.demo.category.common.exception.CategoryNotFoundException
import com.demo.category.model.BasicColorsEnum
import com.demo.category.model.ColorSwatches
import com.demo.category.model.Product
import com.demo.category.model.Products
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class CategoryServiceImpl(
        private var categoryApi: CategoryApi) : CategoryService {
    @Value("\${category.api.key}")
    val apiKey: String = ""

    override fun getCategory(categoryId: String, labelType: String?): Products {
        val categoryReponse: JsonObject = categoryApi.getCategoryById(categoryId, apiKey)
        val products: Products? = parseCategoryResponse(categoryReponse, labelType)
        return products!!
    }

    private fun parseCategoryResponse(categoryReponse: JsonObject, labelType: String?): Products? {
        val productResponse: JsonArray = categoryReponse.getAsJsonArray("products")
        val prodcuctList: MutableList<Product>? = createProducts(productResponse, labelType)
        return Products(prodcuctList!!)
    }

    private fun createProducts(productResponse: JsonArray, labelType: String?): MutableList<Product> {
        val productList: MutableList<Product>? = arrayListOf()
        productResponse.map { ele ->
            val price: JsonElement? = ele.asJsonObject["price"]
            val currencyCode: String = price!!.asJsonObject["currency"].asString
            val nowPrice: String? = calculateNowPrice(price.asJsonObject["now"])
            val wasPrice: String? = when {
                price.asJsonObject["was"]!!.asString.isNullOrEmpty() -> CategoryConstants.DFAULT_ZERO_VAL
                else -> price.asJsonObject["was"].asString
            }
            val priceReduction = calculatePriceReduction(wasPrice, nowPrice)
            if (priceReduction > 0) {
                val title = ele.asJsonObject["title"].asString
                val productId = ele.asJsonObject["productId"].asString
                val colorSwatchesResp = ele.asJsonObject["colorSwatches"].asJsonArray
                val colorSwatches: MutableList<ColorSwatches> = createColorSwatches(colorSwatchesResp)
                val priceLabel: String? = calculatePricelabel(wasPrice, nowPrice,
                        price.asJsonObject["then1"], price.asJsonObject["then2"], currencyCode, labelType)
                val product = Product(productId, title, PriceFormatter.formatPriceLabel(nowPrice!!, currencyCode)!!, priceLabel!!, colorSwatches)
                product.maxReduction = priceReduction
                productList!!.add(product)
            }
        }
        productList!!.sortWith(Comparator { product1, product2 ->
            product2.maxReduction!!.roundToInt() - product1.maxReduction!!.roundToInt()
        })
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

    private fun calculatePriceReduction(was: String?, nowPrice: String?): Float {
        val wasFloat: Float = was!!.toFloat()
        val nowFloat: Float = nowPrice!!.toFloat()
        val maxReduction: Float = wasFloat - nowFloat
        if (maxReduction > 0) {
            return (maxReduction / wasFloat) * 100
        }
        return maxReduction
    }

    private fun calculatePricelabel(was: String?, now: String?, then1: JsonElement?, then2: JsonElement?, currencyCode:
    String, labelType: String?): String? {
        var priceLabel: String? = ""
        val wasPriceFmt: String? = PriceFormatter.formatPriceLabel(was, currencyCode)
        val thenPriceFmt: String? = PriceFormatter.formatPriceLabel(getThenPrice(then1, then2), currencyCode)
        val nowPriceFmt: String? = PriceFormatter.formatPriceLabel(now, currencyCode)

        when (labelType) {
            CategoryConstants.SHOW_WAS_TEHN_NOW -> {
                priceLabel = if (thenPriceFmt.equals(CategoryConstants.DFAULT_ZERO_VAL)) "was $wasPriceFmt,now $nowPriceFmt"
                else "was $wasPriceFmt,then $thenPriceFmt,now $nowPriceFmt"
            }
            CategoryConstants.SHOW_PERC_DISCOUNT -> {
                if (!was.equals(CategoryConstants.DFAULT_ZERO_VAL)) {
                    val discount: Float = ((was!!.toFloat() - now!!.toFloat()) / was.toFloat()) * 100
                    val disountString: String? = PriceFormatter.formatPriceLabel(discount.toString())
                    priceLabel = "$disountString% off - now $nowPriceFmt"
                }
            }
            CategoryConstants.SHOW_WAS_NOW -> priceLabel = "was $wasPriceFmt,now $nowPriceFmt"
            else -> throw CategoryNotFoundException(HttpStatus.NOT_FOUND.value(), "Invalid Price Label Input")

        }
        return priceLabel
    }

    private fun getThenPrice(then1: JsonElement?, then2: JsonElement?): String? {
        return when {
            !then1!!.asString.isNullOrEmpty() -> then1.asString
            !then2!!.asString.isNullOrEmpty() -> then2.asString
            else -> return ""
        }
    }

    private fun calculateNowPrice(now: JsonElement?) = when {
        now!!.isJsonObject -> {
            if (now.asJsonObject["from"].asString.isNullOrEmpty()) CategoryConstants.DFAULT_ZERO_VAL
            else now.asJsonObject["from"].asString
        }
        else -> {
            if (now.asString.isNullOrEmpty()) CategoryConstants.DFAULT_ZERO_VAL
            else now.asString
        }
    }
}