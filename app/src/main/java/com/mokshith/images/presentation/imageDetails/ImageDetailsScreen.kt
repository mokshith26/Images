package com.mokshith.images.presentation.imageDetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mokshith.images.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScreen(
    navController: NavHostController,
    title: String,
    description: String,
    author: String,
    publishedDate: String,
    imageUrl: String
) {
    val decodedTitle = Uri.decode(title)
    val decodedDescription = (Uri.decode(description))
    val decodedAuthor = Uri.decode(author)
    val decodedPublishedDate = Uri.decode(publishedDate)
    val decodedImageUrl = Uri.decode(imageUrl)

    val (width, height) = parseDimensions(decodedDescription)
    Log.i("TAG", "ImageDetailsScreen out: $width $height" )

    val regex = "<[^>]*>".toRegex() // Matches any HTML tag
    val descriptionWithoutTags = regex.replace(decodedDescription, "")

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ){
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = "Details Screen",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.semantics {
                                contentDescription = "Details Screen Title"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigateUp()
                        },
                            modifier = Modifier.semantics {
                                contentDescription = "Navigate back button"
                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                )
            }

        },
        floatingActionButton = {
            ShareFloatingActionButton(
                title = decodedTitle,
                description = descriptionWithoutTags,
                author = decodedAuthor,
                imageUrl = decodedImageUrl
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingIntent ->

        ImageDetailsData(
            paddingIntent,
            decodedTitle,
            descriptionWithoutTags,
            decodedAuthor,
            decodedPublishedDate,
            decodedImageUrl,
            width,
            height
        )
    }
}

@Composable
fun ShareFloatingActionButton(title: String, description: String, author: String, imageUrl: String) {
    val context = LocalContext.current

    FloatingActionButton(
        onClick = {
            // Create an Intent to share image and metadata
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Image Details")
            val shareMessage = """
                Title: $title
                Description: $description
                Author: $author
                Image URL: $imageUrl
            """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        },
        content = {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share_the_image_with_data)
            )
        },
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ImageDetailsData(
    paddingIntent: PaddingValues,
    decodedTitle: String,
    descriptionWithoutTags: String,
    decodedAuthor: String,
    decodedPublishedDate: String,
    decodedImageUrl: String,
    width: Int?,
    height: Int?
) {
    Column(modifier = Modifier.padding(paddingIntent)) {

        Card(
            modifier = Modifier
                .padding(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Column(modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())) {


                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = decodedImageUrl)
                        .placeholder(R.drawable.ic_launcher_background) // Set placeholder drawable
                        .error(R.drawable.ic_launcher_background).build()
                )
                Image(
                    painter = painter,
                    contentDescription = descriptionWithoutTags,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15))
                        .semantics {
                            contentDescription = descriptionWithoutTags

                        },
                )
                // Strings of the text field come form strings.xml
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.title, decodedTitle),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics {
                        contentDescription = "The title of the images is $decodedTitle"
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.description, descriptionWithoutTags),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "The Description of the image is given as $descriptionWithoutTags"
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.author, decodedAuthor),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics {
                        contentDescription = "The Author of the image is: $decodedAuthor"
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.published, formatDate(decodedPublishedDate)),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "This image is published on ${formatDate(decodedPublishedDate)}"
                    }
                )
                if (width != null && height != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${stringResource(R.string.dimensions)} $width Width $height Height",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "The dimensions of the image are Width: $width, Height: $height"
                        }
                    )
                }
            }
        }
    }
}

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val date: Date? = inputFormat.parse(dateString)
    return date?.let { outputFormat.format(it) } ?: dateString
}


fun parseDimensions(description: String): Pair<Int?, Int?> {
    val widthRegex = "width=\"(\\d+)\"".toRegex()
    val heightRegex = "height=\"(\\d+)\"".toRegex()

    val width = widthRegex.find(description)?.groups?.get(1)?.value?.toIntOrNull()
    val height = heightRegex.find(description)?.groups?.get(1)?.value?.toIntOrNull()

    return Pair(width, height)
}