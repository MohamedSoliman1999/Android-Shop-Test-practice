package com.example.androidshoptest.ui.cartlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidshoptest.databinding.FragmentCartBinding
import com.example.androidshoptest.ui.adapter.CartItemAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding:FragmentCartBinding?=null
    private val binding get() = _binding!!
    private val cartListViewModel:CartListViewModel by viewModels()
    private val cartAdapter:CartItemAdapter by lazy{ CartItemAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
        initObservers()
    }
    private fun initView() {
        binding.rvShoppingItems.apply {
            this.adapter=cartAdapter
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
        binding.rvShoppingItems.layoutManager= LinearLayoutManager(requireContext())
    }
    private fun initListeners() {
        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToNewCartItemFragment())
        }
    }
    private fun initObservers(){
        cartListViewModel.cartItems.observe(viewLifecycleOwner){
            cartAdapter.submitList(it)
        }
        cartListViewModel.cartTotalPrice.observe(viewLifecycleOwner){
            binding.tvShoppingItemPrice.text="$it$"
        }
    }
    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = cartAdapter.currentList[pos]
            cartListViewModel.deleteCartItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    cartListViewModel.insertCartItem(item)
                }
                show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}