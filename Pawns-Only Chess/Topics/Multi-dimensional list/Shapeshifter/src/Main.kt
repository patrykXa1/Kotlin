fun main() {
    // Do not touch code below
    val inputList: MutableList<MutableList<String>> = mutableListOf()
    val n = readLine()!!.toInt()
    for (i in 0 until n) {
        val strings = readLine()!!.split(' ').toMutableList()
        inputList.add(strings)
    }
    // write your code here
    val secondList: MutableList<MutableList<String>> = mutableListOf()
    secondList.add(inputList[inputList.lastIndex])
    secondList.add(inputList[0])
    println(secondList)


}