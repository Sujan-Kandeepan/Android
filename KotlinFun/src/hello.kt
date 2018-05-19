fun main(args: Array<String>) {
    print("Hello Kotlin!")

    var age: Int? = 28
    var favCandy: String = "Snickers"
    println("I am $age years old and I love $favCandy")

    var name = "Sujan"

    // Single line comment

    /*
    Multi
    Line
    Comment
     */

    var weight: Double = 188.6

    var radius = 6
    var pi = 3.14
    var c: Int = (radius * pi * 2).toInt()
    println(c)

    var wallet = 40
    wallet -= 5
    println(wallet)

    age = 19
    age += 40
    /*
    Writing software, fun fun fun
    Dealing with a mid-life crisis
    Over-thinking, crying, going to sleep
     */

    var isTheDogAlive: Boolean = true
    var someAge = 28
    if (someAge != 18 && isTheDogAlive) {

    } else {

    }

    name = "Sujan"
    if (name == "Sujan") {
        println("Sujan is awesome!")
    }

    var luckyNumbers = mutableListOf<Int>(1, 6, 78, 3) // listOf, arrayOf, mutableArrayOf, etc.
    luckyNumbers.add(2, 0)
    print(luckyNumbers[0])
    print(luckyNumbers.size)

    var favoriteMovies: List<String> = listOf<String>("Avatar", "Home Alone", "Despicable Me")

    for (x in 1..10) {
        println(x)
    }

    var favCandies = listOf("Snickers", "Kit Kat", "Mars")
    for (candy in favCandies) {
        println(candy)
    }

    for (num in 1..200) {
        if (num % 2 == 1) {
            println(num)
        }
    }

    var dogs = mutableMapOf("Fido" to 8, "Sarah" to 17, "Sean" to 6)
    println(dogs["Fido"])
    dogs["Jeremy"] = 52

    val slang = mapOf<String, String>("fam" to "friend", "cheesed" to "angry", "baked" to "dumb")

    fun hello(name: String = "Sujan", punctuation: String): String {
        return "Hello $name$punctuation"
    }
    println(hello("Sujan", "!"))

    fun addNumbers(num1: Int, num2: Int): Int = num1 + num2
    println(addNumbers(1, 2))

    fun dogSentence(name: String, age: Int) = "The dog $name is $age years old."
    println(dogSentence("Rufus", 7))

    class Dog {
        var dogName: String
        var dogAge: Int
        var dogFurColour: String

        constructor() {
            this.dogName = ""
            this.dogAge = 0
            this.dogFurColour = ""
        }

        constructor(name: String, age: Int, furColour: String) {
            this.dogName = name
            this.dogAge = age
            this.dogFurColour = furColour
        }

        fun dogInfo():String {
            return "$dogName is $dogAge years old and has $dogFurColour fur"
        }
    }

    var myDawg = Dog("Fido", 5, "brown")
    myDawg.dogName = "Rufus"
    print(myDawg.dogInfo())

    age = null
    var newAge = age!!
    if (age != null) {
        age!!
    }

    var dogMap = mapOf("Fido" to 8)
    var dogAge = dogs["abc"]

    var stringNullable: String?
    stringNullable = null
}