package com.example.admindiyabatti.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admindiyabatti.R
import com.example.admindiyabatti.adapter.AdapterOrders
import com.example.admindiyabatti.databinding.FragmentOrderBinding
import com.example.admindiyabatti.models.OrderItems
import com.example.admindiyabatti.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewmodel: AdminViewModel by viewModels()
    private lateinit var adapterOrder: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        getAllOrders()
        onBackPressed()
        return binding.root
    }

    private fun onBackPressed() {
        binding.tbSearchFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_homeFragment)
        }
    }

    private fun getAllOrders() {
        binding.shimmerViewContainerOrders.visibility = View.VISIBLE
        binding.tvText.visibility = View.GONE
        lifecycleScope.launch {
            viewmodel.getAllOrders().collect { orderList ->
                if (orderList.isNotEmpty()) {
                    val orderedList = ArrayList<OrderItems>()
                    for (orders in orderList) {

                        val title = StringBuilder()
                        var totalPrice = 0

                        for (products in orders.orderList!!) {
                            val price = products.productPrice?.substring(1)?.toInt()
                            val itemCount = products.productCount!!
                            totalPrice += (price?.times(itemCount)!!)
                            title.append("${products.productCategory}")
                        }

                        val orderedItems = OrderItems(
                            orders.orderId,
                            orders.orderDate,
                            orders.orderStatus,
                            title.toString(),
                            totalPrice,
                            orders.userAddress
                        )
                        orderedList.add(orderedItems)
                    }

                    Log.d("OrderFrag", "" + orderList.toString())
                    adapterOrder = AdapterOrders(requireContext(),::onOrderItemViewClicked)
                    binding.rvOrders.adapter = adapterOrder
                    adapterOrder.differ.submitList(orderedList)
                    binding.shimmerViewContainerOrders.visibility = View.GONE

                }else {

                    binding.shimmerViewContainerOrders.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE

                }
            }
        }

    }

    fun onOrderItemViewClicked(orderItems: OrderItems) {
        val bundle =Bundle()
        bundle.putInt("status",orderItems.itemStatus!!.toInt())
        bundle.putString("orderId",orderItems.orderId)
        bundle.putString("userAddress",orderItems.userAddress)

        findNavController().navigate(R.id.action_orderFragment_to_orderDeatailFragment,bundle)


    }

//    private fun onBackButtonClicked() {
//        binding.tbSearchFragment.setNavigationOnClickListener {
//            findNavController().navigate(R.id.action_ordersFragment_to_profileFragment)
//        }
//    }

}