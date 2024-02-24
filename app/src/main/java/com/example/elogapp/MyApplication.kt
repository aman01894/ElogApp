package com.example.elogapp

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.util.Log
import com.example.devicemanager.App
import com.example.devicemanager.AppModel
import com.example.devicemanager.DefaultLogger
import com.example.elogapp.activity.auth.AuthViewModelFactory
import com.example.elogapp.activity.auth.ReAuthViewModelFactory
import com.example.elogapp.activity.ui.company_info.CompanyInfoViewModelFactory
import com.example.elogapp.activity.ui.dalily_log.DailyLogChartViewModelFactory
import com.example.elogapp.activity.ui.dalily_log.DailyLogViewModelFactory
import com.example.elogapp.activity.ui.dispatch.DispatchHomeViewModelFactory
import com.example.elogapp.activity.ui.document.DocGalleryViewModelFactory
import com.example.elogapp.activity.ui.document.DocumentLoadListViewModelFactory
import com.example.elogapp.activity.ui.dvir.NewDvirViewModelFactory
import com.example.elogapp.activity.ui.dvir_pretrip.DvirPreTripViewModelFactory
import com.example.elogapp.activity.ui.exception.ExceptionsViewModelFactory
import com.example.elogapp.activity.ui.home.HomeViewModelFactory
import com.example.elogapp.activity.ui.loads.closed_load.ClosedLoadsListViewModelFactory
import com.example.elogapp.activity.ui.loads.open_load.OpenLoadItemViewModelFactory
import com.example.elogapp.activity.ui.loads.open_load.OpenLoadListViewModelFactory
import com.example.elogapp.activity.ui.loads.open_payment.OpenPaymentViewModelFactory
import com.example.elogapp.activity.ui.loads.payment_history.PaymentHistoryViewModelFactory
import com.example.elogapp.activity.ui.loads.pending_load.PendingLoadItemViewModelFactory
import com.example.elogapp.activity.ui.loads.pending_load.PendingLoadsListViewModelFactory
import com.example.elogapp.activity.ui.logout.LogoutViewModelFactory
import com.example.elogapp.activity.ui.shippingdocs.ShippingDocsViewModelFactory
import com.example.elogapp.activity.ui.unidentifiedevent.UnidentifiedEventViewModelFactory
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.repository.*
import com.example.elogapp.util.OutboundService
import com.example.elogapp.util.network.AuthInterceptor
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.pref.UserPreference
import com.pt.sdk.Sdk
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {


    val CONNECTED_DEVICE_CHANNEL = "connected_device_channel"
    val UPDATE_CHANNEL = "update_channel"


    var mModel: AppModel? = null

    override val kodein = Kodein.lazy {

        import(androidXModule(this@MyApplication))


        bind() from singleton { AuthInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { MyRoomDb(instance()) }
        bind() from singleton { UserPreference(instance()) }


        bind() from singleton {UserRepository(instance(),instance()) }
        bind() from singleton { HomeRepository(instance(),instance()) }
        bind() from singleton { NewDvirRepository(instance(),instance()) }
        bind() from singleton { PreTripDvirRepository(instance(),instance()) }
        bind() from singleton { LoadRepository(instance(),instance()) }
        bind() from singleton { CompanyInfoRepository(instance(),instance()) }
        bind() from singleton { ViolationRepository(instance(),instance()) }
        bind() from singleton { ExceptionRepository(instance(),instance()) }
        bind() from singleton { DailyLogRepository(instance(),instance()) }
        bind() from singleton { DocGalleryRepository(instance(),instance()) }
        bind() from singleton { ShippingDocsRepository(instance(),instance()) }
        bind() from singleton { UnidentifiedDataRepository(instance(),instance()) }

        bind() from provider {AuthViewModelFactory(instance())}
        bind() from provider { ReAuthViewModelFactory(instance()) }
        bind() from provider {HomeViewModelFactory(instance()) }
        bind() from provider {NewDvirViewModelFactory(instance()) }
        bind() from provider { DvirPreTripViewModelFactory(instance()) }
        bind() from provider { PendingLoadsListViewModelFactory(instance()) }
        bind() from provider { PendingLoadItemViewModelFactory(instance()) }
        bind() from provider { CompanyInfoViewModelFactory(instance()) }
        bind() from provider { DocumentLoadListViewModelFactory(instance()) }
        bind() from provider { OpenLoadListViewModelFactory(instance()) }
        bind() from provider { OpenLoadItemViewModelFactory(instance()) }
        bind() from provider { ClosedLoadsListViewModelFactory(instance()) }
        bind() from provider { OpenPaymentViewModelFactory(instance()) }
        bind() from provider { PaymentHistoryViewModelFactory(instance()) }
        bind() from provider { LogoutViewModelFactory(instance()) }
        bind() from provider { ExceptionsViewModelFactory(instance()) }
        bind() from provider { UnidentifiedEventViewModelFactory(instance()) }
        bind() from provider { DailyLogViewModelFactory(instance()) }
        bind() from provider { DailyLogChartViewModelFactory(instance()) }
        bind() from provider { DocGalleryViewModelFactory(instance()) }
        bind() from provider { ShippingDocsViewModelFactory(instance()) }
        bind() from provider { DispatchHomeViewModelFactory(instance()) }
    }


    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

//        if (BuildConfig.DEBUG){
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()
//                    .penaltyLog()
//                    .build()
//            )
//            StrictMode.setVmPolicy(
//                StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build()
//            )
//        }

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // PT sdk init
        Sdk.getInstance().logger = DefaultLogger()
        Sdk.getInstance().initialize(this)
        // Note: For PT managed service user, provide your API key here or update the app/build.gradle
        Sdk.getInstance().apiKey = BuildConfig.API_KEY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //DfuServiceInitiator.createDfuNotificationChannel(this);
            val channel = NotificationChannel(
                App.CONNECTED_DEVICE_CHANNEL,
                getString(R.string.channel_connected_devices_title),
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = getString(R.string.channel_connected_devices_description)
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val upd_channel = NotificationChannel(
                App.UPDATE_CHANNEL,
                getString(R.string.channel_update_title),
                NotificationManager.IMPORTANCE_LOW
            )
            upd_channel.description = getString(R.string.channel_update_description)
            upd_channel.setShowBadge(false)
            upd_channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(upd_channel)
        }
        Log.d(AppModel.TAG, "App created ...")
        mModel = AppModel.getInstance()


    }



    override fun onTerminate() {
        super.onTerminate()
        Log.d(AppModel.TAG, "App terminated. ----------")
    }

}