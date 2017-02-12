package com.engr195.spartansuperway.spartansuperway.utils

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

// Simple class extending ChildEventListener to reduce boilerplate in Activity/Fragment code
open class SimpleChildEventListener : ChildEventListener {
    override fun onChildMoved(snapshot: DataSnapshot?, prevChild: String?) {
    }

    override fun onChildChanged(snapshot: DataSnapshot?, prevChild: String?) {
    }

    override fun onChildAdded(snapshot: DataSnapshot?, prevChild: String?) {
    }

    override fun onChildRemoved(snapshot: DataSnapshot?) {
    }

    override fun onCancelled(message: DatabaseError?) {
    }
}