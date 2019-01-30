package com.demo.category.service

import com.demo.category.common.exception.CategoryException
import com.demo.category.common.exception.CategoryNotFoundException
import com.demo.category.model.Products

interface CategoryService {
    @Throws(CategoryException::class, CategoryNotFoundException::class)
    fun getCategory(categoryId: String, labelType: String?): Products
}