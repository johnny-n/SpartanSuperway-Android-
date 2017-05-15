package com.engr195.spartansuperway.spartansuperway.data

// ETA status codes
val etaStatusPickupUser = 100       // Pod is otw to pickup user at their location
val etaStatusWaiting = 200      // Pod is waiting for user to get inside
val etaStatusDestination = 300  // Pod is otw to user's final destination
val etaStatusArrival = 400      // Pod has arrived to the user's final destination
val etaStatusNoTicket = 900     // User has no active ticket
