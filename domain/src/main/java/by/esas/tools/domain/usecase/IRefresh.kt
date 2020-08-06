package by.esas.tools.domain.usecase

import by.esas.tools.logger.IErrorModel

interface IRefresh<E : Enum<E>, Model : IErrorModel<E>> {
    fun getToken(): String
    fun refresh(repeat: () -> Unit, onError: (Model) -> Unit, onCancel: () -> Unit)
    fun onCancel()
}