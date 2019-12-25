package com.comparesmartphoneprice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.calculate_frag.*
import kotlinx.android.synthetic.main.calculate_frag.view.*

class CalculateFragment : Fragment(), AdapterView.OnItemSelectedListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.calculate_frag, null)

        //-----------------------------------------------------------------------------------------------------
        //Device Group
        var deviceNameArray: ArrayList<String> = arrayListOf()

        view.device_group.setOnCheckedChangeListener { _, i ->
            when (i) {
                device_samsung.id -> {
                    deviceNameArray = GlobalVar.samsungDeviceNameArray
                    setDevieArray(view, deviceNameArray)

                    //setDevieArray(view, deviceNameArray)
                    CalculateData.deviceBrand = 0
                }
                device_lg.id -> {
                    deviceNameArray = GlobalVar.lgDeviceNameArray
                    setDevieArray(view, deviceNameArray)

                    CalculateData.deviceBrand = 1
                }
                device_apple.id -> {
                    deviceNameArray = GlobalVar.appleDeviceNameArray
                    setDevieArray(view, deviceNameArray)

                    CalculateData.deviceBrand = 2
                }
            }
        }
        //-----------------------------------------------------------------------------------------------------
        //Select Device item at Spinner
        view.device_spinner.onItemSelectedListener = this
        //-----------------------------------------------------------------------------------------------------
        //Select Telecom
        view.myTel_group.setOnCheckedChangeListener { _, i ->
            when (i) {
                myTel_skt.id -> {
                    CalculateData.telecom = 0
                }

                myTel_lg.id -> {
                    CalculateData.telecom = 1
                }

                myTel_kt.id -> {
                    CalculateData.telecom = 2
                }
            }
        }

        //-----------------------------------------------------------------------------------------------------
        //Support Fund

        view.publicsupportfund.setOnClickListener(View.OnClickListener {
            period_radiogroup.visibility = View.VISIBLE
            CalculateData.supportfund = 0 //공시지원
            shortperiod.text = view.resources.getString(R.string.SixMonth)
            period_callingplan_textview.text = view.resources.getString(R.string.PeriodSixMonth)
        })

        view.choicesupportfund.setOnClickListener(View.OnClickListener {
            period_radiogroup.visibility = View.VISIBLE
            CalculateData.supportfund = 1 //선택약정
            shortperiod.text = view.resources.getString(R.string.FourMonth)
            period_callingplan_textview.text = view.resources.getString(R.string.PeriodFourMonth)
        })

        view.shortperiod.setOnClickListener(View.OnClickListener {
            period_callingplan.visibility = View.VISIBLE
        })

        view.longperiod.setOnClickListener(View.OnClickListener {
            CalculateData.period = 2
            period_callingplan.visibility = View.GONE
        })

        //-----------------------------------------------------------------------------------------------------
        //Family Checkbox
        view.family_checkbox_1.setOnCheckedChangeListener { compoundButton, _ ->
            Log.d("compoundButton", compoundButton.text.toString())
            checkOnlyFamilyCheckbox(view, compoundButton)
        }

        view.family_checkbox_2.setOnCheckedChangeListener { compoundButton, _ ->
            Log.d("compoundButton", compoundButton.text.toString())
            checkOnlyFamilyCheckbox(view, compoundButton)
        }

        view.family_checkbox_3.setOnCheckedChangeListener { compoundButton, _ ->
            Log.d("compoundButton", compoundButton.text.toString())
            checkOnlyFamilyCheckbox(view, compoundButton)
        }
        //-----------------------------------------------------------------------------------------------------
        //Calculate Button
        view.calculate_submit.setOnClickListener(View.OnClickListener {

            var finalResult: Boolean = false

            getPeriodState(view)
            getFamily(view)
            getWelfare(view)
            finalResult = checkFinal(view)
            Log.d("finalResult", finalResult.toString())

            //TODO 마지막에 삭제
//            val intent: Intent = Intent(context, CalculateResult::class.java)
//            startActivity(intent)

            //TODO 마지막에 주석처리 해제
            if (finalResult) {
                val intent: Intent = Intent(context, CalculateResult::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.CalculateToast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return view
    }

    //Set device spinner
    fun setDevieArray(view: View, array: ArrayList<String>) {
        val deviceSpinnerAdapter =
            ArrayAdapter(
                view.context,
                android.R.layout.simple_spinner_dropdown_item,
                array
            )
        view.device_spinner.adapter = deviceSpinnerAdapter
    }

    fun getPeriodState(v: View) {
        if (v.shortperiod.isChecked) {
            when (CalculateData.supportfund) {
                0 -> {
                    CalculateData.period = 0
                }

                1 -> {
                    CalculateData.period = 1
                }
            }
        }
    }

    fun getFamily(v: View) {
        if (v.family_checkbox_1.isChecked) {
            CalculateData.family = 0
        } else if (v.family_checkbox_2.isChecked) {
            CalculateData.family = 1
        } else if (v.family_checkbox_3.isChecked) {
            CalculateData.family = 2
        }
    }

    fun getWelfare(v: View) {
        if (v.welfare_checkbox.isChecked ?: true){
            CalculateData.welfare = true
        }
    }

    //Family Checkbox choice only one
    fun checkOnlyFamilyCheckbox(view: View, compoundButton: CompoundButton) {
        when (compoundButton) {
            view.family_checkbox_1 -> {
                view.family_checkbox_2.isChecked = false
                view.family_checkbox_3.isChecked = false
            }

            view.family_checkbox_2 -> {
                view.family_checkbox_1.isChecked = false
                view.family_checkbox_3.isChecked = false
            }

            view.family_checkbox_3 -> {
                view.family_checkbox_1.isChecked = false
                view.family_checkbox_2.isChecked = false
            }
        }
    }

    //Check for Intent 결과물 확인을 위한 액티비티로 넘어가기 전 필수값 확인
    fun checkFinal(v: View): Boolean {
        Log.d("call_checkFinal", "checkFinal()")
        var finalResult: Boolean = false

        val checkTelecomResult = checkTelecom()
        Log.d("checkFinal_Telecom", checkTelecomResult.toString())
        Log.d("checkFinal_Telecom", CalculateData.telecom.toString())

        val checkDeviceBrandResult: Boolean = checkDeviceBrand()
        Log.d("checkFinal_DeviceBrand", checkDeviceBrandResult.toString())
        Log.d("checkFinal_DeviceBrand", CalculateData.deviceBrand.toString())

        val checkDeviceModelResult: Boolean = checkDeviceModel()
        Log.d("checkFinal_DeviceModel", checkDeviceModelResult.toString())
        Log.d("checkFinal_DeviceModel", CalculateData.deviceModel)

        val checkCallingPlanResult: Boolean = checkCallingPlan(v)
        Log.d("checkFinal_CallingPlan", checkCallingPlanResult.toString())
        Log.d("checkFinal_CallingPlan", CalculateData.callingPlan.toString())

        val checkPeriodCallingPlanResult: Boolean = checkPeriodCallingPlan(v)
        Log.d("checkFinal_PCallingPlan", checkPeriodCallingPlanResult.toString())
        Log.d("checkFinal_PCallingPlan", CalculateData.periodCallingPlan.toString())

        val checkSupportfundResult: Boolean = checkSupportfund()
        Log.d("checkFinal_Supportfund", checkSupportfundResult.toString())
        Log.d("checkFinal_Supportfund", CalculateData.supportfund.toString())

        val checkPeriodResult: Boolean = checkPeriod()
        Log.d("checkFinal_Period", checkPeriodResult.toString())
        Log.d("checkFinal_Period", CalculateData.period.toString())

        val checkStorefundResult: Boolean = checkStorefund(v)
        Log.d("checkFinal_Storefund", checkStorefundResult.toString())
        Log.d("checkFinal_Storefund", CalculateData.storefund.toString())

        val resultArray: MutableList<Boolean> = mutableListOf()
        resultArray.add(checkTelecomResult)
        resultArray.add(checkDeviceBrandResult)
        resultArray.add(checkDeviceModelResult)
        resultArray.add(checkCallingPlanResult)
        resultArray.add(checkPeriodCallingPlanResult)
        resultArray.add(checkSupportfundResult)
        resultArray.add(checkPeriodResult)
        resultArray.add(checkStorefundResult)

        //최종으로 반환할 결과 : 하나라도 false면 결과는 false
        for (i in resultArray) {
            if (i == false) {
                finalResult = false
                break
            } else {
                finalResult = true
            }
        }

        Log.d("resultArray", resultArray.size.toString())

        return finalResult
    }

    fun checkTelecom(): Boolean {
        var checkTelecomResult: Boolean = false

        checkTelecomResult = CalculateData.telecom != -1

        return checkTelecomResult
    }

    fun checkDeviceBrand(): Boolean {
        var checkDeviceBrandResult: Boolean = false

        checkDeviceBrandResult = CalculateData.deviceBrand != -1

        return checkDeviceBrandResult
    }

    fun checkDeviceModel(): Boolean {
        var checkDeviceModelResult: Boolean = false

        checkDeviceModelResult = !CalculateData.deviceModel.equals("")

        return checkDeviceModelResult
    }

    fun checkCallingPlan(v: View): Boolean {
        var checkCallingPlanResult: Boolean = false

        if (!v.callingplan_editText.text.toString().equals("")) {
            CalculateData.callingPlan = Integer.parseInt(v.callingplan_editText.text.toString())
            checkCallingPlanResult = true
            Log.d("checkCallingPlan", CalculateData.callingPlan.toString())
        } else {
            Log.d("checkCallingPlan", "wrong data")
        }

        return checkCallingPlanResult
    }

    fun checkPeriodCallingPlan(v: View): Boolean {
        var checkPeriodCallingPlanResult: Boolean = false

        if (!v.period_callingplan_editText.text.toString().equals("")) {
            CalculateData.periodCallingPlan =
                Integer.parseInt(v.period_callingplan_editText.text.toString())
            checkPeriodCallingPlanResult = true
            Log.d("checkPeriodCallingPlan", CalculateData.periodCallingPlan.toString())
        } else if (CalculateData.period == 2) {
            CalculateData.periodCallingPlan = 0
            checkPeriodCallingPlanResult = true
            Log.d("checkPeriodCallingPlan", checkPeriodCallingPlanResult.toString())
        } else {
            Log.d("checkPeriodCallingPlan", "wrong data")
        }

        return checkPeriodCallingPlanResult
    }

    fun checkSupportfund(): Boolean {
        var checkSupportfundResult: Boolean = false

        checkSupportfundResult = CalculateData.supportfund != -1

        return checkSupportfundResult
    }

    fun checkPeriod(): Boolean {
        var checkPeriodResult: Boolean = false

        checkPeriodResult = CalculateData.period != -1

        return checkPeriodResult
    }

    fun checkStorefund(v: View): Boolean {
        var checkStorefundResult: Boolean = false

        if (!v.storefund_edit.text.toString().equals("")) {
            CalculateData.storefund = Integer.parseInt(v.storefund_edit.text.toString())
            checkStorefundResult = true
            Log.d("checkStorefund", CalculateData.storefund.toString())
        } else {
            CalculateData.storefund = 0
            checkStorefundResult = true
            Log.d("checkStorefund", "none storefund")
        }

        return checkStorefundResult
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    //스피너 선택 리스너
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            device_spinner -> {
                Log.d("SelectSpinnerItem", "DeviceSpinner")
                Log.d("SelectSpinnerItem", p0!!.selectedItem.toString())
                CalculateData.deviceModel = p0!!.selectedItem.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CalculateData.family = -1
        CalculateData.welfare = false
    }
}