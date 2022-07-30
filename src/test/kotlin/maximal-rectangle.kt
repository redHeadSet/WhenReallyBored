import org.junit.jupiter.api.Test
import java.util.LinkedList
import java.util.Queue
import kotlin.math.max

// https://leetcode.com/problems/maximal-rectangle/submissions/

// 답은 맞으나 Timeout
// dp 를 적용해야 할 거 같다
// 좌, 하단으로만 움직이는 걸 수정해야 할 듯 하다

class MaximalRect {
    @Test
    fun testMain() {
        val data = listOf<CharArray>(
            listOf<Char>('1','1','1','1','1','1','1','1').toCharArray(),
            listOf<Char>('1','1','1','1','1','1','1','0').toCharArray(),
            listOf<Char>('1','1','1','1','1','1','1','0').toCharArray(),
            listOf<Char>('1','1','1','1','1','0','0','0').toCharArray(),
            listOf<Char>('0','1','1','1','1','0','0','0').toCharArray(),
        ).toTypedArray()
        println("maximalRectangle : ${maximalRectangle(data)}")
    }

    fun maximalRectangle(matrix: Array<CharArray>): Int {
        var rectDataQueue:Queue<CalcRectData> = LinkedList()

        var allOnesCount = 0
        for(x in 0 until matrix[0].size){
            for(y in matrix.indices){
                if(matrix[y][x] == '1') {
                    allOnesCount++
                    rectDataQueue.add(
                        CalcRectData(
                            checkPoint = Pair(x, y)
                        )
                    )
                }
            }
        }
        if(allOnesCount == matrix.size * matrix[0].size)
            return allOnesCount

        var maxRectSize = if(rectDataQueue.isEmpty()) 0 else 1

        while(!rectDataQueue.isEmpty()){
            var eachDataWillMoveX = rectDataQueue.poll()
            var eachDataWillMoveY = eachDataWillMoveX.copy()

            if(matrix.isOkMoveRight(
                    checkPoint = Pair(eachDataWillMoveX.getXPoint(), eachDataWillMoveX.getYPoint()),
                    movedY = eachDataWillMoveX.movedY
                )){
                eachDataWillMoveX.moveX()
                maxRectSize = max(maxRectSize, eachDataWillMoveX.getSize())
                rectDataQueue.add(eachDataWillMoveX)
            }

            if(matrix.isOkMoveDown(
                    checkPoint = Pair(eachDataWillMoveY.getXPoint(), eachDataWillMoveY.getYPoint()),
                    movedX = eachDataWillMoveY.movedX
                )){
                eachDataWillMoveY.moveY()
                maxRectSize = max(maxRectSize, eachDataWillMoveY.getSize())
                rectDataQueue.add(eachDataWillMoveY)
            }
        }
        return maxRectSize
    }
}

data class CalcRectData(
    var checkPoint: Pair<Int, Int>,
    var movedX: Int = 0,
    var movedY: Int = 0,
){

    fun getSize() : Int { return (movedX+1)*(movedY+1) }
    fun moveX() {
        movedX++
        checkPoint = Pair(checkPoint.first+1, checkPoint.second)
    }
    fun moveY() {
        movedY++
        checkPoint = Pair(checkPoint.first, checkPoint.second+1)
    }
    fun getXPoint() = checkPoint.first
    fun getYPoint() = checkPoint.second
}

fun Array<CharArray>.isOkMoveRight(checkPoint:Pair<Int, Int>, movedY : Int) : Boolean {
    if(checkPoint.first+1 >= this[0].size)
        return false

    if(this[checkPoint.second][checkPoint.first+1] != '1')
        return false

    if(movedY > 0){
        for(checkY in 1 .. movedY){
            if(this[checkPoint.second-checkY][checkPoint.first+1] != '1')
                return false
        }
    }
    return true
}

fun Array<CharArray>.isOkMoveDown(checkPoint:Pair<Int, Int>, movedX : Int) : Boolean {
    if(checkPoint.second+1 >= this.size)
        return false

    if(this[checkPoint.second+1][checkPoint.first] != '1')
        return false

    if(movedX > 0){
        for(checkX in 1 .. movedX){
            if(this[checkPoint.second+1][checkPoint.first-checkX] != '1')
                return false
        }
    }
    return true
}

