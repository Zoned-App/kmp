package com.github.zoned.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform