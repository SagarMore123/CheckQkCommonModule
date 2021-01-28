package com.astrika.checqk.discount.model

import com.astrika.checqk.discount.utils.Constants


class LoginDTO {

    var deviceKey: String? = "123"
    var isForgotPassword: Boolean? = false
    var password: String? = ""
    var role: String? = Constants.ROLE
    var sourceId: String? = Constants.SOURCE
    var username: String? = ""

}

class RefreshTokenDTO {
    var username: String? = ""
    var refreshToken: String? = ""
}

class AccessTokenDTO {
    var access_token: String? = ""
    var refresh_token: String? = ""
}