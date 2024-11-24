package com.example.bpregister.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BPApplication:Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private lateinit var db: BpDb
    private lateinit var repo: BloodPressureReadingRepository

    override fun onCreate() {
        super.onCreate()
        // Database is initialized when the application starts
        db = BpDb.getDatabase(this, applicationScope)
        repo = BloodPressureReadingRepository(db.getBloodPressureReadingDao())
    }

    fun getRepo(): BloodPressureReadingRepository {
        return repo
    }
    fun getDb(): BpDb {
        return db
    }
}