package nl.capaxit.rxexamples

import io.reactivex.Observable

fun getObservable(number: Int) = if (number < 4) Observable.empty<Int>() else Observable.just(number)

fun main(args: Array<String>) {
//    Below code all yield the same result but differ in readability.
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

    val numberObservables: List<Observable<Int>> = listOf<Observable<Int>>(getObservable(1), getObservable(2), getObservable(3), getObservable(4))
    Observable.concat(numberObservables)
            .subscribe({ println(it)})
}