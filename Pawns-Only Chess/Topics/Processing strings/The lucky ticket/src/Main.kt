fun main() {
    val numbers = readLine()
    val list = numbers?.toList()

    var suma1=0
    var suma2=0
    for( i in 0..2){
        suma1+= list!![i].toInt()
        suma2+= list[list.size-1-i].toInt()
    }

    if(suma1==suma2)
        println("Lucky")
    else
        println("Regular")

}