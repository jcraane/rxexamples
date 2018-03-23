package nl.capaxit.rxexamples

import io.reactivex.Observable

fun getObservable(number: Int) = if (number < 4) Observable.empty<Int>() else Observable.just(number)
fun getObservableTwo(number: Int) = if (number == 2) Observable.empty<Int>() else Observable.just(number)

fun main(args: Array<String>) {
    getObservable(1)
            .switchIfEmpty(getObservable(2)
                    .switchIfEmpty(getObservable(3)
                            .switchIfEmpty(getObservable(4))))
            .subscribe({ println(it)})

    getObservable(1)
            .switchIfEmpty(getObservable(2))
            .switchIfEmpty(getObservable(3))
            .switchIfEmpty(getObservable(4))
            .subscribe({ println(it)})

    Observable.concat(listOf(getObservable(1), getObservable(2), getObservable(3), getObservable(4)))
            .subscribe({ println(it)})

    println("empty")
    getObservableTwo(1)
            .switchIfEmpty(getObservableTwo(2))
            .switchIfEmpty(getObservableTwo(3))
            .switchIfEmpty(getObservableTwo(4))
            .subscribe({ println(it)})

    println("concat")
    Observable.concat(listOf(getObservableTwo(1), getObservableTwo(2), getObservableTwo(3), getObservableTwo(4)))
            .subscribe({ println(it)})
}