package ru.evbobrutskov.tinkoffandroiddeveloper2021.util

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.evbobrutskov.tinkoffandroiddeveloper2021.R
import ru.evbobrutskov.tinkoffandroiddeveloper2021.content.DevelopersLifeApiStatus

@BindingAdapter("imageUrl")
fun bindGif(imgView: ImageView, gifUrl: String?) {
    gifUrl?.let {
        val imgUri = gifUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).asGif()
            .load(gifUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("developersLifeApiStatus")
fun bindStatus(statusImageView: ImageView, status: DevelopersLifeApiStatus?) {
    when (status) {
        DevelopersLifeApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        DevelopersLifeApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        DevelopersLifeApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}