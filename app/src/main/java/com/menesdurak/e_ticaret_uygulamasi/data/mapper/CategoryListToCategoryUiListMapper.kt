package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.ListMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.CategoryUi

class CategoryListToCategoryUiListMapper(
) : ListMapper<String, CategoryUi> {
    override fun map(input: List<String>): List<CategoryUi> {
        return input.map {
            CategoryUi(
                name = it
            )
        }
    }
}