package ru.evbobrutskov.tinkoffandroiddeveloper2021.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.evbobrutskov.tinkoffandroiddeveloper2021.MainActivity
import ru.evbobrutskov.tinkoffandroiddeveloper2021.databinding.FragmentContentBinding

enum class DevelopersLifeApiStatus { LOADING, ERROR, DONE }

class ContentFragment(position: Int) : Fragment() {

    private var _binding:  FragmentContentBinding? = null
    private val binding get() = _binding!!

    private val category = MainActivity.getCategory(position)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater)

        try {
            binding.lifecycleOwner = this

            binding.viewModel = (activity as MainActivity).viewModel

            binding.fabNext.setOnClickListener {
                onButtonNextClick()
            }
            binding.fabPrev.setOnClickListener {
                onButtonPrevClick()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun onButtonNextClick() {
        try {
            (activity as MainActivity).apply {
                if (megaIndex[category] == 0) {
                    binding.fabPrev.visibility = View.VISIBLE
                }
                megaIndex[category] = (megaIndex[category] ?: 0) + 1
                getData(category, megaIndex[category] ?: 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onButtonPrevClick() {
        try {
            (activity as MainActivity).apply {
                if (megaIndex[category] ?: 0 > 0) {
                    megaIndex[category] = (megaIndex[category] ?: 0) - 1
                    if (megaIndex[category] ?: 0 == 0) {
                        binding.fabPrev.visibility = View.INVISIBLE
                    }
                    getData(category, megaIndex[category] ?: 0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}