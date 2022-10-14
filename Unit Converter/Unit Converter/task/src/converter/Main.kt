package converter

import java.io.IOException

enum class Type {LENGTH, MASS, TEMPERATURE, NONE}

enum class Unit(val shortName: String, val nameSingular: String, val namePlural: String, val converter: Double, val type: Type) {
    METER("m", "meter", "meters", 1.0, Type.LENGTH),
    KILOMETER("km", "kilometer", "kilometers", 1000.0, Type.LENGTH),
    CENTIMETER("cm", "centimeter", "centimeters", 0.01, Type.LENGTH),
    MILLIMETER("mm", "millimeter", "millimeters", 0.001, Type.LENGTH),
    MILE("mi", "mile", "miles", 1609.35, Type.LENGTH),
    YARD("yd", "yard", "yards", 0.9144, Type.LENGTH),
    FOOT("ft", "foot", "feet", 0.3048, Type.LENGTH),
    INCH("in", "inch", "inches", 0.0254, Type.LENGTH),
    GRAM("g","gram","grams",1.0, Type.MASS),
    KILOGRAM("kg","kilogram","kilograms",1000.0, Type.MASS),
    MILLIGRAM("mg","milligram","milligrams",0.001, Type.MASS),
    POUNDS("lb","pound","pounds",453.592, Type.MASS),
    OUNCES("oz","ounce","ounces",28.3495, Type.MASS),
    CELSIUS("celsius","degree Celsius", "degrees Celsius", 0.0, Type.TEMPERATURE),
    FAHRENHEIT("fahrenheit", "degree Fahrenheit", "degrees Fahrenheit", 0.0, Type.TEMPERATURE),
    KELVIN("k","kelvin","kelvins",0.0,Type.TEMPERATURE),
    NULL("", "", "", 0.0, Type.NONE);
}

fun findByName(name: String): Unit {
    val unitName = name.lowercase()

    for (item in Unit.values()) {
        if (item.shortName == unitName ||
            item.nameSingular.lowercase() == unitName ||
            item.namePlural.lowercase() == unitName
        ) return item
    }

    if(name.lowercase() == "dc" || name.lowercase() == "c") return Unit.CELSIUS
    else if(name.lowercase() == "df" || name.lowercase() == "f") return Unit.FAHRENHEIT

    return Unit.NULL
}


fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val inputLine = readln()
        if (inputLine == "exit") break

        val list = inputLine.split(' ').toMutableList()

        val inputUnit1:String
        val inputUnit2:String
        var value1:Double

        try{
            value1 = list[0].toDouble()

            if (list[1].lowercase() == "degrees" || list[1].lowercase() == "degree") {
                inputUnit1 = "${list[1]} ${list[2]}"
            }
            else{
                inputUnit1 = list[1]
            }

            if (list[list.lastIndex-1].lowercase() == "degrees" || list[list.lastIndex-1].lowercase() == "degree") {
                inputUnit2 = "${list[list.lastIndex-1]} ${list[list.lastIndex]}"
            }
            else {
                inputUnit2 = list[list.lastIndex]
            }
        }
        catch(e: Exception) {
            println("Parse error")
            continue
        }

        var value2 = 0.0

        val unit1 = findByName(inputUnit1)
        val unit2 = findByName(inputUnit2)

        var answerUnit1 = ""
        var answerUnit2 = ""
        var message: String

        var validInput = true

        when {
            unit1 == Unit.NULL && unit2 == Unit.NULL -> {
                validInput = false
                answerUnit1 = "???"
                answerUnit2 = "???"
            }
            unit1 == Unit.NULL -> {
                validInput = false
                answerUnit1 = "???"
                answerUnit2 = unit2.namePlural
            }
            unit2 == Unit.NULL -> {
                validInput = false
                answerUnit1 = unit1.namePlural
                answerUnit2 = "???"
            }

            unit1.type != unit2.type  -> {
                validInput = false
                answerUnit1 = unit1.namePlural
                answerUnit2 = unit2.namePlural
            }

            unit1 == unit2 -> value2 = value1

            unit1.type == Type.LENGTH && value1 < 0 -> validInput = false
            unit1.type == Type.MASS && value1 < 0 -> validInput = false

            unit1.type == Type.LENGTH && unit2 == Unit.METER -> value2 = value1 * unit1.converter // from unit of length to meters
            unit1 == Unit.METER && unit2.type == Type.LENGTH -> value2 = value1 * 1/unit2.converter // from meters to unit of length
            unit1.type == Type.LENGTH && unit2.type == Type.LENGTH -> {
                val transitionValue = value1 * unit1.converter
                value2 = transitionValue * 1/unit2.converter
            } // from unit of length to unit of length

            unit1.type == Type.MASS && unit2 == Unit.GRAM -> value2 = value1 * unit1.converter // from unit of mass to grams
            unit1 == Unit.GRAM && unit2.type == Type.MASS -> value2 = value1 * 1/unit2.converter // from grams to unit of mass
            unit1.type == Type.MASS && unit2.type == Type.MASS -> {
                val transitionValue = value1 * unit1.converter
                value2 = transitionValue * 1/unit2.converter
            } // from unit of mass to unit of mass

            unit1 == Unit.CELSIUS && unit2 == Unit.FAHRENHEIT -> value2 = value1 * 9/5 + 32
            unit1 == Unit.FAHRENHEIT && unit2 == Unit.CELSIUS -> value2 = (value1 - 32) * 5/9
            unit1 == Unit.CELSIUS && unit2 == Unit.KELVIN -> value2 = value1 + 273.15
            unit1 == Unit.KELVIN && unit2 == Unit.CELSIUS -> value2 = value1 - 273.15
            unit1 == Unit.FAHRENHEIT && unit2 == Unit.KELVIN -> value2 = (value1 + 459.67) * 5/9
            unit1 == Unit.KELVIN && unit2 == Unit.FAHRENHEIT -> value2 = value1 * 9/5 - 459.67

        }

        if (validInput){
            answerUnit1 = if (value1 == 1.0) unit1.nameSingular else unit1.namePlural
            answerUnit2 = if (value2 == 1.0) unit2.nameSingular else unit2.namePlural

            message = "$value1 $answerUnit1 is $value2 $answerUnit2"
        }
        else if (unit1.type == Type.LENGTH && value1 < 0){
            message = "Length shouldn't be negative"
        }
        else if (unit1.type == Type.MASS && value1 < 0) {
            message = "Weight shouldn't be negative"
        } else{
            message = "Conversion from $answerUnit1 to $answerUnit2 is impossible"
        }

        println(message)
    }

}

