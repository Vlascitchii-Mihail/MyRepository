package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vm.timemanager.data.Task
import com.vm.timemanager.databinding.FragmentNewTaskAddingBinding
import com.vm.timemanager.viewModel.NewTaskAddingViewModel
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
            viewModel.task?.value = Task(startTime = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date)
//            newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
//            binding.buttonStartDate.text = newDate?.toString()
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
            viewModel.task?.value?.startTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
//            binding.buttonStartTime.text = newTime?.toString()
        }

        binding.apply {

            buttonStartDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(viewModel.task?.value?.startTime ?: Calendar.getInstance().time)
                )
            }

            buttonEndDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(Calendar.getInstance().time)
                )
            }

            buttonStartTime.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectTime(viewModel.task?.value?.startTime ?: Calendar.getInstance().time)
                )
            }


            saveButton.setOnClickListener {
                viewModel.addTask(Task(
                    day = viewModel.day,
                    startTime = viewModel.task?.value?.startTime,

                    /**
                     * must be in viewModel
                     */
                    endTime = newDate,
                    taskName = viewModel.task?.value?.taskName ?: "",
                    taskDescription = viewModel.task?.value?.taskDescription ?: ""))
            }
        }


        return view
    }
}