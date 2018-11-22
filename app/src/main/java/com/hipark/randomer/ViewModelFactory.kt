package com.hipark.randomer

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.hipark.randomer.addedititem.AddEditItemViewModel
import com.hipark.randomer.data.Source.ItemsRepository
import com.hipark.randomer.itemdetail.ItemDetailViewModel
import com.hipark.randomer.main.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val application: Application,
    private val itemsRepository: ItemsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddEditItemViewModel::class.java) ->
                    AddEditItemViewModel(application, itemsRepository)
                isAssignableFrom(ItemDetailViewModel::class.java) ->
                    ItemDetailViewModel(application, itemsRepository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(application, itemsRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                    Injection.provideItemRepository(application.applicationContext))
                    .also { INSTANCE = it }
                }
    }
}