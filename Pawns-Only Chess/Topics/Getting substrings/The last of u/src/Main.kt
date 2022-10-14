fun main() {
    val string = readLine()
    val string1 = string?.replaceAfterLast('u',string.substringAfterLast('u').uppercase())
    println(string1)

}