package com.hashim.workmanager

/*
*1->Worker:  is where, the code for the actual work to be performed in the background is
*executed. doWork() is overridden and inside code is executed in the backgroud.
*
*2->WorkRequest: This represents a request to do some work. A Worker is passed as part of creating
*WorkRequest. When making the WorkRequest,l Constraints on when the Worker should run be put
*in workrequest.
*
*3->WorkManager: This class actually schedules the WorkRequest and makes it run. It
* schedules WorkRequests in a way that it spreads out the load on system resources,
* while honoring the constraints specified.
*/



/*
*
    OneTimeWorkRequest: A WorkRequest that will only execute once.
    PeriodicWorkRequest: A WorkRequest that will repeat on a cycle.

*
* */