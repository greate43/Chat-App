package com.sk.greate43.smack.model

class Channel(val name: String, val Description: String, val id: String) {
    override fun toString(): String {
        return "#$name"
    }
}