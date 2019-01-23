package com.demo.category.service

import com.demo.category.common.CategoryException
import com.demo.category.common.CategoryNotFoundException
import com.demo.category.model.Products

interface CategoryService {
    @Throws(CategoryException::class, CategoryNotFoundException::class)
    fun getCategory(categoryId: String, labelType: String?): Products
}