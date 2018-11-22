package com.hipark.randomer.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.design.widget.Snackbar
import android.view.View
import com.hipark.randomer.SingleLiveEvent
import com.hipark.randomer.main.MainViewModel
import com.hipark.randomer.main.ScrollChildSwipeRefreshLayout

fun View.showSnackbar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun View.setupSnackbar(lifecycleOwner: LifecycleOwner,
                       snackbarMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    snackbarMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}

@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
    viewModel: MainViewModel) {
    setOnRefreshListener { viewModel.loadItems(true) }
}