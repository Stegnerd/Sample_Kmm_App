package com.stegnerd.kmmapp.shared

import com.stegnerd.kmmapp.shared.cache.Database
import com.stegnerd.kmmapp.shared.cache.DatabaseDriverFactory
import com.stegnerd.kmmapp.shared.entity.RocketLaunch
import com.stegnerd.kmmapp.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}