package com.mokshith.images.domain.useCase.getImagesList

import com.mokshith.images.common.ApiState
import com.mokshith.images.data.remote.dto.imagesList.ImagesListResponseDto
import com.mokshith.images.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class GetImagesListUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(str: String): Flow<ApiState<ImagesListResponseDto>> = flow {
        try {
            emit(ApiState.Loading())
            val response = repository.getImagesListByName(str)
            if (response.isSuccessful) {
                emit(ApiState.Success(response.body()!!))
            }else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                // Handle 404 Not Found specifically
                emit(ApiState.Error("Menu list not found. (404)"))
            } else {
                // Handle other errors (consider adding more specific checks)
                emit(ApiState.Error("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ApiState.Error("Exception $e Couldn't reach servers. Check your internet connection"))
        } catch (e: TimeoutException){
            emit(ApiState.Error("Timeout Exception : $e"))
        }
    }
}