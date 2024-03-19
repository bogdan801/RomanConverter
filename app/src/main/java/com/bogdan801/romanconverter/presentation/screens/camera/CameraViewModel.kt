package com.bogdan801.romanconverter.presentation.screens.camera

import androidx.lifecycle.ViewModel
import com.bogdan801.romanconverter.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(CameraScreenState())
    val screenState = _screenState.asStateFlow()

    fun setNewValue(newValue: String){
        _screenState.update {
            it.copy(
                someValue = newValue
            )
        }
    }

}