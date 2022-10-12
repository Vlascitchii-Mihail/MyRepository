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
         * read data from the file and create the Employee's objects
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
        }

        /**
         * write the data about rhe Employee to file
         */
        fun writeToFile(employee: MutableList<Employee>) {
            //write data in the file
            File("Users.txt").printWriter().use { out ->
                employee.forEach {
                    out.println(it.getEmployeeInfo() + "\n")
                }
            }
        }
    }

    fun sortEmployee(employee: MutableList<Employee>) {

    }
}


fun main() {
    val employee: MutableList<Employee> = mutableListOf()
    Employee.randomEmployee(employee)
    Employee.writeToFile(employee)
}


fun quickSort( array: Array<Int>, left: Int, right: Int) {
    val center = partition (array, left, right)
    if(left < center-1) { // 2) Sorting left half
        quickSort(array, left, center-1)
    }
    if(center < right) { // 3) Sorting right half
        quickSort(array,center, right)
    }
}

fun partition(array: Array<Int>, lft: Int, rgt: Int): Int {
    var left = lft
    var right = rgt
    val pivot = array[(left + right)/2] // 4) Pivot Point
    while (left <= right) {
        while (array[left] < pivot) left++ // 5) Find the elements on left that should be on right

        while (array[right] > pivot) right-- // 6) Find the elements on right that should be on left

        // 7) Swap elements, and move left and right indices
        if (left <= right) {
            swapArray(array, left,right)
            left++
            right--
        }
    }
    return left
}

fun swapArray(array: Array<Int>, left: Int, right: Int) {
    val temp = array[left]
    array[left] = array[right]
    array[right] = temp
}