package com.mokshith.images.domain.repository

import com.mokshith.images.data.remote.dto.imagesList.ImagesListResponseDto
import retrofit2.Response

interface Repository {
    suspend fun getImagesListByName(str: String): Response<ImagesListResponseDto>
}


