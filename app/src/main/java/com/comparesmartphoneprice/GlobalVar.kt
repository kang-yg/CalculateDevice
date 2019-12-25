package com.comparesmartphoneprice

import android.app.Application

class GlobalVar : Application() {
    companion object{
        var samsungDeviceNameArray: ArrayList<String> = arrayListOf()
        var samsungDeviceCodeNameArray: ArrayList<String> = arrayListOf()
        var lgDeviceNameArray: ArrayList<String> = arrayListOf()
        var lgDeviceCodeNameArray: ArrayList<String> = arrayListOf()
        var appleDeviceNameArray: ArrayList<String> = arrayListOf()
        var appleDeviceCodeNameArray: ArrayList<String> = arrayListOf()
        var allDevices : ArrayList<String> = arrayListOf<String>()
        var devicesPrice : ArrayList<String> = arrayListOf<String>()
    }
}