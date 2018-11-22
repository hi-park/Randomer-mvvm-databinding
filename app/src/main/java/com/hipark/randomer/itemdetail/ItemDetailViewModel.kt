package com.hipark.randomer.itemdetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import android.support.annotation.StringRes
import com.hipark.randomer.SingleLiveEvent
import com.hipark.randomer.data.Item
import com.hipark.randomer.data.Source.ItemsDataSource
import com.hipark.randomer.data.Source.ItemsRepository

class ItemDetailViewModel(
    context: Application,
    private val itemsRepository: ItemsRepository
) : AndroidViewModel(context), ItemsDataSource.GetItemCallback {

    val item = ObservableField<Item>()
    val editItemCommand = SingleLiveEvent<Void>()
    val deleteItemCommand = SingleLiveEvent<Void>()
    val snackbarMessage = SingleLiveEvent<Int>()
    var isDataLoading = false
        private set
    val isDataAvailable
        get() = item.get() != null

    fun deleteItem() {
        item.get()?.let {
            itemsRepository.deleteItem(it.id)
            deleteItemCommand.call()
        }
    }

    fun editItem() {
        editItemCommand.call()
    }

    fun start(itemId: String?) {
        itemId?.let {
            isDataLoading = true
            itemsRepository.getItem(it, this)
        }
    }

    fun setItem(item: Item) {
        this.item.set(item)
    }

    override fun onItemLoaded(item: Item) {
        setItem(item)
        isDataLoading = false
    }

    override fun onDataNotAvailable() {
        item.set(null)
        isDataLoading = false
    }

    fun onRefresh() {
        if(item.get() != null)
            start((item.get() as Item).id)
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        snackbarMessage.value = message
    }

}