package com.yunushan.letgocasestudy.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yunushan.letgocasestudy.R
import com.yunushan.letgocasestudy.databinding.FragmentPriceDetectionQuestionnaireBinding
import com.yunushan.letgocasestudy.presentation.adapter.QuestionnareListAdapter
import com.yunushan.letgocasestudy.presentation.event.QuestionnareDetailsEvent
import com.yunushan.letgocasestudy.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PriceDetectionQuestionnaireFragment : Fragment() {

    private var _binding: FragmentPriceDetectionQuestionnaireBinding? = null
    private val binding get() = _binding!!
    private lateinit var questionOptionsAdapter: QuestionnareListAdapter
    private val sharedViewModel: MainViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.questionnareDetailFlow.collect {
                    when (it) {
                        is QuestionnareDetailsEvent.Error -> {
                            createErrorToast()
                        }

                        is QuestionnareDetailsEvent.FragmentListLoaded -> {
                            questionOptionsAdapter.submitList(it.itemDetail.itemList)
                            setTitle(it.itemDetail.titleId)
                        }

                        QuestionnareDetailsEvent.Idle -> Unit
                        QuestionnareDetailsEvent.NavigateMileageFragment -> {
                            findNavController().navigate(R.id.action_priceDetectionQuestionnaireFragment_to_mileageInputFragment)
                        }

                        is QuestionnareDetailsEvent.PricingResult -> {
                            //no-op
                        }
                    }
                }
            }
        }
    }

    private fun createErrorToast() {
        Toast.makeText(requireContext(), R.string.default_error_message, Toast.LENGTH_LONG).show()
    }

    private fun setTitle(titleId: Int) {
        binding.questionnareTitleText.text = getString(titleId)
    }

    private fun initAdapter() {
        questionOptionsAdapter = QuestionnareListAdapter {
            sharedViewModel.getNextPage(it)
        }
        binding.optionsRecycler.adapter = questionOptionsAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceDetectionQuestionnaireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}