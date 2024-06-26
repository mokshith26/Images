package com.mokshith.images.presentation.imagesList

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mokshith.images.R
import com.mokshith.images.common.composobles.ErrorScreen
import com.mokshith.images.common.composobles.LoadingScreen
import com.mokshith.images.data.remote.dto.imagesList.Item
import com.mokshith.images.navigation.Screen

@Composable
fun ImagesListScreen(
    navController: NavHostController, viewModel: ImagesListViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var tags by remember { mutableStateOf("") }
    Column {
        TextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Enter tags") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .semantics {
                    // Provide accessibility properties for the TextField
                    contentDescription = "Search tags"
                }
        )
        Button(
            //I have not restricted min count for search because we are getting response if don't pass any response
            onClick = {
                viewModel.fetchPhotos(tags)
                keyboardController?.hide()
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .semantics {
                    // Provide accessibility properties for the Button
                    contentDescription = "Search button"
                }
        ) {
            Text("Search")
        }

        val photos = viewModel.state.value

        if (photos.error.isNotBlank()) {
            ErrorScreen(error = photos.error)
        }
        if (photos.isLoading) {
            LoadingScreen()
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos.imagesList.size) { index ->
                val photo = photos.imagesList[index]
                AnimatedVisibility(
                    visible = true, // Adjust visibility logic based on your requirements
                    enter = fadeIn(animationSpec = tween(400)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NetworkImage(photo) {
                        navigateToImageDetails(navController, photo)
                    }
                }
            }
        }
    }
}

fun navigateToImageDetails(navController: NavHostController, photo: Item) {
    val encodedTitle = Uri.encode(photo.title)
    val encodedDescription = Uri.encode(photo.description)
    val encodedAuthor = Uri.encode(photo.author)
    val encodedPublishedDate = Uri.encode(photo.published)
    val encodedImageUrl = Uri.encode(photo.media.m)
    navController.navigate(
        Screen.ImageDetailsScreen.route +
                "/$encodedTitle/$encodedDescription/$encodedAuthor/$encodedPublishedDate/$encodedImageUrl"
    )
}

@Composable
fun NetworkImage(item: Item, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clickable { onClick() },
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = item.media.m)
                .placeholder(R.drawable.ic_launcher_background) // Set placeholder drawable
                .error(R.drawable.ic_launcher_background).build()
        )
        Image(
            painter = painter,
            contentDescription = item.description,
            contentScale = ContentScale.Crop,

            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20)),
        )
    }
}

