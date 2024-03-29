package by.esas.tools

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.checker.Checker
import by.esas.tools.checker.IRequestFocusHandler
import by.esas.tools.checker.checks.LengthCheck
import by.esas.tools.checking.AppChecker
import by.esas.tools.checking.FieldChecking
import by.esas.tools.databinding.ActivityMainBinding
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import by.esas.tools.usecase.GetDefaultCardUseCase
import by.esas.tools.util.LanguageSetter
import by.esas.tools.util.hideSystemUIR
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers

open class MainActivity : AppActivity<MainVM, ActivityMainBinding>() {
    override val TAG: String = "MainActivity"
    override var logger: ILogger<AppErrorStatusEnum, *> = LoggerImpl()
    override fun provideViewModel(): MainVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MainVM::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun provideVariableInd(): Int {
        return BR.viewModel
    }

    override fun provideLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun provideSetter(): LanguageSetter {
        return object : LanguageSetter {
            override fun getDefaultLanguage(): String {
                return "en"
            }

            override fun getLanguage(): String {
                return "en"
            }

            override fun setLanguage(lang: String) {

            }
        }
    }

    override fun getAppContext(): Context {
        return App.appContext
    }

    override fun setAppContext(context: Context) {
        App.appContext = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBar = insets.getSystemWindowInsetTop()
            val navigationBar = insets.getSystemWindowInsetBottom()
            binding.root.updatePadding(top = statusBar)
            return@setOnApplyWindowInsetsListener insets
        }
        /*viewModel.update = {
            binding.fMainAddInvoiceAdditionalContainer.invalidate()
        }*/

        binding.fMainSetErrorPin.setOnClickListener {
            doCheck()
            if (!binding.aMainText3.getText().equals("12345678")) {
                binding.aMainText3.setError("Some error")
            } else {
                binding.aMainText3.setError("")
            }
        }
    }

    fun doCheck() {
        AppChecker()
            .setShowError(true)
            .setListener(object : Checker.CheckListener {})
            .validate(
                listOf(
                    FieldChecking(binding.aMainText4, true)
                        .setRequestFocusHandler(object : IRequestFocusHandler {
                            override fun handleRequestFocus() {
                                binding.aMainText4.visibility = View.VISIBLE
                                binding.aMainText4.requestFocus()
                                //scroll to position
                            }
                        })
                        .addCheck(LengthCheck(0, 4))
                )
            )
    }
    /*val field = findViewById<InputFieldView>(R.id.a_main_text2)
    field.setInputPrefix("1")*/
    /*val spinner = findViewById<SpinnerFieldView>(R.id.a_main_service_spinner)
    spinner.adapter.clear()
    (spinner.adapter as ArrayAdapter<String>).addAll(listOf("1", "2", "3"))
    spinner.adapter.notifyDataSetChanged()
    val currentText = "1"
    spinner.setText(currentText)*/
    /*viewModel.addToObservable(viewModel.mainText, Observer<String> {
        AppChecker()
            .setShowError(true)
            .setListener(object : Checker.CheckListener {})
            .validate(listOf(FieldChecking(field, true).addCheck(LengthCheck(0, 4))))
    })*/


    override fun setFullScreen() {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                }
                statusBarColor = Color.TRANSPARENT
            }

        }
    }

    fun testError() {
        val uc = GetDefaultCardUseCase(
            AppErrorMapper(Moshi.Builder().build(), LoggerImpl()),
            Dispatchers.Main
        )
        uc.execute {
            onComplete {

            }
            onError {
                it.statusEnum
            }
            onCancel {

            }
        }
        val model = BaseErrorModel(0, AppErrorStatusEnum.APP_UNPREDICTED_ERROR)
        model.statusEnum
    }

    override fun hideSystemUI() {
        logger.logInfo("hideSystemUI")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemUIR(this)
        } else hideSystemUIApp(this)
    }


}
