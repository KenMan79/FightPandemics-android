package com.fightpandemics.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.result.Event
import com.fightpandemics.domain.OnBoardCompleteActionUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class OnBoardViewModel @Inject constructor(
    private val onBoardCompleteActionUseCase: OnBoardCompleteActionUseCase,
) : ViewModel() {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    fun skipToHelpBoardClick() {
        viewModelScope.launch {
            onBoardCompleteActionUseCase(true)
            _navigateToMainActivity.value = Event(Unit)
        }
    }
}