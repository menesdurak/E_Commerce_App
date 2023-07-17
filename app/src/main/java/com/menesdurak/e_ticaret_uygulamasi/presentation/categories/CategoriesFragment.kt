package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentCategoriesBinding
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter(::onCategoryClick) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getAllCategories()

        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoryAdapter

        categoriesViewModel.categoriesList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    categoryAdapter.updateList(it.data)
                }

                is Resource.Error -> {

                }

                Resource.Loading -> {

                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onCategoryClick(categoryName: String) {
        Toast.makeText(requireContext(), categoryName, Toast.LENGTH_SHORT).show()
    }
}