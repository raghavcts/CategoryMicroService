package com.demo.category.common

import java.util.*

object PriceFormatter {

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

}