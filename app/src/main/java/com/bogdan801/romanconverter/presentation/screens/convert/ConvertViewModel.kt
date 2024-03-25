package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.lifecycle.ViewModel
import com.bogdan801.romanconverter.domain.repository.Repository
import com.bogdan801.romanconverter.presentation.components.InputKeyboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(ConvertScreenState())
    val screenState = _screenState.asStateFlow()

    fun setRomanValue(romanValue: String){
        _screenState.update {
            it.copy(
                romanValue = romanValue
            )
        }
    }

    fun setArabicValue(arabicValue: String){
        _screenState.update {
            it.copy(
                arabicValue = arabicValue
            )
        }
    }

    fun setKeyboardType(type: InputKeyboardType){
        _screenState.update {
            it.copy(
                type = type
            )
        }
    }
}