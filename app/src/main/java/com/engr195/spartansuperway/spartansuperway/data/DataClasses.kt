package com.engr195.spartansuperway.spartansuperway.data

data class FirebaseAccount(val firstName: String, val lastName: String, val email: String, val password: String)

data class Ticket(val fromLocation: String, val toLocation: String, val date: String, val time: String)