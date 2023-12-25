package com.example.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyViewModel(private val customerRepository: CustomerRepository, private val supplierRepository:SupplierRepository) : ViewModel() {
    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    private val _customerDetails = MutableLiveData<Customer>()
    val customerDetails: LiveData<Customer> get() = _customerDetails

    private val _supplierDetails = MutableLiveData<Supplier>()
    val supplierDetails: LiveData<Supplier> get() = _supplierDetails

    private val _suppliers = MutableLiveData<List<Supplier>>()
    val suppliers: LiveData<List<Supplier>> get() = _suppliers

    fun insertCustomer(c: Customer) {
        viewModelScope.launch {
            customerRepository.insertCustomer(c)
        }
    }

    fun getAllCustomers(): List<Customer> {
        return customerRepository.getAllCustomers()
    }

    fun loadCustomers() {
        _customers.value = customerRepository.getAllCustomers()
    }

    fun loadCustomerDetails(customerId: String) {
        viewModelScope.launch {
            val customer = customerRepository.getCustomerDetails(customerId)
            _customerDetails.postValue(customer)
        }
    }

    fun deleteCustomer(codigo: String) {
        viewModelScope.launch {
            customerRepository.deleteCustomerById(codigo)
        }
    }

    fun updateCustomer(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        viewModelScope.launch {
            customerRepository.updateCustomerById(codigo, nuevoNombre, nuevaDireccion, nuevoTelefono)
        }
    }

    fun insertSupplier(s: Supplier) {
        viewModelScope.launch {
            supplierRepository.insertSupplier(s)
        }
    }

    fun getAllSuppliers(): List<Supplier> {
        return supplierRepository.getAllSuppliers()
    }

    fun loadSuppliers() {
        _suppliers.value = supplierRepository.getAllSuppliers()
    }

    fun loadSupplierDetails(supplierId: String) {
        viewModelScope.launch {
            val supplier = supplierRepository.getSupplierDetails(supplierId)
            _supplierDetails.postValue(supplier)
        }
    }

    fun deleteSupplier(codigo: String) {
        viewModelScope.launch {
            supplierRepository.deleteSupplierById(codigo)
        }
    }

    fun updateSupplier(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        viewModelScope.launch {
            supplierRepository.updateSupplierById(codigo, nuevoNombre, nuevaDireccion, nuevoTelefono)
        }
    }


    fun existeCodigoCliente(codigo: String): LiveData<Boolean> {
        return customerRepository.isCodigoCustomerExists(codigo)
    }

    fun existeCodigoProveedor(codigo: String): LiveData<Boolean> {
        return supplierRepository.isCodigoSupplierExists(codigo)
    }

}