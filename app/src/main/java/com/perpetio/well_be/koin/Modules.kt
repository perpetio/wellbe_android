package com.perpetio.well_be.koin

import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.data.DataStoreManager
import com.perpetio.well_be.network.RestApiBuilder
import com.perpetio.well_be.network.WebSocketBuilder
import com.perpetio.well_be.repo.auth.AuthRepository
import com.perpetio.well_be.repo.auth.AuthRepositoryImpl
import com.perpetio.well_be.repo.chat.ChatRepository
import com.perpetio.well_be.repo.chat.ChatRepositoryImpl
import com.perpetio.well_be.repo.post.PostRepository
import com.perpetio.well_be.repo.post.PostRepositoryImpl
import com.perpetio.well_be.repo.user.UserRepository
import com.perpetio.well_be.repo.user.UserRepositoryImpl
import com.perpetio.well_be.ui.auth.viewmodel.AuthViewModel
import com.perpetio.well_be.ui.main.viewmodel.ChatViewModel
import com.perpetio.well_be.ui.main.viewmodel.PostViewModel
import com.perpetio.well_be.ui.main.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { PostViewModel(get(), get()) }
    viewModel { ChatViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}

val networkModule = module {
    single { RestApiBuilder(get()) }
    single { WebSocketBuilder(get()) }
}

val accountModule = module {
    single { DataStoreManager(get()) }
    single { AccountModule(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
}