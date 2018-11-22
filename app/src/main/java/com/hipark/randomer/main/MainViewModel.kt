package com.hipark.randomer.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.hipark.randomer.R
import com.hipark.randomer.SingleLiveEvent
import com.hipark.randomer.addedititem.AddEditItemActivity
import com.hipark.randomer.data.Item
import com.hipark.randomer.data.Source.ItemsDataSource
import com.hipark.randomer.data.Source.ItemsRepository
import com.hipark.randomer.util.ADD_EDIT_RESULT_OK
import com.hipark.randomer.util.DELETE_RESULT_OK
import com.hipark.randomer.util.EDIT_RESULT_OK

class MainViewModel(
    context: Application,
    private val itemsRepository: ItemsRepository
) : AndroidViewModel(context) {

    private val isDataLoadingError = ObservableBoolean(false)

    internal val openItemEvent = SingleLiveEvent<String>()

    val items: ObservableList<Item> = ObservableArrayList()
    val dataLoading = ObservableBoolean(false)
    val noItemsLabel = ObservableField<String>()
    val empty = ObservableBoolean(false)
    val itemsAddViewVisible = ObservableBoolean()
    val snackbarMessage = SingleLiveEvent<Int>()
    val newItemEvent = SingleLiveEvent<Void>()

    fun start() {
        loadItems(false)
    }

    fun loadItems(forceUpdate: Boolean) {
        loadItems(forceUpdate, true)
    }

    private fun showSnackbarMessage(message: Int) {
        snackbarMessage.value = message
    }

    fun addNewItem() {
        newItemEvent.call()
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if(AddEditItemActivity.REQUEST_CODE == requestCode) {
            snackbarMessage.value =
                    when(resultCode) {
                        EDIT_RESULT_OK ->
                            R.string.successfully_saved_item_message
                        ADD_EDIT_RESULT_OK ->
                            R.string.successfully_added_item_message
                        DELETE_RESULT_OK ->
                            R.string.successfully_deleted_item_message
                        else -> return
                    }
        }
    }

    private fun loadItems(forceUpdate: Boolean, showLoadingUI: Boolean) {

        if(showLoadingUI) {
            dataLoading.set(true)
        }

        if(forceUpdate) {
            itemsRepository.refreshItems()
        }

        itemsRepository.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(itemsToShow: List<Item>) {

                if(showLoadingUI) {
                    dataLoading.set(false)
                }

                isDataLoadingError.set(false)



                with(items) {
                    clear()
                    addAll(itemsToShow)
                    empty.set(isEmpty())
                }
            }

            override fun onDataNotAvaiilable() {
                isDataLoadingError.set(true)
            }
        })
    }
}