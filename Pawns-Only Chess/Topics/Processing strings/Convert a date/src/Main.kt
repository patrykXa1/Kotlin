fun main() {
    val string = readLine()
    val list = string?.split("-")

    println("${list?.get(1)}/${list?.get(2)}/${list?.get(0)}")
}