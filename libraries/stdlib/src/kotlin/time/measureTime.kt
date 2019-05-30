/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package kotlin.time

import kotlin.contracts.*

/**
 * Executes the given [action] and returns the duration of elapsed time interval.
 *
 * The elapsed time is measured with [MonoClock].
 */
public inline fun measureTime(action: () -> Unit): Duration {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return MonoClock.measureTime(action)
}


/**
 * Executes the given [action] and returns the duration of elapsed time interval.
 *
 * The elapsed time is measured with the specified `this` [Clock] instance.
 */
public inline fun Clock.measureTime(action: () -> Unit): Duration {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }

    val mark = mark()
    action()
    return mark.elapsed()
}


/**
 * Data class representing a result of executing an action, along with the duration of elapsed time interval.
 *
 * @property value the result of the action.
 * @property duration the time elapsed to execute the action.
 */
public data class TimedValue<T>(val value: T, val duration: Duration)

/**
 * Executes the given [action] and returns an instance of [TimedValue] class, containing both
 * the result of the [action] execution and the duration of elapsed time interval.
 *
 * The elapsed time is measured with [MonoClock].
 */
public inline fun <T> measureTimedValue(action: () -> T): TimedValue<T> {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }

    return MonoClock.measureTimedValue(action)
}

/**
 * Executes the given [action] and returns an instance of [TimedValue] class, containing both
 * the result of the [action] execution and the duration of elapsed time interval.
 *
 * The elapsed time is measured with the specified `this` [Clock] instance.
 */
public inline fun <T> Clock.measureTimedValue(action: () -> T): TimedValue<T> {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }

    val mark = mark()
    val result = action()
    return TimedValue(result, mark.elapsed())
}
