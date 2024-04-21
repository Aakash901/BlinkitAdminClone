package com.example.admindiyabatti.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admindiyabatti.R
import com.example.admindiyabatti.utils.Utils
import com.example.admindiyabatti.adapter.AdapterCartProducts
import com.example.admindiyabatti.databinding.FragmentOrderDeatailBinding
import com.example.admindiyabatti.viewmodels.AdminViewModel
import kotlinx.coroutines.launch


class OrderDeatailFragment : Fragment() {

    private lateinit var binding: FragmentOrderDeatailBinding
    private lateinit var adapterCartProducts: AdapterCartProducts
    private val viewmodel: AdminViewModel by viewModels()
    private var status = 0
    private var currentStatus = 0
    private var orderId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDeatailBinding.inflate(layoutInflater)

        getValues()
        settingStatus(status)
        onBackButtonClicked()
        onChangeStatusBtnClicked()
        lifecycleScope.launch {
            getOrderedProducts()
        }
        return binding.root
    }

    private fun onChangeStatusBtnClicked() {
        binding.btnChangeStatus.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.menuRecived -> {
                        currentStatus = 1
                        if (currentStatus > status) {
                            status = 1
                            settingStatus(1)
                            viewmodel.updateOrderStatus(orderId, 1)
                            lifecycleScope.launch {

                                viewmodel.sendNotification(orderId,"Received","Your order is received")
                            }
                        } else {
                            Utils.showToast(requireContext(), "Order is already received...")
                        }

                        true
                    }

                    R.id.menuDispatched -> {
                        currentStatus = 2
                        if (currentStatus > status) {
                            status = 2
                            settingStatus(2)
                            viewmodel.updateOrderStatus(orderId, 2)
                            lifecycleScope.launch {

                                viewmodel.sendNotification(orderId,"Dispatched","Your order is dispatched")
                            }
                        } else {
                            Utils.showToast(requireContext(), "Order is already dispatched...")
                        }
                        true

                    }

                    R.id.menuDelivered -> {
                        currentStatus = 3
                        if (currentStatus > status) {
                            status = 3
                            settingStatus(3)
                            viewmodel.updateOrderStatus(orderId, 3)
                            lifecycleScope.launch {

                                viewmodel.sendNotification(orderId,"Delivered","Your order is delivered")
                            }
                        } else {
                            Utils.showToast(requireContext(), "Order is already delivered...")
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDeatailFragment_to_orderFragment)
        }
    }


    suspend private fun getOrderedProducts() {
        viewmodel.getOrderedProducts(orderId).collect { cartProductList ->

            adapterCartProducts = AdapterCartProducts()
            binding.rvProductItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

        }
    }

    private fun settingStatus(status: Int) {
        val statusToViews = mapOf(
            0 to listOf(binding.iv1),
            1 to listOf(binding.iv1, binding.iv2, binding.view1),
            2 to listOf(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2),
            3 to listOf(
                binding.iv1,
                binding.iv2,
                binding.view1,
                binding.iv3,
                binding.view2,
                binding.iv4,
                binding.view3
            )
        )

        val viewsToTint = statusToViews.getOrDefault(status, emptyList())
        for (view in viewsToTint) {
            view.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.blue)
        }
    }

    private fun getValues() {
        val bundle = arguments
        status = bundle?.getInt("status")!!
        orderId = bundle.getString("orderId").toString()
        binding.tvUserAddress.text = bundle.getString("userAddress").toString()


    }


}