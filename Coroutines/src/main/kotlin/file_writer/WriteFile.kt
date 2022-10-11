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
        "ID: $Id $name $surname    Birth Date: $birthDate  salary: $salary"

}


fun main() {
    val employee: MutableList<Employee> = mutableListOf()

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

    //write data in the file
    File("Users.txt").printWriter().use { out ->
        employee.forEach {
            out.println(it.getEmployeeInfo() + "\n")
        }
    }

}