package com.example.androidshoptest.ui.addcart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.example.androidshoptest.R
import com.example.androidshoptest.databinding.FragmentNewCartItemBinding
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.ui.gallery.GalleryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewCartItemFragment : Fragment() {
    private var _binding: FragmentNewCartItemBinding? = null
    private val binding get() = _binding!!
    lateinit var galleryViewModel: GalleryViewModel //by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        _binding = FragmentNewCartItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            setFragmentResultListener("selected_image") { requestKey, bundle ->
                // We use a String here, but any type that can be put in a Bundle is supported
                val result = bundle.getString("bundle_selected_image")
                binding.ivShoppingImage.load(result) {
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_add_to_photos_24)
                    error(R.drawable.ic_baseline_add_to_photos_24)
                }
                // Do something with the result
            }
//            galleryViewModel.selectedImageUrl.observe(viewLifecycleOwner) {
//                if(!it.isNullOrEmpty()){
//                    binding.ivShoppingImage.load(it) {
//                        crossfade(true)
//                        placeholder(R.drawable.ic_baseline_add_to_photos_24)
//                        error(R.drawable.ic_baseline_add_to_photos_24)
//                    }
//                }
//            }
        }
        galleryViewModel.insertShoppingItem.observe(viewLifecycleOwner) {
            it.contentIfHandled()?.let { result ->
                when (result) {
                    is MainState.Success -> {
                        Snackbar.make(
                            activity!!.findViewById(android.R.id.content),
                            "Shopping item Added",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    is MainState.Loading -> {
                        // NO OP
                    }
                    is MainState.Error -> {
                        Snackbar.make(
                            activity!!.findViewById(android.R.id.content),
                            result.throwable?.message ?: "A Error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is MainState.Idle -> TODO()
                }
            }

        }
    }

    private fun initListeners() {
        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(NewCartItemFragmentDirections.actionNewCartItemFragmentToGalleryFragment())
        }
        binding.btnAddShoppingItem.setOnClickListener {
            galleryViewModel.validateShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.etShoppingItemAmount.text.toString(),
                binding.etShoppingItemPrice.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}