package com.msa.petsearch.shared.coreutil.sharedviewmodel

import com.msa.petsearch.shared.coreutil.commonflow.CommonFlow
import com.msa.petsearch.shared.coreutil.commonflow.asCommonFlow
import com.msa.petsearch.shared.coreutil.resource.MessageDeque
import com.msa.petsearch.shared.coreutil.resource.ResourceMessage
import com.msa.petsearch.shared.coreutil.sharedviewmodel.navigation.NavigationState
import com.msa.petsearch.shared.coreutil.sharedviewmodel.navigation.RouteNavigator
import com.msa.petsearch.shared.coreutil.sharedviewmodel.store.*
import com.msa.petsearch.shared.coreutil.sharedviewmodel.store.ActionDispatcher
import com.msa.petsearch.shared.coreutil.sharedviewmodel.store.NanoRedux.*
import com.msa.petsearch.shared.coreutil.sharedviewmodel.util.SuperViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.EmptyCoroutineContext

/**
 *
 * Heavily modified version of the Arch Library written by fededri
 * https://github.com/fededri/Arch
 *
 */
abstract class BaseViewModel<A : Action, S : State, SE : SideEffect, RS : RenderState, N : Navigation, E : Event, ER : Error>
constructor(
    private val updater: Updater<A, S, SE, E, N, ER>? = null,
    private val initialState: S,
    private val processor: Processor<SE, A>? = null,
    private val stateMapper: StateMapper<S, RS>? = null,
    private val argsMapper: ArgsMapper<S>? = null,
    private val coroutineExceptionHandler: CoroutineExceptionHandler? = null,
    private val routeNavigator: RouteNavigator
) : SuperViewModel(), ActionDispatcher<A>, KoinComponent, RouteNavigator by routeNavigator {

    private val state = MutableStateFlow(initialState)

    private val events = MutableSharedFlow<E>()

    private val renderState by lazy {
        MutableStateFlow(stateMapper?.mapToRenderState(state.value))
    }

    private val messageDeque: MessageDeque by inject()

    fun updateArgsInState(args: HashMap<String, String>) {
        argsMapper?.let { mapper ->
            state.value = mapper.mapArgsToState(currentState = state.value, args = args)
        }
        stateMapper?.let { mapper ->
            renderState.value = mapper.mapToRenderState(state = state.value)
        }
    }

    fun observeMessageDeque(): CommonFlow<ResourceMessage?> {
        return messageDeque.readOnlyStateFlow().asCommonFlow()
    }

    fun observeNavigationState(): CommonFlow<NavigationState> {
        return routeNavigator.navigationState.asCommonFlow()
    }

    fun observeRenderState(): CommonFlow<RS?> {
        return renderState.asStateFlow().asCommonFlow()
    }

    fun observeState(): CommonFlow<S> {
        return state.asStateFlow().asCommonFlow()
    }

    fun observeEvents(): CommonFlow<E> {
        return events.asSharedFlow().asCommonFlow()
    }

    override suspend fun action(action: A) {
        updater?.onNewAction(action, state.value)?.let { next ->

            if (next.events.isNotEmpty()) {
                next.events.forEach {
                    events.emit(it)
                }
            }

            if (next.sideEffects.isNotEmpty()) {
                dispatchSideEffects(next.sideEffects)
            }

            if (next.navigation.isNotEmpty()) {
                next.navigation.firstOrNull()?.let {
                    routeNavigator.onNavStart(it.state)
                }
            }

            if (next.errors.isNotEmpty()) {
                next.errors.forEach { error ->
                    error.message
                        ?.let {
                            messageDeque.enqueue(
                                it.copy(
                                    text = it.text,
                                    messageType = it.messageType,
                                    dequeueCallback = { messageDeque.dequeue() }
                                )
                            )
                        }
                }
            }

            state.value = next.state

            stateMapper?.mapToRenderState(next.state)?.let { rs ->
                renderState.value = rs
            }
        }
    }

    private fun dispatchSideEffects(sideEffects: Set<SE>) {
        sideEffects.forEach { effect ->
            val coroutineContext = coroutineExceptionHandler ?: EmptyCoroutineContext
            effect.coroutineScope.launch(coroutineContext) {
                withContext(effect.dispatcher) {
                    processor?.dispatchSideEffect(effect)?.let {
                        withContext(effect.dispatcher) {
                            action(it)
                        }
                    }
                }
            }
        }
    }

    public override fun onCleared() {
        state.value = initialState
        super.onCleared()
    }
}
