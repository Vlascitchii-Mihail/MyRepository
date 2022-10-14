package file_writer

import java.io.File
import java.time.LocalDate
import kotlin.random.Random
import kotlin.system.measureTimeMillis


object Statistics {
    var quickSortComparisons = 0
    var quickSortSwaps = 0

    var mergeSortComparisons = 0
    var mergeSortDividing = 0
}


/**
 * contains personal information about Employees
 */
class Employee(
    val Id: Int? = null,
    val name: String = "",
    val surname: String = "",
    val birthDate: LocalDate? = null,
    var salary: Int? = null
) {

    /**
     * @return information about the Employee in string format
     */
    fun getEmployeeInfo() =
        "ID: $Id  $surname $name  Birth Date: $birthDate  salary: $salary"


    companion object {

        /**
         * read data from the files and create the Employee's objects using the data from the file
         * @param employee - empty List which will be filled with Employee's objects
         */
        fun randomEmployee(employee: MutableList<Employee>) {

            //read random data from the files
            val names: List<String>  = File("User's names.txt").readLines()
            val surname: List<String> = File("User's surnames.txt").readLines()


            //create random Employee objects, items
            for (i in 0 .. 99) {
                employee.add(Employee(
                    Id = (10_000 .. 100_000).random(),
                    name = names.random(),
                    surname = surname.random(),
                    birthDate = LocalDate.of(1987,1,1)
                        .plusDays(Random.nextLong(6205)),
                    salary = (10_000 .. 15_000).random()
                )
                )
            }

            //write to file users' names
            writeToFile(employee.toTypedArray())
        }


        /**
         * sort the users from file
         */
        fun quickSortEmployee(filePath: String) {

            //loading the user's names from file
            val usersArray =  loadingFromFile(filePath).toTypedArray()

            //speed of the quick sort
            val quickSortSpeed = measureTimeMillis {

                //sort the usersList using QuickSort
                quickSort(usersArray, 0, usersArray.size - 1)
            }

            println("The speed of the Quick Sort algorithm: $quickSortSpeed msc\n" +
                    "The number of comparisons: ${Statistics.quickSortComparisons}\n" +
                    "The number of swaps: ${Statistics.quickSortSwaps}\n")

            //write user's names in a new file
            writeToFile(usersArray)
        }


        /**
         * start the Merge Sort and write the data to file
         */
        fun mergeSortEmployee(filePath: String) {
            val usersArray = loadingFromFile(filePath).toTypedArray()

            //find the speed of the merge sort
            val speedInMillis = measureTimeMillis {

                //start MergeSort
                mergeSort(usersArray)
            }

            println("The speed of the Merge Sort algorithm is: $speedInMillis msc\n" +
                    "The number of comparisons: ${Statistics.mergeSortComparisons}\n" +
                    "The number of dividing and merges: ${Statistics.mergeSortDividing * 2}\n")

            //write to file
            writeToFile(usersArray)
        }
    }
}




/**
 * loading the user's names from file
 */
fun loadingFromFile(filePath: String): MutableList<Employee> {
    val usersList: MutableList<Employee> = mutableListOf()

    //converting the strings from the file to Employee's objects
    File(filePath).readLines().map { nameString ->
        val splitString = nameString.split(" ")
        if (splitString[0].isNotEmpty()) {
            usersList.add(
                Employee(
                    Id = splitString[1].toInt(),
                    name = splitString[4],
                    surname = splitString[3],
                    birthDate = LocalDate.parse(splitString[8]),
                    salary = splitString[11].toInt()
                )
            )
        }
    }

    return usersList
}


/**
 * write the data about the Employee's array to file
 * @param employee array of Employee's objects
 */
fun writeToFile(employee: Array<Employee>) {
    println("Enter the name of the file: ")

    //write the file name from the console
    //let - checking the input if it isn't null
    //write data to the file
    readLine()?.let { fileName ->
        File(fileName).printWriter().use { out ->
            employee.forEach { employee->
                out.println(employee.getEmployeeInfo() + "\n")
            }
        }
    }
}




/**
 * sort function using the Quick Sort algorithm
 */
fun quickSort(usersArray: Array<Employee>, left: Int, right: Int) {

    //compare elements and swap them, return center of the array
    val center = partition (usersArray, left, right)

    //Sorting left half
    if(left < center-1) {
        quickSort(usersArray, left, center-1)
    }

    //Sorting right half
    if(center < right) {
        quickSort(usersArray,center, right)
    }
}




/**
 * find the center of the array and compare elements which are bigger or smaller than pivot element
 */
private fun partition(array: Array<Employee>, lft: Int, rgt: Int): Int {
    var left = lft
    var right = rgt

    //Pivot Point
    val pivot = array[(left + right)/2].surname
    while (left <= right) {

        //Find the elements on left that should be on right
        while (array[left].surname < pivot) {
            Statistics.quickSortComparisons++
            left++
        }

        // Find the elements on right that should be on left
        while (array[right].surname > pivot) {
            Statistics.quickSortComparisons++
            right--
        }

        //Swap elements, and move left and right indices
        if (left <= right) {
            swapArray(array, left,right)
            left++
            right--
        }
    }
    return left
}



/**
 * swapping the elements of the array
 */
private fun swapArray(array: Array<Employee>, left: Int, right: Int) {
    val temp = array[left]
    array[left] = array[right]
    array[right] = temp
    Statistics.quickSortSwaps++
}




/**
 * using the Merge algorithm
 */
fun merge(leftArray: Array<Employee>, rightArray:Array<Employee>, resultArray:Array<Employee>) {
    var iLeft=0
    var jRight=0

    //resultArray.indices - Returns the range of valid indices for the array.
    for(result in resultArray.indices) {

        Statistics.mergeSortComparisons++

        //if right array is finished or if left array isn't finished and if left value less than right value
        if((jRight>=rightArray.size) || (iLeft<leftArray.size && leftArray[iLeft].surname <=rightArray[jRight].surname)) {
            resultArray[result]=leftArray[iLeft]
            iLeft++
        } else {
            resultArray[result]=rightArray[jRight]
            jRight++
        }
    }
}

/**
 * first coll of Merge function, which using recursion to divide the array
 */
fun mergeSort(initArray:Array<Employee>) {
    if(initArray.size<=1)
        return

    //dividing the array on 2 parts, last index exclusive
    val leftArray = initArray.copyOfRange(0,initArray.size/2)
    val rightArray = initArray.copyOfRange(initArray.size/2, initArray.size)

    Statistics.mergeSortDividing++
    //using recursion to divide the arrays
    mergeSort(leftArray)

    Statistics.mergeSortDividing++
    mergeSort(rightArray)

    //calling the Merge algorithm
    merge(leftArray,rightArray, initArray)
}



fun main() {
    val employee: MutableList<Employee> = mutableListOf()

////    read data from the files and create the Employee's objects using the data from the file
//    Employee.randomEmployee(employee)

//    sort the users from file
    Employee.quickSortEmployee("Users.txt")

    Employee.mergeSortEmployee("Users.txt")
}