package g.sig.questweaver.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.datasources.recentgames.RecentGamesLocalDataSource
import g.sig.data.repositories.RecentGamesRepositoryImpl
import g.sig.domain.repositories.RecentGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RecentGamesModule {

    @Provides
    @Singleton
    fun provideRecentGamesDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): RecentGamesDataSource {
        return RecentGamesLocalDataSource(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideRecentGamesRepository(
        recentGamesDataSource: RecentGamesDataSource
    ): RecentGamesRepository {
        return RecentGamesRepositoryImpl(recentGamesDataSource)
    }
}