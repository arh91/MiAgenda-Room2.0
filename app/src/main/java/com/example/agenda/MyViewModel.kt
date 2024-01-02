package com.example.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(private val customerRepository: CustomerRepository, private val supplierRepository:SupplierRepository) : ViewModel() {
    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    private val _customer = MutableLiveData<List<Customer>>()
    val customer: LiveData<List<Customer>> get() = _customer

    private val _customerDetails = MutableLiveData<Customer>()
    val customerDetails: LiveData<Customer> get() = _customerDetails

    private val _supplierDetails = MutableLiveData<Supplier>()
    val supplierDetails: LiveData<Supplier> get() = _supplierDetails

    private val _suppliers = MutableLiveData<List<Supplier>>()
    val suppliers: LiveData<List<Supplier>> get() = _suppliers

    private val _supplier = MutableLiveData<List<Supplier>>()
    val supplier: LiveData<List<Supplier>> get() = _supplier

    fun insertCustomer(c: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.insertCustomer(c)
        }
    }

    /*fun getAllCustomers(): List<Customer> {
        return customerRepository.getAllCustomers()
    }*/

    fun loadCustomerByName(nombre: String){
        viewModelScope.launch(Dispatchers.IO) {
            val customer = customerRepository.getCustomerByName(nombre)
            _customer.postValue(customer)
        }
    }

    fun loadCustomers() {
        viewModelScope.launch(Dispatchers.IO){
            val customers = customerRepository.getAllCustomers()
            _customers.postValue(customers)
        }
    }

    fun loadCustomerDetails(codigo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val customer = customerRepository.getCustomerDetails(codigo)
            _customerDetails.postValue(customer)
        }
    }

    fun deleteCustomer(codigo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.deleteCustomerById(codigo)
        }
    }

    fun updateCustomer(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.updateCustomerById(codigo, nuevoNombre, nuevaDireccion, nuevoTelefono)
        }
    }

    fun insertSupplier(s: Supplier) {
        viewModelScope.launch(Dispatchers.IO) {
            supplierRepository.insertSupplier(s)
        }
    }

    /*suspend fun getAllSuppliers(): List<Supplier> {
        return supplierRepository.getAllSuppliers()
    }*/

    fun loadSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            val suppliers = supplierRepository.getAllSuppliers()
            _suppliers.postValue(suppliers)
        }
    }

    fun loadSupplierByName(nombre: String){
        viewModelScope.launch(Dispatchers.IO) {
            val supplier = supplierRepository.getSupplierByName(nombre)
            _supplier.postValue(supplier)
        }
    }

    fun loadSupplierDetails(codigo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val supplier = supplierRepository.getSupplierDetails(codigo)
            _supplierDetails.postValue(supplier)
        }
    }

    fun deleteSupplier(codigo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            supplierRepository.deleteSupplierById(codigo)
        }
    }

    fun updateSupplier(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        viewModelScope.launch(Dispatchers.IO) {
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