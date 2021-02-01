package dev.forcetower.playtime.injection

import android.content.Context
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcetower.playtime.BuildConfig
import dev.forcetower.playtime.Constants
import dev.forcetower.playtime.core.source.network.TMDbService
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.util.ObjectConverters
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PlayDB::class.java, "play.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun service(client: OkHttpClient, gson: Gson): TMDbService =
        Retrofit.Builder()
            .baseUrl(Constants.TMDB_SERVICE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(TMDbService::class.java)

    @Provides
    @Singleton
    fun interceptor() = Interceptor { chain ->
        val request = chain.request()
        val host = request.url.host
        if (host.contains(Constants.TMDB_URL, ignoreCase = true)) {
            val headers = request.headers.newBuilder()
                .add("Accept", "application/json")
                .build()

            val url = request.url.newBuilder()
                .addQueryParameter("api_key", Constants.TMDB_API_KEY)
                .apply {
                    if (request.url.queryParameter("language") == null)
                        addQueryParameter("language", Locale.getDefault().toLanguageTag())
                }
                .build()

            val renewed = request.newBuilder().url(url).headers(headers).build()

            chain.proceed(renewed)
        } else {
            val requested = request.newBuilder().addHeader("Accept", "application/json").build()
            chain.proceed(requested)
        }
    }

    @Provides
    @Singleton
    fun client(interceptor: Interceptor) =
        OkHttpClient.Builder()
            .followRedirects(true)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor {
                Timber.d(it)
            }.apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BASIC
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .build()

    @Provides
    @Singleton
    fun gson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, ObjectConverters.LD_DESERIALIZER)
            .registerTypeAdapter(ZonedDateTime::class.java, ObjectConverters.ZDT_DESERIALIZER)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .create()
}