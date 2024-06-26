package com.mokshith.images.presentation.imagesList

import com.mokshith.images.data.remote.dto.imagesList.Item

data class ImagesListState(
    val isLoading: Boolean = false,
    val imagesList: List<Item> = emptyList(),
    val error: String = ""
)
