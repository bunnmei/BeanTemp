package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimeSet(
    up: () -> Unit,
    down: () -> Unit,
    time: String,
    unit: String,
    modifier : Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(60.dp)
            .height(180.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = modifier
                .height(60.dp)
                .width(60.dp)
                .clickable { up() },
            contentAlignment = Alignment.Center
        ){
            Icon(imageVector = Icons.Default.ExpandLess, contentDescription = "Up")
        }
        Box(
            modifier = modifier
                .height(60.dp)
                .width(60.dp)
                .background(
                    MaterialTheme.colors.secondary,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = time,
                fontSize = 24.sp
            )
        }
        Box(
            modifier = modifier
                .height(60.dp)
                .width(60.dp)
                .clickable { down() },
            contentAlignment = Alignment.Center
        ){
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Down")
        }
    }
    Column(
        modifier = modifier
            .width(60.dp)
            .height(180.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = unit,
            fontSize = 24.sp
        )
    }
}