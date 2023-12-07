package com.example.storyappsubmission1.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsubmission1.R
import com.example.storyappsubmission1.databinding.ActivityMainBinding
import com.example.storyappsubmission1.response.ListStoryItem
import com.example.storyappsubmission1.stories.Story
import com.example.storyappsubmission1.stories.StoryAdapter
import com.example.storyappsubmission1.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val token = getSharedPreferences("LoginSession", Context.MODE_PRIVATE).getString("token", "")
        viewModel.getStories(token.toString())
        viewModel.isLoading.observe(this) { isLoading -> showLoading(isLoading) }
        viewModel.listStory.observe(this) { listStory -> setUserStories(listStory) }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val fabSetting: FloatingActionButton = findViewById(R.id.menu_setting)
        fabSetting.setOnClickListener {
            logoutUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                logoutUser()
            }
        }
        return true
    }

    private fun setUserStories(stories: List<ListStoryItem>) {
        val list = ArrayList<Story>()
        for (story in stories) {
            val storyData = Story(story.id.toString(), story.name.toString(), story.photoUrl.toString(), story.description.toString())
            list.add(storyData)
        }

        val listStory = StoryAdapter(list)
        binding.rvStories.adapter = listStory

        listStory.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val storyDetail = Story(data.id, data.userName, data.avtUrl, data.desc)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, storyDetail)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadMain.visibility = View.VISIBLE
        } else {
            binding.loadMain.visibility = View.INVISIBLE
        }
    }

    private fun logoutUser() {
        getSharedPreferences("LoginSession", Context.MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finishAffinity()
    }
}
