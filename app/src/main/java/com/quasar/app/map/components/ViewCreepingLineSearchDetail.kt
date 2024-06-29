package com.quasar.app.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quasar.app.map.models.Distance
import com.quasar.app.map.models.DistanceUnit
import com.quasar.app.map.models.Heading
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.SearchLeg
import com.quasar.app.map.models.SearchPattern
import com.quasar.app.map.models.Speed
import com.quasar.app.map.models.SpeedUnit

@Composable
fun ViewSearchPatternDetailSheet(
    search: SearchPattern,
    modifier: Modifier = Modifier
) {
    val legs = search.legs

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Creeping Line Search", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(legs) { index, leg ->
                SearchLegRow(leg, index + 1)
            }
        }
    }
}

@Composable
fun SearchLegRow(leg: SearchLeg, rowNumber: Int, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        .padding(4.dp)
        .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(4.dp))
        .padding(8.dp)
    ) {
        Text(rowNumber.toString())
        Text("${leg.heading.value}°")
        Text("${leg.speed}")
        Text("${leg.distance}")
        Text("${leg.time} sec")
    }
}

private val searchPattern = SearchPattern.createCreepingLineSearch(
    startPoint = Position(0.0, 0.0),
    trackDirection = Heading(0),
    speed = Speed(10.0, SpeedUnit.Kts),
    legCount = 8,
    legDistance = Distance(500.0, DistanceUnit.Metres),
    sweepWidth = Distance(100.0, DistanceUnit.Metres)
)

@Composable
@Preview(showSystemUi = true)
private fun Preview() {
    ViewSearchPatternDetailSheet(search = searchPattern)
}