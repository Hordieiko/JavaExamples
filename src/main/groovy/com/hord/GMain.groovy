package com.hord

import com.google.gson.Gson
import com.google.gson.JsonObject

class GMain {
    static void main(String[] args) {
        def json = "{\"error\":\"consent_required\"}"
        def jsonObject = new Gson().fromJson(json, JsonObject.class)
        println jsonObject
    }
}
