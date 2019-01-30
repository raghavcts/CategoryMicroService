package com.demo.category.util

import com.demo.category.common.CategoryConstants
import com.demo.category.common.exception.CategoryNotFoundException
import com.demo.category.model.Product
import com.google.gson.JsonElement
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.math.roundToInt

object ProductFormatter {
    fun formatPriceLabel(price: String?, currencyCode: String = "GBP"): String? {
        val currency = Currency.getInstance(currencyCode)
        val symbol = currency.symbol
        var priceLablefmt: String? = "0.00"
        if (!price.isNullOrEmpty()) {
            val priceFloat: Float = price?.toFloat() ?: 0.00f
            priceLablefmt = if (priceFloat > 10)
                symbol + priceFloat.toInt()
            else """$symbol${"%.2f".format(price!!.toBigDecimal())}"""
        }
        return priceLablefmt
    }

    fun calculatePriceReduction(was: String?, nowPrice: String?): Float {
        val wasFloat: Float = was!!.toFloat()
        val nowFloat: Float = nowPrice!!.toFloat()
        val maxReduction: Float = wasFloat - nowFloat
        if (maxReduction > 0) {
            return (maxReduction / wasFloat) * 100
        }
        return maxReduction
    }

    fun calculatePricelabel(was: String?, now: String?, then1: JsonElement?, then2: JsonElement?, currencyCode:
    String, labelType: String?): String? {
        var priceLabel: String? = ""
        val wasPriceFmt: String? = formatPriceLabel(was, currencyCode)
        val thenPriceFmt: String? = formatPriceLabel(getThenPrice(then1, then2), currencyCode)
        val nowPriceFmt: String? = formatPriceLabel(now, currencyCode)

        when (labelType) {
            CategoryConstants.SHOW_WAS_TEHN_NOW -> {
                priceLabel = if (thenPriceFmt.equals(CategoryConstants.DFAULT_ZERO_VAL)) "was $wasPriceFmt,now $nowPriceFmt"
                else "was $wasPriceFmt,then $thenPriceFmt,now $nowPriceFmt"
            }
            CategoryConstants.SHOW_PERC_DISCOUNT -> {
                if (!was.equals(CategoryConstants.DFAULT_ZERO_VAL)) {
                    val discount: Float = ((was!!.toFloat() - now!!.toFloat()) / was.toFloat()) * 100
                    val disountString: String? = formatPriceLabel(discount.toString())
                    priceLabel = "$disountString% off - now $nowPriceFmt"
                }
            }
            CategoryConstants.SHOW_WAS_NOW -> priceLabel = "was $wasPriceFmt,now $nowPriceFmt"
            else -> throw CategoryNotFoundException(HttpStatus.NOT_FOUND.value(), "Invalid Price Label Input")

        }
        return priceLabel
    }

    fun getThenPrice(then1: JsonElement?, then2: JsonElement?): String? {
        return when {
            !then1!!.asString.isNullOrEmpty() -> then1.asString
            !then2!!.asString.isNullOrEmpty() -> then2.asString
            else -> return ""
        }
    }

    fun calculateNowPrice(now: JsonElement?) = when {
        now!!.isJsonObject -> {
            if (now.asJsonObject["from"].asString.isNullOrEmpty()) CategoryConstants.DFAULT_ZERO_VAL
            else now.asJsonObject["from"].asString
        }
        else -> {
            if (now.asString.isNullOrEmpty()) CategoryConstants.DFAULT_ZERO_VAL
            else now.asString
        }
    }

    fun sortProducts(productList: MutableList<Product>?) {
        productList!!.sortWith(Comparator { product1, product2 ->
            product2.maxReduction!!.roundToInt() - product1.maxReduction!!.roundToInt()
        })
    }

}