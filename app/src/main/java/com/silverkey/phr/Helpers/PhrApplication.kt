package com.silverkey.phr.Helpers

import android.app.Application
import android.graphics.Typeface
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 * Created by Mohamed Taher on 12/28/2017.
 */
class PhrApplication : Application() {
    //TODO remove this lines when no memory leak in the app

    var coconFont: Typeface? = null
    //(1)
    var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        //(2)
        refWatcher = LeakCanary.install(this)
        initFonts()
    }

    private fun initFonts() {
        coconFont = Typeface.createFromAsset(assets, "fonts/cocon.otf")
    }
}