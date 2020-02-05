package com.puddlealley.splash.rx

import com.puddlealley.splash.*
import io.kotlintest.specs.StringSpec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.spyk

data class TestState(val changed : Boolean = false): State
sealed class TestPayload : Payload {
    object Changed : TestPayload()
    object Ignore : TestPayload()
}

private class TestFluxStore(dispatcher: Dispatcher) : RxFluxReduceStore<TestState>(TestState(), dispatcher) {

    override fun reduce(state: TestState, action: Payload): TestState {
        return when(action){
            is TestPayload -> {
                when(action){
                    TestPayload.Changed -> state.copy(changed = !state.changed)
                    TestPayload.Ignore -> state
                }
            }
            else -> state
        }
    }

    val callbackCount get() =  callbacks.size

}
class RxFluxReduceStoreTest : DescribeSpec() {

    private var dispatcher = spyk<Dispatcher>()
    private var store = TestFluxStore(dispatcher)

    override fun beforeTest(testCase: TestCase) {
        dispatcher = spyk<Dispatcher>()
        store = TestFluxStore(dispatcher)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
    }

    init {
        describe("rx store"){

            /*it("changed registers only one listener on subscription") {
                val updates = store.onChanged
                val test = updates.test()
                val test1 = updates.test()

                dispatcher.dispatch(TestPayload.Changed)
                dispatcher.dispatch(TestPayload.Ignore)

                store.callbackCount.shouldBe(1)
                test.assertValueCount(1)
                test.assertNoErrors()
                test.assertValueAt(0, Unit)
            }*/

            it("updates registers only one listener on subscription") {
                val updates = store.updates
                val test = updates.test()
                val test1 = updates.test()

                dispatcher.dispatch(TestPayload.Changed)

                store.callbackCount.shouldBe(1)
                test.assertValueCount(2)
                test.assertNoErrors()
                test.assertValueAt(0){ !it.changed }
                test.assertValueAt(1){ it.changed }
            }

            it("always starts with current state") {
                val updates = store.updates
                val test = updates.test()
                dispatcher.dispatch(TestPayload.Changed)
                test.cancel()

                val test1 = updates.test()

                store.callbackCount.shouldBe(1)
                test1.assertValueCount(1)
                test1.assertNoErrors()
                test1.assertValueAt(0){ it.changed }
            }
        }
    }

}