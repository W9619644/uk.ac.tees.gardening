package com.example.greenthumb.models
data class RegisterModel(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var photo: String? = null,
    var phone: String? = null
) {
    override fun toString(): String {
        return "RegisterModel(email=$email, username=$username, password=$password, photo=$photo, phone=$phone)"
    }
}
