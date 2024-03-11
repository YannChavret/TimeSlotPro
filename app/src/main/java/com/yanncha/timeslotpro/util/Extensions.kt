package com.yanncha.timeslotpro.util

import android.widget.EditText
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yanncha.timeslotpro.R

fun EditText.setFocusChangeBlue(): EditText {
    setOnFocusChangeListener { _, hasFocus ->
        setBackgroundResource(
            if(hasFocus)
                (R.drawable.edit_text_background_blue)
            else
                (R.drawable.edit_text_background_default)
        )
    }
    return this
}

fun EditText.setFocusChangeYellow(): EditText {
    setOnFocusChangeListener { _, hasFocus ->
        setBackgroundResource(
            if(hasFocus)
                (R.drawable.edit_text_background_yellow)
            else
                (R.drawable.edit_text_background_default)
        )
    }
    return this
}

fun EditText.setFocusChangePink(): EditText {
    setOnFocusChangeListener { _, hasFocus ->
        setBackgroundResource(
            if(hasFocus)
                (R.drawable.edit_text_background_pink)
            else
                (R.drawable.edit_text_background_default)
        )
    }
    return this
}

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { it.lowercase().capitalize() }
}

fun picasso(url: String?, imageView: ImageView) {
    Picasso.get()
        .load(url?.ifEmpty { "boo" } ?: "boo")
        .error(R.drawable.timeslotpro_logo)
        .into(imageView)
}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    fun peekContent(): T = content
}

enum class ReservationFilter {
    ALL, FUTURE, PAST
}
