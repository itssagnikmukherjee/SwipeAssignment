package com.itssagnikmukherjee.swipeassignment.data.di
import android.app.Application
import androidx.room.Room
import com.itssagnikmukherjee.swipeassignment.data.api.SwipeApi
import com.itssagnikmukherjee.swipeassignment.data.local.database.AppDatabase
import com.itssagnikmukherjee.swipeassignment.domain.repo.ProductRepo
import com.itssagnikmukherjee.swipeassignment.ui.viewmodel.ProductListViewModel
import com.itssagnikmukherjee.swipeassignment.utils.ApiConstants
import com.itssagnikmukherjee.swipeassignment.utils.NetworkConnectivityObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    //database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "swipe_database"
        ).build()
    }

    single { get<AppDatabase>().pendingProductDao() }
    single { get<AppDatabase>().cachedProductDao() }

    // Connectivity
    single { NetworkConnectivityObserver(androidContext()) }

    // Repository
    single {
        ProductRepo(
            api = get(),
            cachedProductDao = get(),
            pendingProductDao = get(),
            connectivityObserver = get()
        )
    }

    single {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SwipeApi::class.java)
    }
    viewModel { ProductListViewModel(repo = get(), connectivityObserver = get(), application = androidContext() as Application) }
}