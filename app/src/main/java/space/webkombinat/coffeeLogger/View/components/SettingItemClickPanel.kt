package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingItemClickPanel(
    title: String,
    subTitle: String?,
    panelId: Int,
    id: Int,
    modifier: Modifier = Modifier,
    clickFunc: () -> Unit,
) {
    Row(
        modifier = modifier
            .toggleable(
                value = id == panelId,
                role = Role.Checkbox,
                onValueChange = {
                    clickFunc()
                }
            )
            .padding(16.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 20.sp
            )
            Text(
                text = subTitle ?: "",
                fontSize = 12.sp
            )
        }
        Spacer(modifier = modifier.weight(1f))
        Checkbox(checked = id == panelId, onCheckedChange = null)
    }
}