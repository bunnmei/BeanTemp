package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemPlate(
    itemTitle: String?,
    itemText: String?,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = if (content == null) {
            modifier
                .fillMaxWidth()
                .height(60.dp)
        } else {
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        }
    ) {
        Text(
            text = itemTitle ?: "",
            fontSize = 12.sp,
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        if (content == null){
            Text(
                text = itemText ?: "NULL",
                fontSize = 20.sp,
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp),
            )
        } else {
            content()
        }

    }
}