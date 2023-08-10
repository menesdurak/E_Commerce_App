package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.CubeTransformer
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentHomeDetailBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.product_detail.ProductDetailFragmentArgs

class HomeDetailFragment : Fragment() {
    private var _binding: FragmentHomeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewPagerAdapter: HomeDetailViewPagerAdapter by lazy {
        HomeDetailViewPagerAdapter()
    }

    private var adNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving clicked character ID and location ID
        val args: HomeDetailFragmentArgs by navArgs()
        adNumber = args.adNumber

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = listOf(
            R.drawable.home5,
            R.drawable.home1,
            R.drawable.home2,
            R.drawable.home3,
            R.drawable.home4,
            R.drawable.home5,
            R.drawable.home1
        )

        viewPagerAdapter.updateList(imageList)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.setPageTransformer(CubeTransformer())

        binding.viewPager.setCurrentItem(adNumber, false)

        val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val itemCount = binding.viewPager.adapter?.itemCount ?: 0
        // attach scroll listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView, dx: Int, dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
                val lastItemVisible = layoutManager.findLastVisibleItemPosition()
                if (firstItemVisible == (itemCount - 1) && dx > 0) {
                    recyclerView.scrollToPosition(1)
                } else if (lastItemVisible == 0 && dx < 0) {
                    recyclerView.scrollToPosition(itemCount - 2)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}