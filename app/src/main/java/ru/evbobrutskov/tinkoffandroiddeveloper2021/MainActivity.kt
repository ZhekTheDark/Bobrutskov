package ru.evbobrutskov.tinkoffandroiddeveloper2021

import android.graphics.Color
import android.os.Bundle
import android.util.LruCache
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.evbobrutskov.tinkoffandroiddeveloper2021.content.ContentFragmentPagerAdapter
import ru.evbobrutskov.tinkoffandroiddeveloper2021.content.ContentViewModel
import ru.evbobrutskov.tinkoffandroiddeveloper2021.databinding.ActivityMainBinding
import ru.evbobrutskov.tinkoffandroiddeveloper2021.network.DevelopersLifePropertyContainer

const val cacheSize = 64 * 1024 * 1024

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val viewModel: ContentViewModel by lazy {
        ViewModelProvider(this).get(ContentViewModel::class.java)
    }

    val megaCache = mapOf<String, LruCache<Int, DevelopersLifePropertyContainer>>(
        "latest" to LruCache<Int, DevelopersLifePropertyContainer>(cacheSize),
        "hot" to LruCache<Int, DevelopersLifePropertyContainer>(cacheSize),
        "top" to LruCache<Int, DevelopersLifePropertyContainer>(cacheSize)
    )
    val megaIndex = mutableMapOf<String, Int>(
        "latest" to 0,
        "hot" to 0,
        "top" to 0
    )

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val tabs = binding.tabs.apply {
            setSelectedTabIndicatorColor(Color.BLUE)
            setTabTextColors(Color.BLACK, Color.BLUE)
        }

        viewPager = binding.pager.apply {
            adapter = ContentFragmentPagerAdapter(this@MainActivity)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    getData(getCategory(position), megaIndex[getCategory(position)] ?: 0)
                }
            })

        }

        setTabLayoutMediator(tabs)
    }

    private fun setTabLayoutMediator(tabs: TabLayout) {
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Последнее"
                }
                1 -> {
                    tab.text = "Лучшее"
                }
                else -> {
                    tab.text = "Горячее"
                }
            }
        }.attach()
    }

    fun getData(category: String, page: Int) {
        var data = megaCache[category]?.get(page)
        if (data == null) {
            viewModel.getDevelopersLifeProperties(category, page, this)
        } else {
            viewModel.setDevelopersLifeProperties(data)
        }
    }

    companion object {
        fun getCategory(position: Int): String {
            return when (position) {
                0 -> {
                    "latest"
                }
                1 -> {
                    "hot"
                }
                else -> {
                    "top"
                }
            }
        }
    }
}

