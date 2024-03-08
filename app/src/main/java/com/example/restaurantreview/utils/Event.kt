package com.example.restaurantreview.utils

open class Event <out T>(private val content : T){

    @Suppress("MemberVisibilityCaanBePrivate")
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}