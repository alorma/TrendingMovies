package com.alorma.presentation.common

open class Action
open class Route
open class State
open class Event

abstract class EventHandler<E : Event>(content: E) {

    protected var eventContent: E? = content

    var hasBeenHandled = false
        private set // Allow external read but not write

    abstract fun peekContent(): E?
}

class NonPersistentEventHandler<E: Event>(t: E) : EventHandler<E>(t) {
    override fun peekContent(): E? {
        val event = eventContent
        eventContent = null
        return event
    }
}

class PersistentEventHandler<E: Event>(t: E) : EventHandler<E>(t) {
    override fun peekContent(): E? = eventContent
}
