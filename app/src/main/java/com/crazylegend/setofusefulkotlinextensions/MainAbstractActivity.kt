package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.delegates.activityVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.setofusefulkotlinextensions.adapter.TestAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by activityVM<TestAVM>()
    private val adapter by lazy {
        TestAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.forItemClickListener = forItemClickListenerDSL { position, item, _ ->
            debug("CLICKED AT ${item.title} at position $position")
        }
        testAVM.posts?.observe(this, Observer {
            when (it) {
                is RetrofitResult.Success -> {
                    adapter.submitList(it.value)
                }
                RetrofitResult.Loading -> {
                    debug(it.toString())
                }
                RetrofitResult.EmptyData -> {
                    debug(it.toString())
                }
                is RetrofitResult.Error -> {
                    debug(it.toString())
                }
                is RetrofitResult.ApiError -> {
                    debug(it.toString())
                }
            }.exhaustive
        })
    }

}


