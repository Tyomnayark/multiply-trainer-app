package com.example.multiplyeverywhere

import java.io.Serializable


class User(val name: String, val points: Int, var profileImage :String, val level: Int, var complexity: Int) : Serializable {
}
