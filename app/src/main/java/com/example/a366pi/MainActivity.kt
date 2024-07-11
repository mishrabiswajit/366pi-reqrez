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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
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

@Composable
fun MyApp() {
    var showAddUserPage by remember { mutableStateOf(false) }
    val users = remember { mutableStateOf(listOf<User>()) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMessage by remember { mutableStateOf("") }

    // Initialization on app launch
    LaunchedEffect(Unit) {
        scope.launch {
            try {

                // Getting user response [GET]
                val response = RetrofitInstance.api.getUsers()
                users.value = response.data

            } catch (e: Exception) {

                // Printing error message if no internet connection available
                errorMessage = "No internet connection"
            }
        }
    }

    if (showAddUserPage) {
        AddUserPage(
            onBack = { showAddUserPage = false },
            onUserAdded = { newUser ->
                users.value += newUser
                showAddUserPage = false
            }
        )
    } else {

        // Displaying HomePage when app opens at first
        HomePage(
            users = users.value, // passing the list of users which we got from UserResponse [GET]
            errorMessage = errorMessage,
            onAddUserClicked = { showAddUserPage = true },
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    users: List<User>, //
    errorMessage: String, //
    onAddUserClicked: () -> Unit,
    snackbarHostState: SnackbarHostState //
) {
    Scaffold(

        // Top Bar Section
        topBar = {
            TopAppBar(

                // Top Bar - Title
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // Top Bar - Title - Logo
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )

                        // Top Bar - Title - App Name
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("366pi", color = Color.White)
                    }
                },

                // Top Bar - Title - Styling
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },

        // Initializing SnackbarHost
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        // Adding Floating Add Button
        floatingActionButton = {
            FloatingActionButton(onClick = onAddUserClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            // If the error message is not empty
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
            // If the error message is empty proceed with the working
            else {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Passing fetched users data [GET] for displaying
                    UserList(users)
                }
            }
        }
    }
}

// Getting the list of users
@Composable
fun UserList(users: List<User>) {
    LazyColumn {

        // Iterating through each user
        items(users) { user ->

            // Passing each user data for displaying in HomePage
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
            painter = rememberAsyncImagePainter(user.avatar),
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

// Using experimental material3 api for TopAppBar as old is depreciated
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserPage(onBack: () -> Unit, onUserAdded: (User) -> Unit) {
    var employeeFirstname by remember { mutableStateOf("") }
    var employeeLastname by remember { mutableStateOf("") }
    var employeePosition by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    // Regular expression to allow only letters
    val namePattern = Regex("^[a-zA-Z]*$")

    Scaffold(

        // Top-bar Section
        topBar = {
            TopAppBar(

                // topbar - title
                title = { Text("Add User") },

                // topbar - styling
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),

                // topbar - navigation [back button]
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }

            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Asking for first name
            TextField(
                value = employeeFirstname,
                onValueChange = {

                    // Checks for invalid characters
                    scope.launch {
                        if (namePattern.matches(it)) {
                            employeeFirstname = it
                        } else {
                            snackbarHostState.showSnackbar("Name cannot contain special characters or numbers")
                        }
                    }
                },
                label = { Text("Enter your First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Asking for last name
            TextField(
                value = employeeLastname,
                onValueChange = {

                    // Checks for invalid characters
                    scope.launch {
                        if (namePattern.matches(it)) {
                            employeeLastname = it
                        } else {
                            snackbarHostState.showSnackbar("Name cannot contain special characters or numbers")
                        }
                    }
                },
                label = { Text("Enter your Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Asking for position
            TextField(
                value = employeePosition,
                onValueChange = { employeePosition = it },
                label = { Text("Enter your Position at the company") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // AddUser Button
            Button(
                onClick = {
                    scope.launch {
                        if (employeeFirstname.isEmpty()) {
                            snackbarHostState.showSnackbar("Employee First Name cannot be empty")
                        }else if (employeeLastname.isEmpty()) {
                            snackbarHostState.showSnackbar("Employee Last Name cannot be empty")
                        }else if (employeePosition.isEmpty()) {
                            snackbarHostState.showSnackbar("Employee Position cannot be empty")
                        } else {
                            try {
                                val response = RetrofitInstance.api.createUser(CreateUserRequest(employeeFirstname, employeeLastname, employeePosition))

                                // Console log
                                println("User created: ${response.employeeFirstname} ${response.employeeLastname}")

                                // User Output
                                snackbarHostState.showSnackbar("User created: ${response.employeeFirstname} ${response.employeeLastname}")

                                // PUSH
                                onUserAdded(User(response.id.toInt(),response.employeePosition,response.employeeFirstname, response.employeeLastname,""))
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error creating user")
                            }
                        }
                    }
                },

                // AddUser Button - Styling
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
