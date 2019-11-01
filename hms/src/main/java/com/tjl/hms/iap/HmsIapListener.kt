package com.tjl.hms.iap

interface HmsIapListener {
    fun onUpdateListDataByType(data:ArrayList<Product>,type:Int)
    fun onUpdateItemData(data:Product)
    fun onHmsIapSupported(bSupported:Boolean)
}