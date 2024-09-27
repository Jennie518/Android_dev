package com.example.lab4

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab4.ui.theme.Lab4Theme
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: JokeViewModel by viewModels {
            JokeViewModelFactory((application as JokeApplication).jokeRepository)
        }

        setContent {
            Lab4Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),  // 调整整体间距
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(vm)
                }
            }
        }
    }
}

@Composable
fun MainContent(vm: JokeViewModel) {
    val currentJoke by vm.currentJoke.observeAsState()
    val allJokes by vm.allJokes.observeAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(10.dp)) // 增加顶部间距

        Text(
            text = "Chuck Norris Jokes",
            fontSize = 28.sp,  // 调整字体大小
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary  // 使用主题色
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp)  // 轻微阴影
        ) {
            Text(
                text = currentJoke?.value ?: "No joke yet, click the button!",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(
            onClick = {
                scope.launch {
                    val newJoke = getJokeCoroutine()
                    vm.addJoke(newJoke)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)  // 调整按钮宽度
                .padding(8.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Get Another Joke", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Previous Jokes",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.secondary  // 使用次要颜色
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.Gray)  // 边框样式
        ) {
            allJokes?.let { jokes ->
                items(jokes.size) { index ->
                    Text(
                        text = jokes[index].value,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

data class JokeResult(var value: String)

//class JokeViewModel : ViewModel() {
//    private val _data = MutableLiveData<List<JokeResult>>(listOf())
//    val data = _data as LiveData<List<JokeResult>>
//    fun addData(newVal: JokeResult) {
//        _data.value = _data.value?.plus(newVal)
//        Log.e("UPDATE", "${data.value?.size}")
//    }
//}

suspend fun getJokeCoroutine(): JokeResult {
    return withContext(Dispatchers.IO) {

        val url: Uri = Uri.Builder().scheme("https")
            .authority("api.chucknorris.io")
            .appendPath("jokes")
            .appendPath("random").build()

        val conn = URL(url.toString()).openConnection() as HttpURLConnection
        conn.connect()
        val gson = Gson()
        val result = gson.fromJson(
            InputStreamReader(conn.inputStream, "UTF-8"),
            JokeResult::class.java
        )
        Log.e("data!", gson.toJson(result).toString())
        result
    }
}
