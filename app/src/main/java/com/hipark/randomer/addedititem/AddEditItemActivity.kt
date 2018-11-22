package com.hipark.randomer.addedititem

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hipark.randomer.R
import com.hipark.randomer.util.ADD_EDIT_RESULT_OK
import com.hipark.randomer.util.obtainViewModel
import com.hipark.randomer.util.replaceFragmentInActivity

class AddEditItemActivity : AppCompatActivity(), AddEditItemNavigator {

    override fun onItemSaved() {
        setResult(ADD_EDIT_RESULT_OK)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_item)

        replaceFragmentInActivity(obtainViewFragment(), R.id.contentFrame)

        subscribeToNavigationChanges()
    }

    private fun subscribeToNavigationChanges() {
        obtainViewModel().itemUpdatedEvent.observe(this, Observer {
            this@AddEditItemActivity.onItemSaved()
        })
    }

    private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            AddEditItemFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putString(AddEditItemFragment.ARGUMENT_EDIT_ITEM_ID,
                        intent.getStringExtra(AddEditItemFragment.ARGUMENT_EDIT_ITEM_ID))
                }
            }

    fun obtainViewModel(): AddEditItemViewModel = obtainViewModel(AddEditItemViewModel::class.java)

    companion object {
        const val REQUEST_CODE = 1
    }
}
