package com.paul.android.quizapp.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T: Any> DatabaseReference.setValueSuspend(value: T) = suspendCoroutine<T> { continuation ->
    this.setValue(value)
        .addOnSuccessListener {
            continuation.resume(value)
        }
        .addOnFailureListener {
            continuation.resumeWithException(it)
        }
}

suspend inline fun <reified T: Any> DatabaseReference.getValueOnceSuspend() = suspendCoroutine<T> { continuation ->
    this.addListenerForSingleValueEvent(
        object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? T
                if (value != null) {
                    continuation.resume(value)
                } else {
                    continuation.resumeWithException(TypeCastException())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        }
    )
}

inline fun <reified T: Any> DatabaseReference.asFlow() = callbackFlow<T> {
    val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.value as? T
            if (data == null) {
                close()
            } else {
                sendBlocking(data)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            close(error.toException())
        }
    }

    addValueEventListener(listener)

    awaitClose {
        removeEventListener(listener)
        cancel()
    }
}