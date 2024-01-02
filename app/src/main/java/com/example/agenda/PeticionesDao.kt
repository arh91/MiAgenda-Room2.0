package com.example.agenda

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeticionesDao {

    @Insert
    suspend fun insertCustomer(c: Customer)

    @Query("SELECT * FROM Customer")
    suspend fun getAllCustomers(): List<Customer>

    @Query("SELECT * FROM Customer WHERE codigoCli = :customerId")
    suspend fun getCustomerById(customerId: String): Customer

    @Query("SELECT * FROM Customer WHERE nombreCli = :customerName")
    suspend fun getCustomerByName(customerName: String): List<Customer>

    @Query("DELETE FROM Customer WHERE codigoCli = :customerId")
    suspend fun deleteCustomerById(customerId: String)

    @Query("UPDATE Customer SET nombreCli = :customerName, direccionCli = :customerAddress, telefonoCli = :customerPhone WHERE codigoCli = :customerId")
    suspend fun updateCustomerById(customerId: String, customerName: String, customerAddress: String, customerPhone: String)

    @Insert
    suspend fun insertSupplier(s: Supplier)

    @Query("SELECT * FROM Supplier")
    suspend fun getAllSuppliers(): List<Supplier>

    @Query("SELECT * FROM Supplier WHERE codigoProv = :supplierId")
    suspend fun getSupplierById(supplierId: String): Supplier

    @Query("SELECT * FROM Supplier WHERE nombreProv = :supplierName")
    suspend fun getSupplierByName(supplierName: String): List<Supplier>

    @Query("DELETE FROM Supplier WHERE codigoProv = :supplierId")
    suspend fun deleteSupplierById(supplierId: String)

    @Query("UPDATE Supplier SET nombreProv = :supplierName, direccionProv = :supplierAddress, telefonoProv = :supplierPhone WHERE codigoProv = :supplierId")
    suspend fun updateSupplierById(supplierId: String, supplierName: String, supplierAddress: String, supplierPhone: String)


    //Comprobamos si el código que recibe como parámetro existe en la entidad Customer
    @Query("SELECT EXISTS(SELECT 1 FROM Customer WHERE codigoCli = :customerId LIMIT 1)")
    fun isCodigoCustomerExists(customerId: String): LiveData<Boolean>

    //Comprobamos si el código que recibe como parámetro existe en la entidad Supplier
    @Query("SELECT EXISTS(SELECT 1 FROM Supplier WHERE codigoProv = :supplierId LIMIT 1)")
    fun isCodigoSupplierExists(supplierId: String): LiveData<Boolean>
}