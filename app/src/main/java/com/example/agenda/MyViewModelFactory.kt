package com.example.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory (
    private val customerRepository: CustomerRepository,
    private val supplierRepository: SupplierRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(customerRepository, supplierRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}