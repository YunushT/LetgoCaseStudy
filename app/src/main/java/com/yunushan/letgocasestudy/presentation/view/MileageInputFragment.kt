package com.yunushan.letgocasestudy.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yunushan.letgocasestudy.R
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleInfo
import com.yunushan.letgocasestudy.data.model.uimodel.VehiclePricingUIModel
import com.yunushan.letgocasestudy.databinding.FragmentMileageInputBinding
import com.yunushan.letgocasestudy.presentation.event.PriceResultEvent
import com.yunushan.letgocasestudy.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MileageInputFragment : Fragment() {

    private var _binding: FragmentMileageInputBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initUI()
    }

    private fun initUI() {
        with(binding) {
            textButton.setOnClickListener {
                if (inputEditText.text?.isNotEmpty() == true) {
                    hideKeyboard()
                    sharedViewModel.getVehiclePrice(inputEditText.text.toString())
                }
            }
        }
    }

    private fun hideKeyboard() {
        binding.inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.priceResultDetailFlow.collect {
                    when (it) {
                        is PriceResultEvent.Error -> {
                            createErrorToast()
                        }

                        PriceResultEvent.Idle -> Unit
                        is PriceResultEvent.PricingResult -> {
                            setResultUI(it.priceUIDetail, it.vehicleInfo)
                        }
                    }
                }
            }
        }
    }

    private fun createErrorToast() {
        Toast.makeText(requireContext(), R.string.default_error_message, Toast.LENGTH_LONG).show()
    }

    private fun setResultUI(priceUIDetail: VehiclePricingUIModel, vehicleInfo: VehicleInfo) {
        with(binding) {
            priceUIDetail.pageTitle?.let {
                titleText.text = getString(it)
            }
            hideMileageViews()
            resultModelYear.text = getString(R.string.model_year_key, vehicleInfo.vehicleModelYear)
            resultTrim.text = getString(R.string.result_trim_key, vehicleInfo.vehicleTrimName)
            resultEngine.text = getString(R.string.result_engine_key, vehicleInfo.vehicleEngineName)
            resultGearType.text =
                getString(R.string.result_gear_type_key, vehicleInfo.vehicleGearTypeName)
            resultColor.text = getString(R.string.result_color_key, vehicleInfo.vehicleColorName)
            resultUsedMileage.text =
                getString(R.string.result_used_mileage_key, vehicleInfo.vehicleMileage)
            resultPrice.text =
                getString(R.string.result_price_offer_key, priceUIDetail.price.toString())
            showResultViews()
        }
    }

    private fun showResultViews() {
        with(binding) {
            resultModelYear.visibility = View.VISIBLE
            resultTrim.visibility = View.VISIBLE
            resultEngine.visibility = View.VISIBLE
            resultGearType.visibility = View.VISIBLE
            resultColor.visibility = View.VISIBLE
            resultUsedMileage.visibility = View.VISIBLE
            resultPrice.visibility = View.VISIBLE
        }
    }

    private fun hideMileageViews() {
        with(binding) {
            textField.visibility = View.GONE
            textButton.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMileageInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}