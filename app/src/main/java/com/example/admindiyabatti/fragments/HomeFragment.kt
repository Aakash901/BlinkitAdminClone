package com.example.admindiyabatti.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.admindiyabatti.R
import com.example.admindiyabatti.activity.AuthMainActivity
import com.example.admindiyabatti.adapter.AdapterProduct
import com.example.admindiyabatti.adapter.CategoryAdapter
import com.example.admindiyabatti.databinding.EditProductLayoutBinding
import com.example.admindiyabatti.databinding.FragmentHomeBinding
import com.example.admindiyabatti.models.Category
import com.example.admindiyabatti.models.Product
import com.example.admindiyabatti.utils.Constants
import com.example.admindiyabatti.utils.Utils
import com.example.admindiyabatti.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: AdminViewModel by viewModels()
    private lateinit var adapterProduct: AdapterProduct


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        setCategories()
        getAllTheProducts("All")
        searchProducts()
        onLogOut()
        return binding.root

    }

    private fun onLogOut() {
        binding.tbHomeFragment.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    logOutUser()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun logOutUser() {

        val bulider = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val alertDialog = bulider.create()
        bulider.setTitle("Log out")
            .setMessage("Do you want to log out ?")
            .setPositiveButton("yes") { _, _ ->
                viewModel.logOutUser()
                startActivity(Intent(requireContext(), AuthMainActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No") { _, _ ->
                alertDialog.dismiss()

            }
            .show()
            .setCancelable(false)

    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = s.toString().trim()
                adapterProduct.getFilter().filter(query)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun getAllTheProducts(category: String) {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {

            Log.d("AdminViewModel", "Uid id " +Utils.getCurrentUserId().toString())
            viewModel.fetchAllTheProducts(category).collect() {

                if (it.isEmpty()) {
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                } else {
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }

                adapterProduct = AdapterProduct(::onEditBtnClicked)
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>
                binding.shimmerViewContainer.visibility = View.GONE

            }
        }
    }

    private fun setCategories() {

        val categoryList = ArrayList<Category>()
        for (i in 0 until Constants.allProductsCategoryIcon.size) {

            categoryList.add(
                Category(
                    Constants.allProductsCategory[i],
                    Constants.allProductsCategoryIcon[i]
                )
            )

        }

        binding.rvCategories.adapter = CategoryAdapter(categoryList, ::onCategoryClicked)

    }

    private fun onCategoryClicked(categories: Category) {

        getAllTheProducts(categories.category)

    }

    private fun onEditBtnClicked(product: Product,productId:String) {
        val ownerId = Utils.getCurrentUserId().toString()
        if(productId == ownerId){
            val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            editProduct.apply {
                etProductTitle.setText(product.productTitle)
                etProductQuantity.setText(product.productQuantity.toString())
                etProductUnit.setText(product.productUnit)
                etProductPrice.setText(product.productPrice.toString())
                etProductStocks.setText(product.productStock.toString())
                etProductCategory.setText(product.productCategory)
                etProductType.setText(product.productType)
            }

            val uid = product.adminUid
            Log.d("Offline","id is : "+Utils.getCurrentUserId().toString())

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(editProduct.root)
                .create()
            alertDialog.show()

            editProduct.btnEdit.setOnClickListener {
                editProduct.etProductTitle.isEnabled = true
                editProduct.etProductQuantity.isEnabled = true
                editProduct.etProductUnit.isEnabled = true
                editProduct.etProductPrice.isEnabled = true
                editProduct.etProductStocks.isEnabled = true
                editProduct.etProductCategory.isEnabled = true
                editProduct.etProductType.isEnabled = true
            }
            setAutoCompleteTextView(editProduct)

            editProduct.btnAdd.setOnClickListener {

                lifecycleScope.launch {
                    product.productTitle = editProduct.etProductTitle.text.toString()
                    product.productQuantity = editProduct.etProductQuantity.text.toString().toInt()
                    product.productPrice = editProduct.etProductPrice.text.toString().toInt()
                    product.productStock = editProduct.etProductStocks.text.toString().toInt()
                    product.productCategory = editProduct.etProductCategory.text.toString()
                    product.productType = editProduct.etProductType.text.toString()
                    product.productUnit = editProduct.etProductUnit.text.toString()
                    viewModel.savingUpdateProducts(product)
                }

                Utils.showToast(requireContext(), "Changes Saved !")
                alertDialog.dismiss()
            }
        }else{
            Utils.showToast(requireContext(), "You  are not owner of this product !")
        }


    }


    private fun setAutoCompleteTextView(editProduct: EditProductLayoutBinding) {

        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category =
            ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
        val categoryType =
            ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        editProduct.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(categoryType)
        }
    }


}