package com.mokshith.images.data.repository

import com.mokshith.images.data.remote.WebServices
import com.mokshith.images.data.remote.dto.imagesList.ImagesListResponseDto
import com.mokshith.images.domain.repository.Repository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val webServices: WebServices
) : Repository {
    override suspend fun getImagesListByName(str: String): Response<ImagesListResponseDto> {
        return webServices.getImagesByName(str)
    }


}