package de.fhe.ai.colivingpilot.storage

import java.util.UUID

class StaticUUID {

    private val staticID = UUID.randomUUID().toString()

    public fun getID(): String{
        return staticID
    }
}