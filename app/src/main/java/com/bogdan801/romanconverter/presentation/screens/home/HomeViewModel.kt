package com.bogdan801.romanconverter.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.bogdan801.romanconverter.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState = _screenState.asStateFlow()

    fun showNavBar(value: Boolean){
        _screenState.update {
            it.copy(
                isNavBarExpanded = value
            )
        }
    }

    fun blurBackground(value: Boolean){
        _screenState.update {
            it.copy(
                shouldBlur = value
            )
        }
    }
}