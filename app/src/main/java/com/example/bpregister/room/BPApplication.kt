package com.example.bpregister.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BPApplication:Application() {

    val db :BpDatabase by lazy {
        BpDatabase.getDatabase(this,applicationScope)
    }
    val repo: BpRepository by lazy {
        BpRepository(db.getBpItemDao())
    }

    private val applicationScope = CoroutineScope(SupervisorJob())
}