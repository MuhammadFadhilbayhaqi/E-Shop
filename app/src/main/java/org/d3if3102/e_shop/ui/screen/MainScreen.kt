package org.d3if3102.e_shop.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3102.e_shop.BuildConfig
import org.d3if3102.e_shop.R
import org.d3if3102.e_shop.model.Barang
import org.d3if3102.e_shop.model.User
import org.d3if3102.e_shop.navigation.Screen
import org.d3if3102.e_shop.network.ApiStatus
import org.d3if3102.e_shop.network.BarangApi
import org.d3if3102.e_shop.network.UserDataStore
import org.d3if3102.e_shop.ui.theme.EShopTheme


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen() {
//    val context = LocalContext.current
//    val dataStore = UserDataStore(context)
//    val user by dataStore.userFlow.collectAsState(User())
//
//    val viewModel: MainViewModel = viewModel()
//    val errorMessage by viewModel.errorMessage
//
//    var showDialog by remember { mutableStateOf(false)}
//    var showBarangDialog by remember { mutableStateOf(false)}
//
//    var bitmap: Bitmap? by remember { mutableStateOf(null) }
//    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
//        bitmap = getCroppedImage(context.contentResolver, it)
//        if (bitmap != null) showBarangDialog = true
//    }
//
//    Scaffold (
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = stringResource(id = R.string.app_name))
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                actions = {
//                    IconButton(onClick = {
//                        if (user.email.isEmpty()) {
//                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
//                        }
//                        else {
//                            showDialog = true
//                        }
//                    }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.account_circle),
//                            contentDescription = stringResource(id = R.string.profil),
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                val options = CropImageContractOptions(
//                    null, CropImageOptions(
//                        imageSourceIncludeGallery = false,
//                        imageSourceIncludeCamera = true,
//                        fixAspectRatio = true
//                    )
//                )
//                launcher.launch(options)
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = stringResource(id = R.string.tambah_barang))
//            }
//        }
//    ){ padding ->
//        Box(modifier = Modifier.padding(padding)) {
//            ScreenContent(viewModel, user.email,Modifier.padding((padding)))
//
//            if (showDialog) {
//                ProfilDialog(
//                    user = user,
//                    onDismissRequest = { showDialog = false }) {
//                    CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
//                    showDialog = false
//                }
//            }
//
//            if (showBarangDialog) {
//                BarangDialog(
//                    bitmap = bitmap,
//                    onDismissRequest = {showBarangDialog = false}) { namaBarang, hargaBeli, hargaJual, stok, keterangan ->
//                    viewModel.saveData(user.email, namaBarang, hargaBeli, hargaJual, stok, keterangan, bitmap!!)
//                    showBarangDialog = false
//                }
//            }
//
//            if (errorMessage != null) {
//                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                viewModel.clearMessage()
//            }
//        }
//    }
//}
//
//@Composable
//fun ScreenContent(viewModel: MainViewModel, userId: String,modifier: Modifier) {
//    val data by viewModel.data
//    val status by viewModel.status.collectAsState()
//
//    LaunchedEffect(userId) {
//        viewModel.retrieveData(userId)
//    }
//
//    when (status) {
//        ApiStatus.LOADING -> {
//            Box (
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ){
//                CircularProgressIndicator()
//            }
//        }
//        ApiStatus.SUCCES -> {
//            LazyVerticalGrid(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(4.dp),
//                columns = GridCells.Fixed(1),
//                contentPadding = PaddingValues(bottom = 80.dp)
//            ){
//                items(data) { ListItem(barang = it)}
//            }
//        }
//        ApiStatus.FAILED -> {
//            Column (
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ){
//                Text(text = stringResource(id = R.string.error))
//                Button(
//                    onClick = { viewModel.retrieveData(userId) },
//                    modifier = Modifier.padding(top = 16.dp),
//                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
//                ) {
//                    Text(text = stringResource(id = R.string.try_again))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ListItem(barang: Barang) {
//    Box(
//        modifier = Modifier
//            .padding(8.dp)
//            .border(1.dp, Color.Black)
//            .padding(8.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Gambar Barang
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(BarangApi.getBarangUrl(barang.imageId))
//                    .crossfade(true)
//                    .build(),
//                contentDescription = stringResource(id = R.string.gambar, barang.namaBarang),
//                contentScale = ContentScale.Fit,
//                placeholder = painterResource(id = R.drawable.loading_img),
//                error = painterResource(id = R.drawable.baseline_broken),
//                modifier = Modifier
//                    .size(64.dp)
//                    .padding(8.dp)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            // Informasi Barang
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = barang.namaBarang,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Row {
//                    Text(
//                        text = "Cost: ${barang.hargaBeli}",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Text(
//                        text = "Sell: ${barang.hargaJual}",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//                Spacer(modifier = Modifier.height(4.dp))
//                Row {
//                    Text(
//                        text = "Stok: ${barang.stok}",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            // Ikon Edit dan Delete
//            Row(
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Edit,
//                    contentDescription = stringResource(id = R.string.edit),
//                    tint = Color.Blue,
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Icon(
//                    imageVector = Icons.Filled.Delete,
//                    contentDescription = stringResource(id = R.string.delete),
//                    tint = Color.Red,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//    }
//}
//
//
//private suspend fun signIn(context: Context, dataStore: UserDataStore) {
//    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(false)
//        .setServerClientId(BuildConfig.API_KEY)
//        .build()
//
//    val request : GetCredentialRequest = GetCredentialRequest.Builder()
//        .addCredentialOption(googleIdOption)
//        .build()
//
//    try {
//        val credentialManager = CredentialManager.create(context)
//        val result = credentialManager.getCredential(context, request)
//        handleSignIn(result, dataStore)
//    } catch (e: GetCredentialException) {
//        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
//    }
//}
//
//private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
//    val credential = result.credential
//    if (credential is CustomCredential &&
//        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//        try {
//            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
//            val name = googleId.displayName?: ""
//            val email = googleId.id
//            val photoUrl = googleId.profilePictureUri.toString()
//            dataStore.saveData(User(name, email, photoUrl))
//        } catch (e: GoogleIdTokenParsingException) {
//            Log.e("SIGN-IN", "Error: ${e.message}")
//        }
//    } else {
//        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
//    }
//}
//
//private suspend fun signOut(context: Context, dataStore: UserDataStore) {
//    try {
//        val credentialManager = CredentialManager.create(context)
//        credentialManager.clearCredentialState(
//            ClearCredentialStateRequest()
//        )
//        dataStore.saveData(User())
//    } catch (e: ClearCredentialException) {
//        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
//    }
//}
//
//private fun getCroppedImage(
//    resolver: ContentResolver,
//    result: CropImageView.CropResult
//): Bitmap? {
//    if (!result.isSuccessful) {
//        Log.e("IMAGE", "Error: ${result.error}")
//        return null
//    }
//
//    val uri = result.uriContent ?: return null
//
//    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
//        MediaStore.Images.Media.getBitmap(resolver, uri)
//    } else {
//        val source = ImageDecoder.createSource(resolver, uri)
//        ImageDecoder.decodeBitmap(source)
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }
    var showBarangDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentBarangId by remember { mutableStateOf("") } // Deklarasi di MainScreen

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showBarangDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.AboutProject.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.about_aplication),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.account_circle),
                            contentDescription = stringResource(id = R.string.profil),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = true,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_barang)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Memanggil ScreenContent dengan memasukkan currentBarangId dan showDeleteDialog
            ScreenContent(viewModel, user.email, currentBarangId, showDeleteDialog, { id, show ->
                currentBarangId = id
                showDeleteDialog = show
            }, Modifier.padding(padding))

            if (showDialog) {
                ProfilDialog(
                    user = user,
                    onDismissRequest = { showDialog = false }) {
                    CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                    showDialog = false
                }
            }

            if (showBarangDialog) {
                BarangDialog(
                    bitmap = bitmap,
                    onDismissRequest = { showBarangDialog = false }) { namaBarang, hargaBeli, hargaJual, stok, keterangan ->
                    viewModel.saveData(user.email, namaBarang, hargaBeli, hargaJual, stok, keterangan, bitmap!!)
                    showBarangDialog = false
                }
            }

            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.clearMessage()
            }
        }
    }
}

