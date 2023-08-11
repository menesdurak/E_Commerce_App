package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.CubeTransformer
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentHomeDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeDetailFragment : Fragment() {
    private var _binding: FragmentHomeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewPagerAdapter: HomeDetailViewPagerAdapter by lazy {
        HomeDetailViewPagerAdapter()
    }

    private var adNumber = 1

    private var autoSwipeScope : Job? = null

    private lateinit var pagerAnimation : ObjectAnimator

    private val animationDuration = 4000L

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
            R.drawable.home1,
            R.drawable.home2,
            R.drawable.home3,
            R.drawable.home4,
            R.drawable.home5
        )

        binding.progressBar.max = 10000

        val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        // attach scroll listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView, dx: Int, dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                binding.progressBar.progress = 0
            }
        })

        with(binding.viewPager){
            viewPagerAdapter.updateList(imageList)
            this.adapter = viewPagerAdapter
            this.setPageTransformer(CubeTransformer())
            //Home Fragment's view pager have one more extra item at the start
            //So I decreased adNumber 1
            this.setCurrentItem(adNumber - 1, false)
            autoSwipeScope = CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {
                    pagerAnimation = ObjectAnimator.ofInt(
                        binding.progressBar,
                        "progress",
                        binding.progressBar.progress,
                        binding.progressBar.max
                    )
                    pagerAnimation.duration = animationDuration
                    pagerAnimation.interpolator = DecelerateInterpolator()
                    pagerAnimation.start()
                    delay(animationDuration)
                    if (binding.viewPager.currentItem + 1 > imageList.size - 1) {
                        findNavController().popBackStack()
                    } else {
                        binding.viewPager.currentItem++
                        binding.progressBar.progress = 0
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        autoSwipeScope?.cancel()
        autoSwipeScope = null
    }
}