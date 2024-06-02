package com.dicoding.armand.storyapp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource

class RecyclerViewIdlingResource(private val recyclerView: RecyclerView) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (recyclerView.adapter?.itemCount ?: 0 > 0) {
                callback?.onTransitionToIdle()
            }
        }
    }

    override fun getName(): String {
        return RecyclerViewIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        val idle = recyclerView.adapter?.itemCount ?: 0 > 0
        if (idle) {
            callback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
        recyclerView.adapter?.registerAdapterDataObserver(observer)
    }
}