package com.puddlealley.flux.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.jakewharton.rxbinding3.view.clicks
import com.puddlealley.flux.R
import com.puddlealley.flux.service.CodeVerificationResult
import com.puddlealley.flux.store.AppStore
import com.puddlealley.flux.store.device.SecretCaveEvents
import com.puddlealley.splash.android.connect
import com.puddlealley.splash.android.events
import io.reactivex.rxkotlin.merge
import io.reactivex.rxkotlin.ofType
import kotlinx.android.synthetic.main.activity_secret_cave.*
import org.koin.android.ext.android.inject
import timber.log.Timber


/**
 * Screen that listens for the secret code.
 *
 * The rules are:
 * - the secret code is 7 letters long and consists of just the letters A and B.
 * - the user enters the code by tabbing the A button and the B button 7 times.
 * - every 7 taps a request is sent to the server to verify the secret code.
 * - The server returns true if the code is correct.
 * - While the server request is in progress the buttons are disabled.
 * - a progress bar is shown while the server request is in flight.
 * - When the code is correct a green tick is shown on the app.
 * - the code is abbabba
 *
 * Your task is to fill in the Business logic that fufils the above behaviour of the app.
 *
 * I have created a skeleton app that used the architecture that carv uses. You should try to fill in the existing classes to
 * create the above behaviour.
 *
 * A login screen exists for you to copy along with tests.
 *
 * You are not required to write tests.
 *
 */
class SecretCaveActivity : AppCompatActivity() {

    private val appStore: AppStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret_cave)

        appStore.events(this) {
            val buttonAClicked = buttonA.clicks().map { SecretCaveEvents.LetteredEntered("A") }.share()
            val buttonBClicked = buttonB.clicks().map { SecretCaveEvents.LetteredEntered("B") }.share()

            val codeEntered =
                listOf(buttonAClicked, buttonBClicked).merge()
                    .map { it.letter }
                    .buffer(7)
                    .map { SecretCaveEvents.CodeEntered(it.joinToString(separator = "")) }

            listOf(
                buttonAClicked,
                buttonBClicked,
                codeEntered
            ).merge()
        }

        appStore.updates.connect(this) {
            val secretCaveState =  it.secretCaveState
            Timber.d("updating Ui: $secretCaveState")

            progressBar.isVisible = secretCaveState.loading
            codeState.isVisible = !secretCaveState.loading

            buttonA.isEnabled = !secretCaveState.loading
            buttonB.isEnabled = !secretCaveState.loading

            enteredCode.text = secretCaveState.enteredCode

            if (secretCaveState.codeCorrect) {
                codeState.setImageResource(R.drawable.ic_check_circle_green_24dp)
            } else {
                codeState.setImageResource(R.drawable.ic_error_red_24dp)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SecretCaveActivity::class.java)
        }
    }

}

