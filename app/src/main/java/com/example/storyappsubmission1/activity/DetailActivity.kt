package com.example.storyappsubmission1.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyappsubmission1.databinding.ActivityDetailBinding
import com.example.storyappsubmission1.stories.Story

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = retrieveParcelableData()
        if (story != null) {
            displayStoryDetail(story)
        }
    }

    private fun displayStoryDetail(story: Story) {
        loadUserPhoto(story.avtUrl)
        binding.tvUserName.text = story.userName
        binding.tvUserDesc.text = story.desc
    }

    private fun loadUserPhoto(url: String) {
        Glide.with(this@DetailActivity).load(url).into(binding.ivUserPhoto)
    }

    private fun retrieveParcelableData(): Story? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.getParcelableExtra(EXTRA_STORY)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }
    }
}
