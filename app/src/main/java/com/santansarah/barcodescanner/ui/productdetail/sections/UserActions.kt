package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.barcodescanner.R

@Composable
fun UserActions(
    onSignIn: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        TextButton(onClick = { onSignIn() }) {
            Text(text = "Sign up to activate features.")
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share")
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add to list")
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(painter = painterResource(id = R.drawable.fitness), contentDescription = "MyFitnessPal")
        }

    }


}


@Preview(showBackground = true)
@Composable
fun UserActionsPreview() {
    UserActions({})
}