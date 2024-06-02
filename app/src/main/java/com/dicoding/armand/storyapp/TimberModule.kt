package com.dicoding.armand.storyapp

import timber.log.Timber

object TimberModule {
    fun plantTimberTree(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}