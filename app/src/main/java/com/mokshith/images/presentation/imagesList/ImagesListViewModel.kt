package com.mokshith.images.presentation.imagesList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import com.mokshith.images.common.ApiState
import com.mokshith.images.common.composobles.LoadingScreen
import com.mokshith.images.domain.useCase.getImagesList.GetImagesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImagesListViewModel @Inject constructor(
    private val useCase : GetImagesListUseCase
): ViewModel(){
    private val _state = mutableStateOf(ImagesListState())
    val state: State<ImagesListState> = _state

    fun fetchPhotos(tags: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                useCase.invoke(tags).collect{ imagesList->
                    withContext(Dispatchers.Main) {

                        when (imagesList) {
                            is ApiState.Success -> {
                                if (imagesList.data!!.items.isEmpty()){
                                    _state.value =
                                        ImagesListState(
                                            error = imagesList.message
                                                ?: "We Cant find images for the search $tags"
                                        )
                                }else{
                                    _state.value = ImagesListState(imagesList = imagesList.data!!.items)
                                }
                            }

                            is ApiState.Loading -> {
                                _state.value = ImagesListState(isLoading = true)
                            }

                            is ApiState.Error -> {
                                _state.value =
                                    ImagesListState(
                                        error = imagesList.message
                                            ?: "An Unexpected error occurred"
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}