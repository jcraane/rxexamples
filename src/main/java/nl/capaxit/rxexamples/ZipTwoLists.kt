package nl.capaxit.rxexamples

import rx.Observable

fun main(args: Array<String>) {
    val numbers1 = Observable.just(listOf(1, 2, 3))
//    val numbers2 = Observable.just(listOf(4, 5, 6))
    val numbers2 = Observable.empty<List<Int>>()
    numbers1
            .zipWith(numbers2, { a, b ->
                val l = mutableListOf<Int>()
                l.addAll(a)
                l.addAll(b)
                return@zipWith l
            })
            .subscribe({ println(it) })

}
