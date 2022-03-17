package com.example.androidshoptest.ui.gallery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.androidshoptest.databinding.FragmentGalleryBinding
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.ui.adapter.GalleryAdapter
import com.example.androidshoptest.ui.adapter.GalleryReactors
import com.example.androidshoptest.util.Constants.SEARCH_TIME_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class GalleryFragment : Fragment(), GalleryReactors {
    private var _binding: FragmentGalleryBinding?=null
    private val binding get() = _binding!!
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val adapter by lazy {GalleryAdapter(this)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    var job: Job? = null
    private fun initViews(){
        job?.cancel()
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                text?.let {
                    if(text.toString().isNotEmpty()) {
                        galleryViewModel.galleryIntent.send(GalleryIntent.SearchImage(text.toString()))
                    }
                }
            }
        }
        binding.rvImages.adapter=adapter
    }
    private fun initObservers(){
        lifecycleScope.launch {
            galleryViewModel.imageResponse.collectLatest {
                when(it){
                    is MainState.Idle->{

                    }
                    is MainState.Loading ->{
                        binding.progressBar.visibility=View.VISIBLE
                    }
                    is MainState.Success->{
                        binding.progressBar.visibility=View.GONE
                        adapter.submitList(it.data!!.hits.map { imageResult ->
                            imageResult.previewURL
                        })
                    }
                    is MainState.Error ->{
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(requireContext(), "${it.throwable!!.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onGalleryItemClicked(url: String) {
        lifecycleScope.launch {
            galleryViewModel.selectedImageUrl.postValue(url)
            findNavController().popBackStack()
        }
    }
}