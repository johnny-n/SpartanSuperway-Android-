package com.engr195.spartansuperway.spartansuperway.utils

data class FirebaseAccount(val firstName: String, val lastName: String, val email: String, val password: String)

data class Ticket(val fromLocation: String, val toLocation: String, val date: String, val time: String)