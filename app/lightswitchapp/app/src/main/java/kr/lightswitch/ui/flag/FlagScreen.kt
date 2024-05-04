package kr.lightswitch.ui.flag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FlagScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
      Text(text = "Flag Screen 화면입니다.")
    } 
}