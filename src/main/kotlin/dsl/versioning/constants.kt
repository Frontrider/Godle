package io.github.frontrider.godle.dsl.versioning

import org.apache.commons.lang3.SystemUtils

enum class SUPPORTED_OS{
    LINUX,MAC,WINDOWS
}

val os = when {
    SystemUtils.IS_OS_MAC -> {
        SUPPORTED_OS.MAC
    }

    SystemUtils.IS_OS_WINDOWS -> {
        SUPPORTED_OS.WINDOWS
    }
    //we default to linux if we had no idea what the system is.
    else -> {
        SUPPORTED_OS.LINUX
    }
}
val bit = when(SystemUtils.OS_ARCH){
    "x86"-> "32"
    else-> "64"
}