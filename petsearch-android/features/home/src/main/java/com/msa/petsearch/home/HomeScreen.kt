package com.msa.petsearch.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.msa.petsearch.commoncompose.composable.FadeAnimatedVisibility
import com.msa.petsearch.commoncompose.util.disableSplitMotionEvents
import com.msa.petsearch.home.composable.HomeProgressIndicator
import com.msa.petsearch.home.composable.LazyPetGrid
import com.msa.petsearch.home.composable.tabrow.HomeTabRow
import com.msa.petsearch.home.composable.tabrow.rememberHomeTabRowState
import com.msa.petsearch.home.util.isLoading
import com.msa.petsearch.shared.domain.homeuicontract.HomeViewModel
import com.msa.petsearch.shared.domain.homeuicontract.contract.store.HomeAction
import kotlinx.coroutines.launch
import com.msa.petsearch.commonres.R as commonR

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val homeTabRowState = rememberHomeTabRowState()
    val gridState = rememberLazyGridState()

    val renderState by viewModel.observeRenderState().collectAsState(initial = null)
    val petList = renderState?.petPagingData?.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        if (renderState?.petTypes.isNullOrEmpty()) {
            viewModel.action(HomeAction.GetPetTypes)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // TOOLBAR .................................................................................
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .statusBarsPadding()
                    .clickable { }
                    .padding(start = 20.dp, top = 8.dp, end = 24.dp, bottom = 16.dp)
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        painter = painterResource(id = commonR.drawable.ic_near_me),
                        contentDescription = "Location icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "New York City",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 6.dp, end = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Drop down icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "20 W 34th St., New York, United States",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 4.dp, top = 2.dp)
                )
            }
        }

        // TAB_LAYOUT ..............................................................................
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            FadeAnimatedVisibility(
                visible = !renderState?.petTypes.isNullOrEmpty(),
                durationMillis = 500
            ) {
                renderState?.petTypes?.let { petTypes ->
                    HomeTabRow(
                        items = petTypes.map { it.name },
                        state = homeTabRowState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) { index, name ->
                        coroutineScope.launch {
                            homeTabRowState.selectedTabIndex = index
                            viewModel.action(HomeAction.OnPetTypeTabSelected(name))

                            gridState.scrollToItem(0) // Reset grid state
                        }
                    }
                }
            }
        }

        // DIVIDER .................................................................................
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(0.1F))
        )

        // LAZY_GRID ...............................................................................
        if (petList != null && petList.itemCount != 0) {
            LazyPetGrid(
                petList = petList,
                state = gridState,
                progressIndicatorVisibility = petList.isLoading(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05F))
                    .disableSplitMotionEvents()
            ) { petInfo ->
                coroutineScope.launch {
                    viewModel.action(HomeAction.NavigateToPetDetail(petInfo))
                }
            }
        }
        // This else statement is a workaround for the issue where
        // paging data item count becomes 0 (momentarily) when navigating to or from the screen
        // and causes to GridScrollState to reset to 0
        // For detailed explanation, see
        // https://stackoverflow.com/a/70520441
        else {
            HomeProgressIndicator(
                animate = petList.isLoading(),
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}