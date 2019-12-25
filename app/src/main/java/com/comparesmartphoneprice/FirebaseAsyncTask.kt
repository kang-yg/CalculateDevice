package com.comparesmartphoneprice

import android.os.AsyncTask
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAsyncTask : AsyncTask<String, Any, Any>() {
    override fun doInBackground(vararg p0: String?): Any {

        //FirebaseFirestore
        val db = FirebaseFirestore.getInstance()

        getData(db, p0[0].toString())

        return 0
    }
}

//Firebase Firestore에 저장된 데이터 가져오기
fun getData(
    db: FirebaseFirestore,
    manufacturer: String
) {
    var myTempMap: HashMap<String, String> = hashMapOf()
    db.collection("Device").document(manufacturer).get()
        .addOnSuccessListener { documentSnapshot ->
            var str: String = "${documentSnapshot.data}".replace("{", "").replace("}", "")
            var list01: MutableList<String> = str.split(",").toMutableList()

            for (i in 0..list01.size - 1) {
                var list02 = list01[i].split("=")
                myTempMap.put(list02[0], list02[1])
            }

            when (manufacturer) {
                "Samsung" -> {
                    for (i in myTempMap) {
                        GlobalVar.samsungDeviceNameArray.add(i.key)
                        GlobalVar.samsungDeviceCodeNameArray.add(i.value)
                        Log.d("myTempMap_Samsung : ", i.key + "=>" + i.value)
                    }
                }

                "LG" -> {
                    for (i in myTempMap) {
                        GlobalVar.lgDeviceNameArray.add(i.key)
                        GlobalVar.lgDeviceCodeNameArray.add(i.value)
                        Log.d("myTempMap_LG : ", i.key + "=>" + i.value)
                    }
                }

                "Apple" -> {
                    for (i in myTempMap) {
                        GlobalVar.appleDeviceNameArray.add(i.key)
                        GlobalVar.appleDeviceCodeNameArray.add(i.value)
                        Log.d("myTempMap_Apple : ", i.key + "=>" + i.value)
                    }
                }

                "Price" -> {
                    for (i in myTempMap) {
                        GlobalVar.allDevices.add(i.key)
                        GlobalVar.devicesPrice.add(i.value)
                        Log.d("myPriceMap : ", i.key + "=>" + i.value)
                    }
                }
            }
        }
}