package com.kotlinsample.app.models

/**
 * Created on 4/8/2019.
 */
class NewMessage (val id: String, val fromId: String, val toId: String, val message: String, val timestamp : Long,var user: User){
    constructor(): this("","","","",-1, User())
}