package dev.dodo.borrowly.ui

import kotlinx.serialization.Serializable

sealed class AppDestination {

    @Serializable
    data object OnBoarding: AppDestination()

    @Serializable
    data object Login: AppDestination()

    @Serializable
    data object Register: AppDestination()

    @Serializable
    data object Main /*(
        val initialTab: String = "Beranda",
        val statusFilter: String? = null
      )*/ : AppDestination()

    @Serializable
    data object MyProduct: AppDestination()

    @Serializable
    data object LoanRequest: AppDestination()

    @Serializable
    data class EditProfile(
        val idUser: String
    ): AppDestination()

    @Serializable
    data class AddEditProduct(
        val idProduct: String? = null
    ): AppDestination()

    @Serializable
    data class DetailProduct(
        val idProduct: String
    ): AppDestination()

    @Serializable
    data class DetailLoanRequest(
        val idLoan: String
    ): AppDestination()

}