package com.demo.category.controller

import com.demo.category.common.CategoryException
import com.demo.category.common.CategoryNotFoundException
import com.demo.category.model.Products
import com.demo.category.service.CategoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController(@Autowired
                         private var categoryService: CategoryService) {

    private val log = LoggerFactory.getLogger(CategoryController::class.java)

    @GetMapping("/category/{categoryId}", produces = ["application/json"])
    fun getProductsBycategoryId(@PathVariable("categoryId") categoryId: String
                                , @RequestParam("labelType", required = false, defaultValue = "ShowWasNow")
                                labelType: String): ResponseEntity<Any> {
        return try {
            val products: Products? = categoryService.getCategory(categoryId, labelType)
            ResponseEntity.ok(products!!)
        } catch (e: CategoryException) {
            log.error("Internal Error Occured")
            ResponseEntity.ok(Products(arrayListOf()))
        } catch (e: CategoryNotFoundException) {
            log.error("No data found for selected category")
            val map: HashMap<String, String> = hashMapOf("Status" to e.responseStatus)
            ResponseEntity.ok(map)
        } catch (e: Exception) {
            log.error("Unknown Error Occured")
            ResponseEntity.ok(Products(arrayListOf()))
        }
    }
}