package com.ayo.ctmtest.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ayo.ctmtest.R
import com.ayo.ctmtest.data.Word
import com.ayo.ctmtest.view.adapter.WordListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity : AppCompatActivity() {

    private var adapter: WordListAdapter? = null
    private var viewModel: WordListViewModel? = null
    private var snackBar: Snackbar? = null

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewModel = ViewModelProviders.of(this).get(WordListViewModel::class.java)
        viewModel?.event?.observe(this, Observer { handleEvents(it) })
        viewModel?.inject()
        viewModel?.loadWordListFromUrl(BOOK_URL)
    }

    override fun onDestroy() {
        viewModel?.cancelActiveJobs()
        snackBar?.apply { if (isShown) dismiss() }
        super.onDestroy()
    }

    private fun handleEvents(event: WordListViewModel.Event?) {
        when (event) {
            is WordListViewModel.Event.WordList -> {
                event.data?.let { handleWordList(it) }
                event.loading?.let { toggleProgressBar(it) }
                event.exception?.let { showSnackBar("Error") }
            }
        }
    }

    private fun initView() {
        adapter = WordListAdapter()
        ui_word_list.layoutManager = LinearLayoutManager(this)
        ui_word_list.adapter = adapter
    }

    private fun showSnackBar(msg: String) {
        snackBar = Snackbar.make(root_view, msg, Snackbar.LENGTH_LONG)
        snackBar?.show()
    }

    private fun handleWordList(list: List<Word>){
        if (list.isEmpty())
            showSnackBar("Complete!")
        else
            adapter?.update(list)
    }

    private fun toggleProgressBar(loading: Boolean) {
        if (loading) {
            if (progressBar.visibility == View.GONE)
                progressBar.visibility = View.VISIBLE
        } else {
            if (progressBar.visibility == View.VISIBLE)
                progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val BOOK_URL = "https://www.w3.org/TR/PNG/iso_8859-1.txt"

    }
}
