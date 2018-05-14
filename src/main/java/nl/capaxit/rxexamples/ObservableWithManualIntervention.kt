package nl.capaxit.rxexamples

import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import rx.schedulers.TestScheduler
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

val ruleEvaluatorSubscription: CompositeSubscription = CompositeSubscription()
val confirmed: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
val needConfirmation:PublishSubject<Boolean> = PublishSubject.create<Boolean>()
var eventNeedingConfirmation: Event? = null
var numberOfConfirms: Int = 0

fun main(args: Array<String>) {
    needConfirmation
            .subscribe {
//                todo show dialog in real world
                println("Need confirmation")
                numberOfConfirms++
                confirmed.onNext(numberOfConfirms == 2)
            }

    val testScheduler = Schedulers.test()
    confirmed
            .doOnNext { confirmed ->
                println("User has confirmed $confirmed")
                if (confirmed) {
                    println("Compute new journey")
                }
                createRuleEvaluator(testScheduler)
            }
            .subscribe()

    ruleEvaluatorSubscription.add(createRuleEvaluator(testScheduler))
    testScheduler.advanceTimeBy(10, TimeUnit.SECONDS)
}

private fun createRuleEvaluator(testScheduler: TestScheduler?): Subscription? {
    return Observable.interval(1, TimeUnit.SECONDS, testScheduler)
            .map { Event(it.toString(), it == 3L) }
            .doOnNext { println("received: $it") }
            .doOnNext({
                if (it.manualConfirmationNeeded) {
                    ruleEvaluatorSubscription.clear()
                    eventNeedingConfirmation = it
                    needConfirmation.onNext(true)
                }
            })
            .filter {!it.manualConfirmationNeeded}
            .subscribe { println("processed $it") }
}

data class Event(val value: String, val manualConfirmationNeeded: Boolean)
