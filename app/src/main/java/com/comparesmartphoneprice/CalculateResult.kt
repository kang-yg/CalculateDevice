package com.comparesmartphoneprice

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.calculate_result_activity.*

class CalculateResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculate_result_activity)

        Log.d("ResultCalData_tel", CalculateData.telecom.toString())
        Log.d("ResultCalData_brand", CalculateData.deviceBrand.toString())
        Log.d("ResultCalData_model", CalculateData.deviceModel)
        Log.d("ResultCalData_plan", CalculateData.callingPlan.toString())
        Log.d("ResultCalData_perPlan", CalculateData.periodCallingPlan.toString())
        Log.d("ResultCalData_sup", CalculateData.supportfund.toString())
        Log.d("ResultCalData_per", CalculateData.period.toString())
        Log.d("ResultCalData_store", CalculateData.storefund.toString())
        Log.d("ResultCalData_fam", CalculateData.family.toString())
        Log.d("ResultCalData_wel", CalculateData.welfare.toString())

        setTelecom()
        setDeviceModel()
        setSupportfund()
        setDevicePrice()
        setPeriodTitle()
        setStorefund()
        setFamilyDis()
        setWelfare()
        setFinalDevice()
        setFinalTwoYear()
    }

    //통신사 TextView
    fun setTelecom() {
        when (CalculateData.telecom) {
            0 -> result_telecom.text = "SKT"
            1 -> result_telecom.text = "LG U+"
            2 -> result_telecom.text = "KT"
        }
    }

    //디바이스 TextView
    fun setDeviceModel() {
        result_device.text = CalculateData.deviceModel
    }

    //공시지원 선택지원 TextView
    fun setSupportfund() {
        when (CalculateData.supportfund) {
            0 -> {
                result_support.text = getString(R.string.result_public)
            }

            1 -> {
                result_support.text = getString(R.string.result_choice)
            }
        }
    }

    //출고가 TextView
    fun setDevicePrice() {
        factoryprice_content.text = matchPrice().toString()
    }

    //4개월 or 6개월 요금제 TextView
    fun setPeriodTitle() {
        when (CalculateData.supportfund) {
            0 -> {
                result_period_callingplan.visibility = View.VISIBLE
                result_period_callingplan_title.text = getText(R.string.result_SixMonth)
                result_period_callingplan_price.text = (CalculateData.callingPlan * 6).toString()

                result_period_callingplan_remainder.visibility = View.VISIBLE
                result_period_callingplan_remainder_title.text = getString(R.string.Six_remainder)
                result_period_callingplan_remainder_price.text =
                    (CalculateData.periodCallingPlan * 18).toString()

                fullCallingplan()
            }

            1 -> {
                result_period_callingplan.visibility = View.VISIBLE
                result_period_callingplan_title.text = getString(R.string.result_FourMonth)
                result_period_callingplan_price.text =
                    ((CalculateData.callingPlan * 0.75) * 4).toInt().toString()
                result_period_callingplan_remainder.visibility = View.VISIBLE
                result_period_callingplan_remainder_title.text = getString(R.string.Four_remainder)
                result_period_callingplan_remainder_price.text =
                    ((CalculateData.periodCallingPlan * 0.75) * 20).toInt().toString()

                fullCallingplan()
            }
        }
    }

    fun fullCallingplan() {
        result_period_callingplan_all_title.text = getString(R.string.Twentyfour)
        when (CalculateData.period) {
            0 -> {
                result_period_callingplan_all_price.text =
                    ((CalculateData.callingPlan * 6) + (CalculateData.periodCallingPlan * 18)).toString()
            }

            1 -> {
                result_period_callingplan_all_price.text =
                    (((CalculateData.callingPlan * 0.75) * 4) + ((CalculateData.periodCallingPlan * 0.75) * 20)).toInt()
                        .toString()
            }

            2 -> {
                result_period_callingplan.visibility = View.GONE
                result_period_callingplan_remainder.visibility = View.GONE

                if (CalculateData.supportfund == 0) {
                    result_period_callingplan_all_price.text =
                        (CalculateData.callingPlan * 24).toString()
                } else if (CalculateData.supportfund == 1) {
                    result_period_callingplan_all_price.text =
                        ((CalculateData.callingPlan * 0.75) * 24).toInt().toString()
                }
            }
        }
    }


    //디바이스 할인 금액 : 대리점 지원금 + 통신사 TextView
    //통신사 할인 금액을 가져올 수 없어 대리점 지원금만(-)
    fun setStorefund() {
        if (CalculateData.storefund != 0) {
            rsesult_device_discount.visibility = View.VISIBLE
            result_device_dicount_title.text = getString(R.string.StoreFund)
            result_device_dicount_content.text = (CalculateData.storefund * -1).toString()
        }
    }

    //가족할인 금액 TextView
    fun setFamilyDis() {
        result_family_title.text = getString(R.string.result_family)
        when (CalculateData.family) {
            0 -> {
                result_family.visibility = View.VISIBLE
                result_family_content.text =
                    ((result_period_callingplan_all_price.text.toString().toInt() * 0.1) * -1).toInt()
                        .toString()
            }

            1 -> {
                result_family.visibility = View.VISIBLE
                result_family_content.text =
                    ((result_period_callingplan_all_price.text.toString().toInt() * 0.3) * -1).toInt()
                        .toString()
            }

            2 -> {
                result_family.visibility = View.VISIBLE
                result_family_content.text =
                    ((result_period_callingplan_all_price.text.toString().toInt() * 0.5) * -1).toInt()
                        .toString()
            }
        }
    }

    //복지할인 금액 TextView
    fun setWelfare() {
        if (CalculateData.welfare) {
            result_welfare.visibility = View.VISIBLE
            result_welfare_title.text = getString(R.string.result_welfare)
            result_welfare_content.text =
                ((result_period_callingplan_all_price.text.toString().toInt() * 0.35) * -1).toInt()
                    .toString()
        }
    }

    //최종 디바이스 금액 TextView
    fun setFinalDevice() {
        result_final_devie_title.text = getString(R.string.result_fin_device)

        if (CalculateData.storefund != 0){
            result_final_devie_content.text = (matchPrice() + result_device_dicount_content.text.toString().toInt()).toString()
        } else {
            result_final_devie_content.text = matchPrice().toString()
        }
    }

    //2년 사용시 금액 TextView
    fun setFinalTwoYear(){
        result_final_twoyear_title.text = getString(R.string.result_fin_two_year)

        if (CalculateData.family != -1 && CalculateData.welfare == true){
            result_final_twoyear_content.text = (result_period_callingplan_all_price.text.toString().toInt() + result_family_content.text.toString().toInt() + result_welfare_content.text.toString().toInt()).toString()
        }

        if(CalculateData.family == -1 && CalculateData.welfare == true){
            result_final_twoyear_content.text = (result_period_callingplan_all_price.text.toString().toInt() + result_welfare_content.text.toString().toInt()).toString()
        }

        if(CalculateData.family != -1 && CalculateData.welfare == false){
            result_final_twoyear_content.text = (result_period_callingplan_all_price.text.toString().toInt() + result_family_content.text.toString().toInt()).toString()
        }

        if(CalculateData.family == -1 && CalculateData.welfare == false){
            result_final_twoyear_content.text = result_period_callingplan_all_price.text.toString()
        }
    }

    //디바이스에 따른 출고가
    fun matchPrice(): Int {
        var codeName: String = ""
        var price: Int = 0

        for (i in 0 until GlobalVar.samsungDeviceNameArray.size) {
            if (CalculateData.deviceModel.equals(GlobalVar.samsungDeviceNameArray[i])) {
                codeName = GlobalVar.samsungDeviceCodeNameArray[i].trim()
                break
            }
        }

        for (i in 0 until GlobalVar.lgDeviceNameArray.size) {
            if (CalculateData.deviceModel.equals(GlobalVar.lgDeviceNameArray[i])) {
                codeName = GlobalVar.lgDeviceCodeNameArray[i].trim()
                break
            }
        }

        for (i in 0 until GlobalVar.appleDeviceNameArray.size) {
            if (CalculateData.deviceModel.equals(GlobalVar.appleDeviceNameArray[i])) {
                codeName = GlobalVar.appleDeviceCodeNameArray[i].trim()
                Log.d("appleapple", "appleapple")
                break
            }
        }
        codeName = codeName.trim()
        Log.d("matchPrice()_codeName", codeName)

        for (i in 0 until GlobalVar.allDevices.size) {
            Log.d("matchPrice()_com", GlobalVar.allDevices[i])
            if (codeName.equals(GlobalVar.allDevices[i].trim())) {
                price = Integer.parseInt(GlobalVar.devicesPrice[i])
            }
        }
        Log.d("matchPrice()_price", price.toString())

        return price
    }
}