package ru.whatiamdoing.global.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.whatiamdoing.global.App
import ru.whatiamdoing.global.R
import ru.whatiamdoing.global.Screens
import javax.inject.Inject

class AppActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator by lazy {
        object : AppNavigator(this, R.id.container) {
            override fun setupFragmentTransaction(
                fragmentTransaction: FragmentTransaction,
                currentFragment: Fragment?,
                nextFragment: Fragment?
            ) {
                super.setupFragmentTransaction(fragmentTransaction, currentFragment, nextFragment)
                fragmentTransaction.setReorderingAllowed(true)
            }

            override fun applyCommands(commands: Array<out Command>) {
                hideKeyboard()
                super.applyCommands(commands)
            }

            override fun errorOnApplyCommand(command: Command, error: RuntimeException) {
                super.errorOnApplyCommand(command, error)
                Handler(Looper.getMainLooper()).postDelayed(
                    { applyCommand(command) },
                    100
                )
            }
        }
    }

    private val appComponent by lazy { App.appComponent }

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_container)
        if (savedInstanceState == null) {
            navigator.applyCommands(
                arrayOf(
                    BackTo(null),
                    Replace(Screens.main())
                )
            )
        }
    }

    fun hideKeyboard() {
        currentFocus?.let {
            it.post {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}