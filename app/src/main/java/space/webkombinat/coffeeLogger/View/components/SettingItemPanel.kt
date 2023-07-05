package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingItemPanel(title: String, modifier: Modifier = Modifier,content: @Composable (() -> Unit)? = null) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        if (content != null) {
            content()
        }
    }
}