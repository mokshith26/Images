package com.mokshith.images.data.remote.dto.imagesList


data class ImagesListResponseDto(
    val description: String,
    val generator: String,
    val items: List<Item>,
    val link: String,
    val modified: String,
    val title: String
)

