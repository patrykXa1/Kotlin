fun main() {
   val string = readLine()
   val firstLetter = string?.first().toString()
   val lastLetter = string?.last().toString()
   val string1 = string?.substring(1, string.lastIndex)
   val string3 = lastLetter + string1 + firstLetter
   println(string3)


}