package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vm.timemanager.data.Task
import com.vm.timemanager.databinding.FragmentNewTaskAddingBinding
import com.vm.timemanager.viewModel.NewTaskAddingViewModel
import java.time.LocalDateTime
import java.util.*

/**
 * Adding a new Task
 */
class NewTaskAdding : Fragment() {

    private var _binding: FragmentNewTaskAddingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<NewTaskAddingViewModel>()
    private var newDate: Date? = null
    private val args: NewTaskAddingArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_task_adding, container, false)
        _binding = FragmentNewTaskAddingBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModelBind = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.day = args.day

        //val viewModel = ViewModelProvider(this)[DaysViewModel::class.java]
//        val day = NewTaskAddingArgs.fromBundle(requireArguments()).day

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            viewModel.task?.value?.startTime = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as LocalDateTime
//            newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
//            binding.buttonStartDate.text = newDate?.toString()
            Toast.makeText(requireContext(), "Date:  ${viewModel.task?.value}", Toast.LENGTH_SHORT).show()
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
//            viewModel.task?.value?.startTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            viewModel.task?.value?.startTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as LocalDateTime
            Toast.makeText(requireContext(), "Date:  ${viewModel.task?.value}", Toast.LENGTH_SHORT).show()

//            binding.buttonStartTime.text = newTime?.toString()
        }

        binding.apply {

            buttonStartDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(viewModel.task?.value?.startTime ?: LocalDateTime.now())
                )
            }

            buttonEndDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(viewModel.task?.value?.startTime ?: LocalDateTime.now())
                )
            }

            buttonStartTime.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectTime(viewModel.task?.value?.startTime ?: LocalDateTime.now())
                )
            }


            saveButton.setOnClickListener {
                viewModel.addTask(Task(
                    day = viewModel.day,
                    startTime = viewModel.task?.value?.startTime,

                    /**
                     * must be in viewModel
                     */
//                    endTime = newDate,
                    taskName = viewModel.task?.value?.taskName ?: "",
                    taskDescription = viewModel.task?.value?.taskDescription ?: ""))
            }
        }


        return view
    }
}