@Composable
fun ScreenContent(viewModel: MainViewModel, userId: String, currentBarangId: String, showDeleteDialog: Boolean, onDeleteRequest: (String, Boolean) -> Unit, modifier: Modifier) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.SUCCES -> {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { barang ->
                    ListItem(barang = barang, onDeleteRequest = { id ->
                        onDeleteRequest(id, true)
                    })
                }
            }
        }
        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }

    if (showDeleteDialog) {
        DisplayDeleteDialog(
            onDismissRequest = { onDeleteRequest("", false) },
            onConfirm = {
                Log.d("MainScreen", "Deleting Barang ID: $currentBarangId")
                viewModel.deleteData(userId, currentBarangId)
                onDeleteRequest("", false)
            }
        )
    }
}




@Composable
fun ListItem(
    barang: Barang,
    onDeleteRequest: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Black)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Barang
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(BarangApi.getBarangUrl(barang.imageId))
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.gambar, barang.namaBarang),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.baseline_broken),
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Informasi Barang
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = barang.namaBarang,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Sell: ${barang.hargaBeli}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Cost: ${barang.hargaJual}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Stok: ${barang.stok}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Ikon Edit dan Delete
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete),
                    tint = Color.Red,
                    modifier = Modifier
                        .clickable {
                            onDeleteRequest(barang.id)
                        }
                        .size(24.dp)
                )
            }
        }
    }
}




private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request : GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val name = googleId.displayName?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(name, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreePreview() {
    EShopTheme {
        MainScreen(rememberNavController())
    }
}