package com.hipark.randomer.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipark.randomer.R
import com.hipark.randomer.databinding.FragmentMainBinding
import com.hipark.randomer.util.setupSnackbar

class MainFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentMainBinding
    private lateinit var listAdapter: MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as MainActivity).obtainViewModel()
        }

        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewmodel?.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
        setupFab()
        setupListAdapter()
        setupRefreshLayout()
    }

    private fun setupFab() {

        activity?.findViewById<FloatingActionButton>(R.id.fab_add_item)?.run {
            setOnClickListener {
                viewDataBinding.viewmodel?.addNewItem()
            }
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if(viewModel != null) {
            listAdapter = MainAdapter(ArrayList(0), viewModel)
            viewDataBinding.itemsList.adapter = listAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupRefreshLayout() {

        activity?.let {
            viewDataBinding.refreshLayout.run {
                setColorSchemeColors(
                    ContextCompat.getColor(it, R.color.colorPrimary),
                    ContextCompat.getColor(it, R.color.colorAccent),
                    ContextCompat.getColor(it, R.color.colorPrimaryDark)
                )
                scrollUpChild = viewDataBinding.itemsList
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val TAG = "MainFragment"
    }

}