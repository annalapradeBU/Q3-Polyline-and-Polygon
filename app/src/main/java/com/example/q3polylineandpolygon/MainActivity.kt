package com.example.q3polylineandpolygon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.q3polylineandpolygon.ui.theme.Q3PolylineAndPolygonTheme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
// https://developer.android.com/reference/android/widget/Toast
// looked cleaner
import android.widget.Toast



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PolylinePolygonMap()
                }
            }
        }
    }
}

@Composable
fun PolylinePolygonMap(){
    val context = LocalContext.current

    // coordinates for the trail (attempting to do the Esplanade in Boston
    val trailPoints = remember {
        listOf(
            LatLng(42.3526, -71.1107), // start: near BU Bridge / Beach St
            LatLng(42.3510, -71.1030), // passing by the BU Sailing Pavilion
            LatLng(42.3501, -71.0955), // near the Mass Ave Bridge (Harvard Bridge)
            LatLng(42.3535, -71.0880), // following the curve near Back Bay
            LatLng(42.3562, -71.0795), // near the Arthur Fiedler Footbridge
            LatLng(42.3585, -71.0740), // passing the Hatch Shell
            LatLng(42.3610, -71.0705)  // end: Near the Longfellow Bridge

        )
    }

    // park area coordinates (trying to do an area in the Fens)
    val parkPoints = remember {
        listOf(
            LatLng(42.3425, -71.0935),
            LatLng(42.3450, -71.0935),
            LatLng(42.3450, -71.0895),
            LatLng(42.3425, -71.0895)
        )
    }

    // requirement 3: state variables for ui customization
    var isRed by remember { mutableStateOf(false) }
    var isThick by remember { mutableStateOf(false) }
    var isYellowPark by remember { mutableStateOf(false) }

    // derive visual properties from the current state
    val polylineColor = if (isRed) Color.Red else Color.Blue
    val polylineWidth = if (isThick) 20f else 8f
    val polygonColor = if (isYellowPark) Color.Yellow.copy(alpha = 0.5f) else Color.Green.copy(alpha = 0.4f)

    // set initial camera view to focus on the trail
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(trailPoints.first(), 14f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // requirement: display a Polyline
            Polyline(
                points = trailPoints,
                color = polylineColor,
                width = polylineWidth,
                clickable = true,
                // requirement 4: Click Listener
                onClick = {
                    // toast from ch. 9-- just looks cleaner
                    Toast.makeText(context, "Hiking Trail: The Esplanade", Toast.LENGTH_SHORT).show()
                }
            )

            // requirement 2: polygon
            Polygon(
                points = parkPoints,
                fillColor = polygonColor,
                strokeColor = Color.DarkGray,
                strokeWidth = 2f,
                clickable = true,
                // requirement 4 but again, clickable
                onClick = {
                    Toast.makeText(context, "Area of Interest: Back Bay Fens", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // requirement 3: UI for customization!
        // floating toggle card ooooooo
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .width(200.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Map Controls", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                // toggle the color
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Red Trail", modifier = Modifier.weight(1f))
                    Switch(checked = isRed, onCheckedChange = { isRed = it })
                }

                // toggle the thickness
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Thick Line", modifier = Modifier.weight(1f))
                    Switch(checked = isThick, onCheckedChange = { isThick = it })
                }

                // toggle the park color
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Yellow Park", modifier = Modifier.weight(1f))
                    Switch(checked = isYellowPark, onCheckedChange = { isYellowPark = it })
                }
            }
        }


    }

}





