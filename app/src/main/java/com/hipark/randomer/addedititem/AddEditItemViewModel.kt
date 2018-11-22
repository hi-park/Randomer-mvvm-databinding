package com.hipark.randomer.addedititem

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.StringRes
import com.hipark.randomer.R
import com.hipark.randomer.SingleLiveEvent
import com.hipark.randomer.data.Item
import com.hipark.randomer.data.Source.ItemsDataSource
import com.hipark.randomer.data.Source.ItemsRepository
import java.lang.RuntimeException

class AddEditItemViewModel(
    context: Application,
    private val itemsRepository: ItemsRepository
) : AndroidViewModel(context), ItemsDataSource.GetItemCallback {

    val title = ObservableField<String>()
    val description = ObservableField<String>()
    val dataLoading = ObservableBoolean(false)
    internal val snackbarMessage = SingleLiveEvent<Int>()
    internal val itemUpdatedEvent = SingleLiveEvent<Void>()
    private var itemId: String? = null
    private val isNewItem
        get() = itemId == null
    private var isDataLoad = false

    fun start(itemId: String?) {

        if(dataLoading.get()) {
            return
        }

        this.itemId = itemId

        if(isNewItem || isDataLoad)
            return

        dataLoading.set(true)
        itemId?.let {
            itemsRepository.getItem(it, this)
        }
    }

    override fun onItemLoaded(item: Item) {
        title.set(item.title)
        description.set(item.description)
        dataLoading.set(false)
        isDataLoad = true
    }

    override fun onDataNotAvailable() {
        dataLoading.set(false)
    }

    fun saveItem() {
        val item = Item(title.get()!!, description.get()!!)

        if(item.isEmpty) {
            showSnackbarMessage(R.string.empty_item_message)
            return
        }

        if(isNewItem) {
            createItem(item)
        } else {
            itemId?.let {
                updateItem(Item(title.get()!!, description.get()!!, it))
            }
        }
    }

    private fun createItem(newItem: Item) {
        itemsRepository.saveItem(newItem)
        itemUpdatedEvent.call()
    }

    private fun updateItem(item: Item) {
        if(isNewItem) {
            throw RuntimeException("updateItem() was called but item is new.")
        }
        itemsRepository.saveItem(item)
        itemUpdatedEvent.call()
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        snackbarMessage.value = message
    }
}