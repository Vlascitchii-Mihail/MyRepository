package file_writer

import java.io.File
import java.time.LocalDate
import kotlin.random.Random

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
            //read random data from the file
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

            //write in file users' names
            writeToFile(employee.toTypedArray())
        }



        /**
         * sort the users from file
         */
        fun sortEmployee(filePath: String) {
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

//            println(usersList)

            val usersArray = usersList.toTypedArray()

            //sort the usersList
            quickSort(usersArray, 0, usersList.size - 1)

            //write user's names in a new file
            writeToFile(usersArray)
        }
    }
}




/**
 * sort function using the Quick Sort algorithm
 */
fun quickSort(usersArray: Array<Employee>, left: Int, right: Int) {
    val center = partition (usersArray, left, right)
    if(left < center-1) { // 2) Sorting left half
        quickSort(usersArray, left, center-1)
    }
    if(center < right) { // 3) Sorting right half
        quickSort(usersArray,center, right)
    }
}




/**
 * find the center of the array and elements which are bigger or smaller than center element
 */
private fun partition(array: Array<Employee>, lft: Int, rgt: Int): Int {
    var left = lft
    var right = rgt

    //Pivot Point
    val pivot = array[(left + right)/2].surname
    while (left <= right) {

        //Find the elements on left that should be on right
        while (array[left].surname < pivot) left++

        // Find the elements on right that should be on left
        while (array[right].surname > pivot) right--

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
}



/**
 * write the data about the Employee's array to file
 * @param employee array of Employee's objects
 */
fun writeToFile(employee: Array<Employee>) {
    println("Enter the name of the file: ")

    //write the file name from the console
    //let - checking thw input if it isn't null
    //write data in the file
    readLine()?.let { fileName ->
        File(fileName).printWriter().use { out ->
        employee.forEach { employee->
            out.println(employee.getEmployeeInfo() + "\n")
        }
    }
    }
}




fun main() {
    val employee: MutableList<Employee> = mutableListOf()

    //read data from the files and create the Employee's objects using the data from the file
//    Employee.randomEmployee(employee)

    //sort the users from file
//    Employee.sortEmployee("Users.txt")

    val initArray = arrayOf(3, 66, 8, 99, 4, 646, 76, 32, 5, 13, 78, 3, 7)
    mergeSort(initArray)
    initArray.forEach { print("$it, ") }
}

/**
 * using the Merge algorithm
 */
fun merge(leftArray: Array<Int>, rightArray:Array<Int>, resultArray:Array<Int>) {
    var iLeft=0
    var jRight=0

    //resultArray.indices - Returns the range of valid indices for the array.
    for(result in resultArray.indices) {

        //if right array is finished or if left array isn't finished and if left value less than right value
        if((jRight>=rightArray.size) || (iLeft<leftArray.size && leftArray[iLeft]<=rightArray[jRight])) {
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
fun mergeSort(initArray:Array<Int>) {
    if(initArray.size<=1)
        return

    //dividing the array on 2 parts
    val leftArray = initArray.copyOfRange(0,initArray.size/2)
    val rightArray = initArray.copyOfRange(initArray.size/2, initArray.size)

    //using recursion to divide the arrays
    mergeSort(leftArray)
    mergeSort(rightArray)

    //calling the Merge algorithm
    merge(leftArray,rightArray, initArray)
}