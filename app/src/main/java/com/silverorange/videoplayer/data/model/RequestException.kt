package com.silverorange.videoplayer.data.model

class RequestException(val code: Int, message: String) : Exception(message)