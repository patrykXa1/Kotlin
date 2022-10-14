package chess


class PawnsOnly {
    private val player1: String
    private val player2: String
    private val board = MutableList(8) { MutableList(8) { ' ' } }
    private var en_passant_white = MutableList(8){false}
    private var en_passant_black = MutableList(8){false}
    private var whiteNumber = 8
    private var blackNumber = 8

    init {
        this.board[1] = MutableList(8) { 'W' }
        this.board[6] = MutableList(8) { 'B' }
        println("Pawns-Only Chess")
        println("First Player's name:")
        this.player1 = readln()
        println("Second Player's name:")
        this.player2 = readln()
    }

    private fun printBoard() {
        for (i in board.lastIndex downTo 0) {
            println("  +---+---+---+---+---+---+---+---+")
            print("${i + 1} |")
            for (j in board[i]) {
                print(" $j |")
            }
            println()
        }
        println("  +---+---+---+---+---+---+---+---+")
        println("    a   b   c   d   e   f   g   h")
    }

    fun start() {
        printBoard()
        var move = true
        val pattern = "[a-h][1-8][a-h][1-8]".toRegex()
        while (true) {
            val currentPlayer = if (move) player1 else player2
            println("$currentPlayer's turn:")
            val turn = readln()
            when {
                turn == "exit" -> break
                pattern.matches(turn) -> {
                    if (checkTurn(turn, move)) {
                        if (move) en_passant_black = MutableList(8){false}
                        else en_passant_white = MutableList(8){false}

                        val (file1, rank1, file2, rank2) = parseTurn(turn)
                        this.board[rank1][file1] = ' '
                        this.board[rank2][file2] = if (move) 'W' else 'B'
                        this.printBoard()

                        if (winCondition(move)) {
                            break
                        }

                        if (stalemateCondition(!move)) {
                            break
                        }

                        move = !move
                    }
                }

                else -> println("Invalid Input")
            }
        }
    }

    private fun checkTurn(turn: String, move: Boolean): Boolean {
        val (file1, rank1, file2, rank2) = parseTurn(turn)

        if (move) {
            if (this.board[rank1][file1] != 'W') {
                println("No white pawn at ${turn.substring(0, 2)}")
                return false
            } else {
                return when {
                    en_passant_black[file2] && kotlin.math.abs(file2 - file1)  == 1 &&
                            rank2-rank1 == 1 -> {
                        this.board[rank2-1][file2] = ' '
                        this.blackNumber --
                        true
                    }

                    kotlin.math.abs(file1 - file2) == 1 && rank2 - rank1 == 1
                            &&  this.board[rank2][file2] == 'B' -> {
                                this.blackNumber --
                                true
                            }

                    rank2 - rank1 == 1 && this.board[rank2][file2] == ' ' &&
                    file1 == file2 -> true

                    rank2 - rank1 == 2 && rank1 == 1 && this.board[rank2][file2] == ' ' &&
                            this.board[rank2 - 1][file2] == ' ' && file1 == file2 &&
                            ((file2!=0 && this.board[3][file2-1] == 'B') || (file2!=7 && this.board[3][file2+1] == 'B'))  -> {
                                en_passant_white[file2] = true
                                true
                            }

                    rank2 - rank1 == 2 && rank1 == 1 && this.board[rank2][file2] == ' ' &&
                            this.board[rank2 - 1][file2] == ' ' && file1 == file2 -> true

                    else -> {
                        println("Invalid Input")
                        false
                    }
                }
            }
        } else {
            if (this.board[rank1][file1] != 'B') {
                println("No black pawn at ${turn.substring(0, 2)}")
                return false
            } else {
                return when {
                    en_passant_white[file2] && kotlin.math.abs(file2 - file1)  == 1 &&
                    rank1-rank2 == 1 -> {
                        this.board[rank2+1][file2] = ' '
                        this.whiteNumber --
                        true
                    }
                    kotlin.math.abs(file1 - file2) == 1 && rank1 - rank2 == 1 &&
                            this.board[rank2][file2] == 'W' -> {
                                this.whiteNumber --
                                true
                            }

                    rank1 - rank2 == 1 && this.board[rank2][file2] == ' ' && file1 == file2  -> true

                    rank1 - rank2 == 2 && rank1 == 6 &&  this.board[rank2][file2] == ' ' &&
                            this.board[rank2 + 1][file2] == ' ' && file1 == file2 &&
                            ((file2!=0 && this.board[4][file2-1] == 'W') || (file2!=7 && this.board[4][file2+1] == 'W'))  -> {
                                en_passant_black[file2] = true
                                true
                            }

                    rank1 - rank2 == 2 && rank1 == 6 && this.board[rank2][file2] == ' ' &&
                            this.board[rank2 + 1][file2] == ' ' && file1 == file2 -> true

                    else -> {
                        println("Invalid Input")
                        false
                    }
                }
            }
        }
    }

    private fun parseTurn(turn: String) = listOf(turn[0].code - 97, turn[1].toString().toInt() - 1,
        turn[2].code - 97, turn[3].toString().toInt() - 1)

    private fun winCondition(move: Boolean): Boolean {
        if (move){
            if(blackNumber == 0) {
                println("White Wins!")
                return true
            }

            for (i in 0..7){
                if (this.board[7][i] == 'W') {
                    println("White Wins!")
                    return true
                }
            }
        }
        else {
            if (whiteNumber == 0) {
                println("Black Wins!")
                return true
            }

            for (i in 0..7){
                if (this.board[0][i] == 'B') {
                    println("Black Wins!")
                    return true
                }
            }
        }
        return false
    }

    private fun stalemateCondition(move: Boolean): Boolean {
        if (move) {
            for(rank in 1..6){
                for(file in 0..7){
                    if (this.board[rank][file] == 'W'){
                        if (this.board[rank+1][file] == ' ' ||
                            (file != 0 && this.board[rank+1][file-1] == 'B') ||
                            (file != 7 && this.board[rank+1][file+1] == 'B')){
                                return false
                        }
                    }
                }
            }
        }
        else {
            for(rank in 1..6){
                for(file in 0..7){
                    if (this.board[rank][file] == 'B'){
                        if (this.board[rank-1][file] == ' ' ||
                            (file != 0 && this.board[rank-1][file-1] == 'W' ) ||
                            (file != 7 && this.board[rank-1][file+1] == 'W')){
                            return false
                        }
                    }
                }
            }
        }
        println("Stalemate!")
        return true
    }

}

fun main() {
    val game = PawnsOnly()
    game.start()
    println("Bye!")
}