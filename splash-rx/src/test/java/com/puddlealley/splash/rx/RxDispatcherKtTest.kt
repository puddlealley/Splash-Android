package com.puddlealley.splash.rx

import com.puddlealley.splash.Dispatcher
import com.puddlealley.splash.Payload
import io.kotlintest.TestCase
import io.kotlintest.specs.DescribeSpec

class RxDispatcherKtTest : DescribeSpec() {

    private var dispatcher = RxDispatcher()

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        dispatcher = RxDispatcher()
    }

    init {
        describe("rx Dispatcher"){

            it("registers listened on subscription") {
                val payload = object: Payload {}
                val updates = dispatcher.updates
                val test = updates.test()
                val test1 = updates.test()

                dispatcher.dispatch(payload)

                test.assertValueCount(1)
                test.assertNoErrors()
                test.assertValueAt(0, payload)
            }
        }
    }

}