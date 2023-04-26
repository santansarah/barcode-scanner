package com.santansarah.barcodescanner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.grayButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    searchText: String,
    onValueChanged: (String) -> Unit,
    onSearchWithText: ((String) -> Unit)?,
    onSearch: (() -> Unit)?
) {

    val searchFocusRequester = remember {FocusRequester()}

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchWithText?.let { it(searchText) } ?: onSearch?.let { it() }
                }
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(searchFocusRequester),
            //.padding(vertical = 4.dp),
            value = searchText,
            onValueChange = { onValueChanged(it) },
            trailingIcon = {

                Row(
                    //horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = {
                            onValueChanged("")
                            searchFocusRequester.requestFocus()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = gray
                            )
                        }
                    }

                    IconButton(onClick = {
                        onSearchWithText?.let { it(searchText) } ?: onSearch?.let { it() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search for products",
                            tint = grayButton
                        )
                    }
                }
            }
        )
        //Spacer(Modifier.height(10.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchField() {

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    BarcodeScannerTheme {
        SearchTextField(searchText = searchText,
            onValueChanged = {searchText = it},
            onSearchWithText = {}) {

        }
    }
}
