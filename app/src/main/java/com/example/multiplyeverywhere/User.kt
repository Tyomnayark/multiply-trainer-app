package com.example.multiplyeverywhere

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


class User(val name: String, val points: Int, var profileImage :String, val level: Int) : Serializable {
}
