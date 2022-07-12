package com.perpetio.well_be.network

class FormattedNetworkClientException(text: String) :
    IllegalStateException(text) {

    var formattedErrorMessage: String

    init {
        formattedErrorMessage = try {
            val split = text.split("Text: \"")
            split[1].replace("\"", "")
        } catch (ex: Exception) {
            "Unknown error"
        }
    }
}