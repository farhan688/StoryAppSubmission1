package com.example.storyappsubmission1.stories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val userName: String,
    val avtUrl: String,
    val desc: String,
): Parcelable