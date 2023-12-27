package com.plcoding.testingcourse.part8.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.Clock

class VideoCallExpirationFlow(
    private val calls: List<ScheduledVideoCall>, // our own custom logic
    private val clock: Clock = Clock.systemDefaultZone() // our own custom logic
): Flow<List<ScheduledVideoCall>> {


    // This flows and "activates" the flow every second
    private fun tickerFlow() = flow {
        while (true) {
            emit(Unit)
            delay(1000L)
        }
    }

    // we override the collect function, the collector will contain the emissions that are
    // processed in here
    override suspend fun collect(collector: FlowCollector<List<ScheduledVideoCall>>) {
        // each emission of the tickerFlow will be processed
        tickerFlow()
            .flowOn(Dispatchers.Main) // the dispatcher we want to use
            .map {
                calls.filter { it.isExpired(clock) } // only contain the expired ones
            }
            .distinctUntilChanged() // ensures that only unique, non-repeating values are considered
            .collect(collector) // stores the emission that made it until here to the collector
    }
}