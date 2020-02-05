package com.puddlealley.splash.rx

import com.puddlealley.splash.Dispatcher
import com.puddlealley.splash.Payload
import io.kotlintest.specs.StringSpec
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import io.reactivex.Observable

class EpicsKtTest : DescribeSpec() {

    init {

        describe("Epics") {

            it("adds epic middleware") {
                data class Number(val number: Int = 0) : Payload

                val epic: Epic = epicOfType<Number> {
                    it.filter { it.number == -1 }
                        .flatMap { Observable.just(Number(0), Number(1), Number(2), Number(3)) }
                }
                val epic1: Epic = epicOfType<Number> {
                    it.filter { it.number == 3 }
                        .flatMap { Observable.just(Number(4), Number(5), Number(6), Number(7)) }
                }

                val epicMiddleware = createEpicMiddleware(combineEpics(epic, epic1))
                val dispatcher = RxDispatcher(epicMiddleware)

                val test = dispatcher.updates.test()

                epicMiddleware.run()
                dispatcher.dispatch(Number(-1))
                test.assertValueCount(8)
            }
        }
    }

}