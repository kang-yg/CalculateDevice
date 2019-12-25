package com.comparesmartphoneprice

class CalculateData {
    companion object {
        var telecom: Int = -1 // 0 : SKT | 1 : LG U+ | 2 : KT
        var deviceBrand: Int = -1 // 0 : Samsung | 1 : LG | 2 : Apple
        var deviceModel: String = ""
        var callingPlan : Int = 0
        var periodCallingPlan : Int = 0
        var supportfund: Int = -1 // 0 : 공시지원금 | 1 : 선택약정
        var period: Int = -1 // 0 : 6개월 | 1 : 4개월 | 2 : 24개월
        var storefund: Int = 0
        var family: Int = -1 // 0 : 10% | 1: 30% | 2 : 50%
        var welfare: Boolean = false // false : 복지대상X
    }
}