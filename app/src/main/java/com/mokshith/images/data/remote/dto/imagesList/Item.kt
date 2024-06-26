package com.mokshith.images.data.remote.dto.imagesList

import com.google.gson.annotations.SerializedName
import com.mokshith.images.domain.models.ImagesList

data class Item(
    val author: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("date_taken")
    val dateTaken: String,
    val description: String,
    val link: String,
    val media: Media,
    val published: String,
    val tags: String,
    val title: String
)


fun Item.toRequiredData(): ImagesList {
    return ImagesList(
        title = title,
        description = description,
        author = author,
        published = published,
        imageUrl = media.m,
    )
}