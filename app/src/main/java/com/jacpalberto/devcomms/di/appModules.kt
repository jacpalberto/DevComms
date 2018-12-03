package com.jacpalberto.devcomms.di

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.BuildConfig
import com.jacpalberto.devcomms.DevCommsDatabase
import com.jacpalberto.devcomms.address.AddressModel
import com.jacpalberto.devcomms.address.AddressViewModel
import com.jacpalberto.devcomms.eventDetail.EventDetailViewModel
import com.jacpalberto.devcomms.events.EventsModel
import com.jacpalberto.devcomms.events.EventsRepository
import com.jacpalberto.devcomms.events.EventsViewModel
import com.jacpalberto.devcomms.sponsors.SponsorRepository
import com.jacpalberto.devcomms.sponsors.SponsorsModel
import com.jacpalberto.devcomms.sponsors.SponsorsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Alberto Carrillo on 10/10/18.
 */
val sponsorsModule = module {
    viewModel { SponsorsViewModel(get()) }
    single { SponsorRepository(get()) }
    single { SponsorsModel(get(), get()) }
    single { get<DevCommsDatabase>().sponsorsDao() }
}
val eventsModule = module {
    viewModel { EventsViewModel(get()) }
    viewModel { EventDetailViewModel() }
    single { EventsModel(get(), get()) }
    single { EventsRepository(get()) }
    single { get<DevCommsDatabase>().eventsDao() }
}
val addressModule = module {
    viewModel { AddressViewModel(get()) }
    single { AddressModel(get()) }
}
val generalModule = module {
    single {
        Room.databaseBuilder(androidApplication(),
                DevCommsDatabase::class.java,
                BuildConfig.roomDbName)
                .allowMainThreadQueries()
                .build()
    }
    single { FirebaseFirestore.getInstance() }
}
