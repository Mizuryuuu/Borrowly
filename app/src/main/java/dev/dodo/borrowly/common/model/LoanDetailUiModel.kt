package dev.dodo.borrowly.common.model

data class LoanDetailUiModel(
    val loan: Loan? = null,
    val product: ProductUiModel? = null,
    val owner: User? = null,
    val borrower: User? = null
)
