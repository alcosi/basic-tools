package by.esas.tools.basedaggerui.interfaces.navigating

import android.os.Bundle
import androidx.navigation.NavDirections

interface INavigateVM {
    fun navigate(direction: NavDirections)

    fun navigate(direction: NavDirections, parameters: Bundle?)

    fun popBack() {
        popBack(destination = 0, inclusive = false, parameters = null)
    }

    fun popBack(destination: Int, inclusive: Boolean, parameters: Bundle?)
}