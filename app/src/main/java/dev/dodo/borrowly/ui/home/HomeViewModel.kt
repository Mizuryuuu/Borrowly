package dev.dodo.borrowly.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        refreshProduct()
                    } else {
                        searchProduct(query)
                    }
                }
        }
    }

    fun resetSearch() {
        _uiState.update {
            it.copy(searchQuery = "", isEmpty = false)
        }
        queryFlow.value = ""
    }

    fun refreshUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            userRepository.getUser().onSuccess { user ->
                _uiState.update { it.copy(user = user) }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onChangeSearch(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
        queryFlow.value = query
    }

    fun refreshProduct() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = productRepository.getProducts()
            result.onSuccess { products ->
                val uiModel = products.map { product ->
                    async {
                        val user = userRepository.getUserById(product.ownerId)
                        Log.d("DEBUG USER", user.toString())
                        user.getOrNull()?.let {
                            ProductUiModel(
                                product = product,
                                username = it.username,
                                location = it.address,
                                phoneNumber = it.phoneNumber
                            )
                        }
                    }
                }.awaitAll().filterNotNull()
                _uiState.update {
                    it.copy(product = uiModel)
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun searchProduct(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = productRepository.searchProducts(query)
            result.onSuccess { products ->
                val uiModel = products.map { product ->
                    async {
                        val user = userRepository.getUserById(product.ownerId)
                        Log.d("DEBUG USER", user.toString())
                        user.getOrNull()?.let {
                            ProductUiModel(
                                product = product,
                                username = it.username,
                                location = it.address,
                                phoneNumber = it.phoneNumber
                            )
                        }
                    }
                }
                val finalResults = uiModel.awaitAll().filterNotNull()
                _uiState.update {
                    it.copy(product = finalResults, isEmpty = finalResults.isEmpty())
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}