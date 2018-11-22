package com.hipark.randomer.itemdetail

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hipark.randomer.R
import com.hipark.randomer.addedititem.AddEditItemActivity
import com.hipark.randomer.addedititem.AddEditItemFragment
import com.hipark.randomer.itemdetail.ItemDetailFragment.Companion.REQUEST_EDIT_ITEM
import com.hipark.randomer.util.*

class ItemDetailActivity : AppCompatActivity(), ItemDetailNavigator {

    private lateinit var itemViewModel: ItemDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_detail)

        replaceFragmentInActivity(findOrCreateViewFragment(), R.id.contentFrame)

        itemViewModel = obtainViewModel()

        subscribeToNavigationChanges(itemViewModel)

    }

    private fun findOrCreateViewFragment() =
                supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
                ItemDetailFragment.newInstance(intent.getStringExtra(EXTRA_ITEM_ID))

    private fun subscribeToNavigationChanges(viewModel: ItemDetailViewModel) {

        val activity = this@ItemDetailActivity
        viewModel.run {
            editItemCommand.observe(activity,
                Observer { activity.onStartEditItem() })
            deleteItemCommand.observe(activity,
                Observer { activity.onItemDeleted() })
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_EDIT_ITEM) {
            if(resultCode == ADD_EDIT_RESULT_OK) {
                setResult(EDIT_RESULT_OK)
                finish()
            }
        }
    }

    override fun onItemDeleted() {
        setResult(DELETE_RESULT_OK)
        finish()
    }

    override fun onStartEditItem() {
        val itemId = intent.getStringExtra(EXTRA_ITEM_ID)
        val intent = Intent(this, AddEditItemActivity::class.java).apply {
            putExtra(AddEditItemFragment.ARGUMENT_EDIT_ITEM_ID, itemId)
        }
        startActivityForResult(intent, REQUEST_EDIT_ITEM)
    }

    fun obtainViewModel(): ItemDetailViewModel = obtainViewModel(ItemDetailViewModel::class.java)

    companion object {
        const val EXTRA_ITEM_ID = "ITEM_ID"
    }

}
