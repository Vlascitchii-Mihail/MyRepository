package com.vm.timemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.vm.timemanager.R
import com.vm.timemanager.data.Task
import com.vm.timemanager.databinding.FragmentNewTaskAddingBinding
import com.vm.timemanager.viewModel.NewTaskAddingViewModel
import com.vm.timemanager.workManager.PollWorker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "POLL_WORK"

/**
 * Adding a new Task
 */
class NewTaskAdding : Fragment() {

    private var _binding: FragmentNewTaskAddingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<NewTaskAddingViewModel>()
//    private var newDate: Date? = null
    private val args: NewTaskAddingArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_task_adding, container, false)
        _binding = FragmentNewTaskAddingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.toolbar.inflateMenu(R.menu.toolbars_menu)

        //reference to the navigation controller
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //connect appBarConfiguration with nav_graph
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()

        //applies the configuration to the toolbar
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        viewModel.day = args.day

        binding.apply {
            if (args.id != 0) {
                viewModel.getTask(args.id)
                addButton.visibility = View.GONE
            }
            else {
                viewModel.task.value = Task()
                saveButton.visibility = View.GONE
            }

            viewModelBind = viewModel
            lifecycleOwner = viewLifecycleOwner

            buttonStartDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(viewModel.task.value?.startTime ?: LocalDateTime.now(), DatePickerFragment.REQUEST_START_DATE)
                )
            }

            buttonEndDate.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectDate(viewModel.task.value?.endTime ?: viewModel.task.value?.startTime ?: LocalDateTime.now(), DatePickerFragment.REQUEST_END_DATE)
                )
            }

            buttonStartTime.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectTime(viewModel.task.value?.startTime ?: LocalDateTime.now(), TimePickerFragment.REQUEST_START_TIME)
                )
            }

            buttonEndTime.setOnClickListener {
                findNavController().navigate(
                    NewTaskAddingDirections.selectTime(viewModel.task.value?.endTime ?: viewModel.task.value?.startTime ?: LocalDateTime.now(), TimePickerFragment.REQUEST_END_TIME)
                )
            }

            addButton.setOnClickListener {
                viewModel.addTask(Task(
                    day = viewModel.day,
                    startTime = viewModel.task.value?.startTime,
                    endTime = viewModel.task.value?.endTime,
                    taskName = viewModel.task.value?.taskName ?: "No name",
                    taskDescription = viewModel.task.value?.taskDescription ?: "No description"))
                navController.popBackStack()
            }

            saveButton.setOnClickListener {
                viewModel.task.value?.let {
                    viewModel.updateTask(it)
                    navController.popBackStack()
                }
            }

            toolbar.setOnMenuItemClickListener {
               when (it.itemId) {
                   R.id.delete_a_task -> {
                       viewModel.deleteTask()
//                       activity?.onBackPressed()
                       navController.popBackStack()
                   true
                   }
                   else -> false
               }
            }
        }

        //val viewModel = ViewModelProvider(this)[DaysViewModel::class.java]
//        val day = NewTaskAddingArgs.fromBundle(requireArguments()).day

        setFragmentResultListener(DatePickerFragment.REQUEST_START_DATE) { _, bundle ->
            viewModel.task.value?.startTime = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as LocalDateTime
//            Toast.makeText(requireContext(), "Date:  ${viewModel.task.value}", Toast.LENGTH_SHORT).show()
//            this.activity?.recreate()

            binding.startDate.text = viewModel.task.value?.startTime.let {
                it?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }

        setFragmentResultListener(DatePickerFragment.REQUEST_END_DATE) { _, bundle ->
            viewModel.task.value?.endTime = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as LocalDateTime
//            Toast.makeText(requireContext(), "Date:  ${viewModel.task.value}", Toast.LENGTH_SHORT).show()
//            this.activity?.recreate()

            binding.endDate.text = viewModel.task.value?.endTime.let {
                it?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_START_TIME) { _, bundle ->
            viewModel.task.value?.startTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as LocalDateTime
//            Toast.makeText(requireContext(), "Date:  ${viewModel.task.value}", Toast.LENGTH_SHORT).show()
//            this.activity?.recreate()

            binding.startTime.text = viewModel.task.value?.startTime.let {
                it?.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_END_TIME) { _, bundle ->
            viewModel.task.value?.endTime = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as LocalDateTime
//            Toast.makeText(requireContext(), "Date:  ${viewModel.task.value}", Toast.LENGTH_SHORT).show()
//            this.activity?.recreate()

            binding.endTime.text = viewModel.task.value?.endTime.let {
                it?.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            }
        }

        //battery constraint
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()

        val periodicWorkerRequest = PeriodicWorkRequestBuilder<PollWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints).build()

        //planning the request
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            POLL_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkerRequest)

        return view
    }
}