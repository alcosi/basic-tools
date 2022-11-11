package by.esas.tools.screens.dialog

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.baseui.Config
import by.esas.tools.databinding.FMainDynamicMessageDialogBinding
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.dialog.MessageDialog.Companion.ITEM_NAME
import by.esas.tools.dialog.MessageDialog.Companion.USER_ACTION_ITEM_PICKED
import by.esas.tools.dialog.MessageDialog.Companion.USER_ACTION_NEGATIVE_CLICKED
import by.esas.tools.dialog.MessageDialog.Companion.USER_ACTION_NEUTRAL_CLICKED
import by.esas.tools.dialog.MessageDialog.Companion.USER_ACTION_POSITIVE_CLICKED
import by.esas.tools.logger.Action

private const val DYNAMIC_MESSAGE_DIALOG = "DYNAMIC_MESSAGE_DIALOG"

class DynamicMessageDialogFragment :
    AppFragment<DynamicMessageDialogVM, FMainDynamicMessageDialogBinding>() {
    override val fragmentDestinationId = R.id.dynamicMessageDialogFragment
    override fun provideLayoutId() = R.layout.f_main_dynamic_message_dialog

    override fun provideViewModel(): DynamicMessageDialogVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(DynamicMessageDialogVM::class.java)
    }

    override fun provideRequestKeys(): List<String> {
        return listOf(DYNAMIC_MESSAGE_DIALOG, Config.ERROR_MESSAGE_DIALOG)
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == DYNAMIC_MESSAGE_DIALOG) {
            FragmentResultListener { key, result ->
                val actionName = result.getString(by.esas.tools.dialog.Config.DIALOG_USER_ACTION)
                if (!actionName.isNullOrBlank()) {
                    handleAction(Action(actionName, result))
                } else {
                    enableControls(result)
                }
            }
        } else {
            super.provideFragmentResultListener(requestKey)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPositiveButtonLay()
        setNegativeButtonLay()
        setNeutralButtonLay()

        binding.fDynamicMessageDialogShowButton.setOnClickListener {
            showDialog(createDialog(), DYNAMIC_MESSAGE_DIALOG)
        }
    }

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            USER_ACTION_POSITIVE_CLICKED -> Toast.makeText(
                requireContext(),
                resources.getString(R.string.message_dialog_positive_click),
                Toast.LENGTH_SHORT
            ).show()
            USER_ACTION_NEGATIVE_CLICKED -> Toast.makeText(
                requireContext(),
                resources.getString(R.string.message_dialog_negative_click),
                Toast.LENGTH_SHORT
            ).show()
            USER_ACTION_NEUTRAL_CLICKED -> Toast.makeText(
                requireContext(),
                resources.getString(R.string.message_dialog_neutral_click),
                Toast.LENGTH_SHORT
            ).show()
            USER_ACTION_ITEM_PICKED -> Toast.makeText(
                requireContext(),
                action.parameters?.getString(ITEM_NAME),
                Toast.LENGTH_SHORT
            ).show()
            else -> return super.handleAction(action)
        }
        return true
    }

    private fun setPositiveButtonLay() {
        binding.fDynamicMessageDialogBtnPositiveCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicMessageDialogBtnPositiveStyleLay.visibility = layVisibility
            binding.fDynamicMessageDialogBtnPositiveColorLay.visibility = layVisibility
        }
    }

    private fun setNegativeButtonLay() {
        binding.fDynamicMessageDialogBtnNegativeCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicMessageDialogBtnNegativeStyleLay.visibility = layVisibility
            binding.fDynamicMessageDialogBtnNegativeColorLay.visibility = layVisibility
        }
    }

    private fun setNeutralButtonLay() {
        binding.fDynamicMessageDialogBtnNeutralCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicMessageDialogBtnNeutralStyleLay.visibility = layVisibility
            binding.fDynamicMessageDialogBtnNeutralColorLay.visibility = layVisibility
        }
    }

    private fun createDialog(): MessageDialog {
        val dialog = MessageDialog()
        dialog.setRequestKey(DYNAMIC_MESSAGE_DIALOG)
        dialog.setTitle(
            binding.fDynamicMessageDialogTitle.text.toString(),
            getTextStyle(binding.fDynamicMessageDialogSpinnerTitle.selectedItem.toString())
        )
        dialog.setMessage(
            binding.fDynamicMessageDialogMessage.text.toString(),
            getTextStyle(binding.fDynamicMessageDialogSpinnerMessage.selectedItem.toString())
        )
        dialog.setPositiveButton(
            binding.fDynamicMessageDialogBtnPositive.text.toString(),
            appearance = getPositiveButtonAppearance()
        )
        dialog.setNegativeButton(
            binding.fDynamicMessageDialogBtnNegative.text.toString(),
            getNegativeButtonAppearance()
        )
        dialog.setNeutralButton(
            binding.fDynamicMessageDialogBtnNeutral.text.toString(),
            getNeutralButtonAppearance()
        )
        dialog.isCancelable = binding.fDynamicMessageDialogCloseCheck.isChecked
        setDialogItems(dialog)

        return dialog
    }

    private fun getTextStyle(style: String): Int {
        return when (style) {
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }

    private fun getPositiveButtonAppearance(): MessageDialog.ButtonAppearance? {
        return if (binding.fDynamicMessageDialogBtnPositiveCheck.isChecked)
            MessageDialog.ButtonAppearance(
                getTextStyle(binding.fDynamicMessageDialogBtnPositiveSpinner.selectedItem.toString()),
                getColorFromRadioGroup(binding.fDynamicMessageDialogBtnPositiveColor as RadioGroup),
                binding.fDynamicMessageDialogBtnPositiveCaps.isChecked
            ) else
            null
    }

    private fun getNegativeButtonAppearance(): MessageDialog.ButtonAppearance? {
        return if (binding.fDynamicMessageDialogBtnNegativeCheck.isChecked)
            MessageDialog.ButtonAppearance(
                getTextStyle(binding.fDynamicMessageDialogBtnNegativeSpinner.selectedItem.toString()),
                getColorFromRadioGroup(binding.fDynamicMessageDialogBtnNegativeColor as RadioGroup),
                binding.fDynamicMessageDialogBtnNegativeCaps.isChecked
            ) else
            null
    }

    private fun getNeutralButtonAppearance(): MessageDialog.ButtonAppearance? {
        return if (binding.fDynamicMessageDialogBtnNeutralCheck.isChecked)
            MessageDialog.ButtonAppearance(
                getTextStyle(binding.fDynamicMessageDialogBtnNeutralSpinner.selectedItem.toString()),
                getColorFromRadioGroup(binding.fDynamicMessageDialogBtnNeutralColor as RadioGroup),
                binding.fDynamicMessageDialogBtnNeutralCaps.isChecked
            ) else
            null
    }

    private fun setDialogItems(dialog: MessageDialog) {
        if (binding.fDynamicMessageDialogItemsListCheck.isChecked)
            dialog.setItems(
                listOf(
                    resources.getString(R.string.item_1),
                    resources.getString(R.string.item_2),
                    resources.getString(R.string.item_3)
                )
            )
    }

    private fun getColorFromRadioGroup(radioGroup: RadioGroup): Int {
        val checkedButtonId = radioGroup.checkedRadioButtonId
        val checkedButton = radioGroup.findViewById<RadioButton>(checkedButtonId)

        return when (radioGroup.indexOfChild(checkedButton)) {
            0 -> R.color.orange
            1 -> R.color.purple
            else -> R.color.red
        }
    }
}