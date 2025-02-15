package com.ej.riversideassignment.ui.details

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ej.riversideassignment.R
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.utils.isLandscape

@Composable
fun TitleDetailsScreen(
    uiState: UiState,
    onTriggerEvent: (DetailsIntent) -> Unit,
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        when (uiState) {
            is UiState.Loading -> LoadingState(modifier = Modifier.align(Alignment.Center))

            is UiState.Failure -> FailureState(modifier = Modifier.align(Alignment.Center),
                errorMessage = uiState.stringRes)

            is UiState.Success -> {
                val moviesDetails = uiState.data
                MovieDetailsContent(moviesDetails, {
                    onTriggerEvent(DetailsIntent.FavouriteTitle(moviesDetails.imdbID, it))
                })
            }
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun FailureState(modifier: Modifier, @StringRes errorMessage: Int) {
    Text(text = stringResource(errorMessage),
        modifier = modifier,
        color = colorResource(R.color.primary_text))
}

@Composable
fun MovieDetailsContent(
    titleDetails: TitleDetails,
    isFavouriteClicked: (isSelected: Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current
    if (configuration.isLandscape()) {
        Row {
            AsyncImage(model = titleDetails.posterUrl,
                contentDescription = "Poster",
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = titleDetails.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(R.color.primary_text))
                Text(text = stringResource(R.string.release_year, titleDetails.year),
                    color = colorResource(R.color.primary_text))
                Text(text = titleDetails.plot ?: "",
                    color = colorResource(R.color.primary_text),
                    modifier = Modifier.padding(top = 8.dp))
                Row(modifier = Modifier.weight(1f)) {
                    ImageSelector(modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(6.dp),
                        titleDetails.isFavourite,
                        { isFavouriteClicked(it) })
                }
            }

        }
    } else {
        Column {
            AsyncImage(model = titleDetails.posterUrl,
                contentDescription = "Poster",
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp))
            Row {
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)) {
                    Text(text = titleDetails.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = colorResource(R.color.primary_text))
                    Text(text = stringResource(R.string.release_year, titleDetails.year),
                        color = colorResource(R.color.primary_text))
                    Text(text = titleDetails.plot ?: "",
                        color = colorResource(R.color.primary_text),
                        modifier = Modifier.padding(top = 8.dp))
                }
                ImageSelector(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(6.dp),
                    titleDetails.isFavourite,
                    { isFavouriteClicked(it) })
            }
        }
    }
}

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    isFavouriteClicked: (isSelected: Boolean) -> Unit,
) {
    Image(painter = painterResource(if (isFavourite) R.drawable.favourite_fill else R.drawable.favourite_outline),
        contentDescription = "Favourite icon",
        modifier = modifier.clickable { isFavouriteClicked(!isFavourite) })
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsContentPreview() {
    MovieDetailsContent(TitleDetails("Harry Potter",
        "1995",
        "Description",
        "Actors",
        null,
        true,
        ""), {})
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieDetailsContentDarkModePreview() {
    MovieDetailsContent(TitleDetails("Harry Potter",
        "1995",
        "Description",
        "Actors",
        null,
        true,
        ""), {})
}

@Preview(widthDp = 640, heightDp = 320, showBackground = true)
@Composable
fun MovieDetailsContentLandscapePreview() {
    MovieDetailsContent(TitleDetails("Harry Potter",
        "1995",
        "Description",
        "Actors",
        null,
        false,
        ""), {})
}

@Preview(widthDp = 640,
    heightDp = 320,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieDetailsContentLandscapeDarkModePreview() {
    MovieDetailsContent(TitleDetails("Harry Potter",
        "1995",
        "Description",
        "Actors",
        null,
        false,
        ""), {})
}