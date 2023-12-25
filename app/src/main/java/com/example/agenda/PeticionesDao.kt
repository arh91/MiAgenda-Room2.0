package com.example.agenda

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface PeticionesDao {

    @Insert
    fun insertCustomer(c: Customer)

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): List<Customer>

    @Query("SELECT * FROM Customer WHERE codigo = :customerId")
    suspend fun getCustomerById(customerId: String): Customer

    @Query("DELETE FROM Customer WHERE codigo = :customerId")
    suspend fun deleteCustomerById(customerId: String)

    @Query("UPDATE Customer SET nombre = :customerName, direccion = :customerAddress, telefono = :customerPhone WHERE codigo = :customerId")
    suspend fun updateCustomerById(customerId: String, customerName: String, customerAddress: String, customerPhone: String)

    @Insert
    fun insertSupplier(s: Supplier)

    @Query("SELECT * FROM Supplier")
    fun getAllSuppliers(): List<Supplier>

    @Query("SELECT * FROM Supplier WHERE codigo = :supplierId")
    suspend fun getSupplierById(supplierId: String): Supplier

    @Query("DELETE FROM Supplier WHERE codigo = :supplierId")
    suspend fun deleteSupplierById(supplierId: String)

    @Query("UPDATE Supplier SET nombre = :supplierName, direccion = :supplierAddress, telefono = :supplierPhone WHERE codigo = :supplierId")
    suspend fun updateSupplierById(supplierId: String, supplierName: String, supplierAddress: String, supplierPhone: String)


    //Comprobamos si el código que recibe como parámetro existe en la entidad Customer
    @Query("SELECT EXISTS(SELECT 1 FROM Customer WHERE codigo = :customerId LIMIT 1)")
    fun isCodigoCustomerExists(customerId: String): LiveData<Boolean>

    //Comprobamos si el código que recibe como parámetro existe en la entidad Supplier
    @Query("SELECT EXISTS(SELECT 1 FROM Supplier WHERE codigo = :supplierId LIMIT 1)")
    fun isCodigoSupplierExists(supplierId: String): LiveData<Boolean>
}