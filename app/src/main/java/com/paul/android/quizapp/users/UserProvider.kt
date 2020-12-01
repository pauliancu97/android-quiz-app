package com.paul.android.quizapp.users

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProvider @Inject constructor() {

    private val userStateFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val userFlow: Flow<FirebaseUser?> = userStateFlow

    fun getUser() = FirebaseAuth.getInstance().currentUser

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun update() {
        userStateFlow.value = getUser()
    }
}