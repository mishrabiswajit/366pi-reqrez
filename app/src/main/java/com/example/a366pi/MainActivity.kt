package com.example.a366pi

// Importing necessary packages
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

// Driver code
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

// Using experimental material3 api for TopAppBar as old is depreciated
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {

    // Initializing Variables
    val users = remember { mutableStateOf(listOf<User>()) }
    val scope = rememberCoroutineScope()
    var employeeName by remember { mutableStateOf("") }
    var employeePost by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMessage by remember { mutableStateOf("") }

    // Internet Check On Launch
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitInstance.api.getUsers()
                users.value = response.data
            } catch (e: Exception) {
                errorMessage = "No internet connection"
            }
        }
    }

    Scaffold(

        // Top Bar Section
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // Top Bar - Logo
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )

                        // Top Bar - App Name
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("366pi", color = Color.White)
                    }
                },

                // Top Bar - Styling
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },

        // Initializing SnackbarHost
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            // If the errormessage is not empty
            if (errorMessage.isNotEmpty()) {

                // Show error message with a sad emoji
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sad_emoji),
                        contentDescription = "Sad Emoji",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = Color.Red)
                }
            }
            // If the errormessage is empty proceed with the working
            else {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Displaying fetched users data [GET]
                    UserList(users.value)

                    // Asking for Employee Name
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = employeeName,
                        onValueChange = { employeeName = it },
                        label = { Text("Enter your Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Asking for Employee Post
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = employeePost,
                        onValueChange = { employeePost = it },
                        label = { Text("Enter your Post") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Add user button [POST]
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            scope.launch {

                                // Emplty Field Checks
                                if (employeeName.isEmpty()){
                                    snackbarHostState.showSnackbar("Employee Name cannot be empty")
                                }
                                else if(employeePost.isEmpty()){
                                    snackbarHostState.showSnackbar("Employee Post cannot be empty")
                                }

                                // If both the field is filled then [PUSH]
                                else{
                                    try {
                                        val response = RetrofitInstance.api.createUser(CreateUserRequest(employeeName, employeePost))

                                        // Console Level Output
                                        println("User created: ${response.employeeName} with post ${response.employeePost}")

                                        // Snackbar output
                                        snackbarHostState.showSnackbar("User created: ${response.employeeName} with post ${response.employeePost}")
                                    } catch (e: Exception) {
                                        snackbarHostState.showSnackbar("Error creating user")
                                    }

                                }
                            }
                        },

                        // Button - Styling
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        Text("Add User")
                    }
                }
            }
        }
    }
}

// Getting the list of users from reqres [GET]
@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            UserItem(user)
        }
    }
}

// Displaying the fetched users data
@Composable
fun UserItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // UserItem - Image
        Image(
            painter = rememberImagePainter(user.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))

        // UserItem - Details
        Column {
            Text(text = "${user.first_name} ${user.last_name}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
