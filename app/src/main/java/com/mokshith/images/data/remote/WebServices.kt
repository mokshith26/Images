package com.mokshith.images.data.remote

import com.mokshith.images.data.remote.dto.imagesList.ImagesListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getImagesByName(@Query("tags") tags: String): Response<ImagesListResponseDto>

}