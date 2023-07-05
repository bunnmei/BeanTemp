package space.webkombinat.coffeeLogger.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileEntity
import space.webkombinat.coffeeLogger.ViewModel.LoggedListScreenVM

@Composable
fun LoggedListScreen(
    modifier: Modifier = Modifier,
    vm: LoggedListScreenVM,
    jump:(Int) -> Unit
) {
    val data = vm.allRoastProfile.collectAsState(initial = emptyList())
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
    ) {
        itemsIndexed(data.value) { i, item ->
            ProfileCard(item = item, jump = jump)
            if(data.value.size - 1 == i) {
                Spacer(modifier = modifier.height(76.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    item: RoastProfileEntity,
    jump: (Int) -> Unit
) {
    Card(
        onClick = {
            jump(item.id)
        },
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(text = item.name ?: "ななし")
        Text(text = "作成日:${item.create_at}")
        Text(text = "説明:${item.description ?: "特になし"}")

    }
}