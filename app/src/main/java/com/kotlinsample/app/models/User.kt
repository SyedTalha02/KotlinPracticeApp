package com.kotlinsample.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created on 4/5/2019.
 */
@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String, val email: String) : Parcelable {
    constructor() : this("","","", "")

}