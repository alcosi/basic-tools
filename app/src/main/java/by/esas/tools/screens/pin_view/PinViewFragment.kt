package by.esas.tools.screens.pin_view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainPinViewBinding

class PinViewFragment : AppFragment<PinViewVM, FMainPinViewBinding>() {

    override val fragmentDestinationId: Int = R.id.pinViewFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_pin_view
    }

    override fun provideViewModel(): PinViewVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(PinViewVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}