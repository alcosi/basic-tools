package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.basic.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

abstract class DataBindingFragment<VM : BaseViewModel<E>, B : ViewDataBinding, E : Enum<E>> :
    BaseFragment<E>() {

    protected lateinit var binding: B

    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    abstract fun provideLayoutId(): Int

    abstract fun provideTextInputETViewList(): List<TextInputEditText>

    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger = provideLogger()
        logger.setTag(TAG)
        logger.logInfo("onCreateView")

        viewModel = provideViewModel()
        viewModel.initLogger()

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()

        viewModel.alertDialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.logInfo("onViewCreated")
        if (viewModel.switchableViewsList.isNotEmpty()) viewModel.switchableViewsList.clear()
        viewModel.switchableViewsList.addAll(provideTextInputETViewList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
        viewModel.dismissDialogs()
        viewModel.alertDialogBuilder = null
        viewModel.switchableViewsList.clear()
    }
}