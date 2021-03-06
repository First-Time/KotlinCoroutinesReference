package com.lyf.coroutine.select

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlin.random.Random

//region在通道中select
/*fun CoroutineScope.fizz() = produce<String> {
    while (true) { //每300毫秒发送一个"Fizz"
        delay(300)
        send("Fizz")
    }
}

fun CoroutineScope.buzz() = produce<String> {
    while (true) { //每500毫秒发送一个"Buzz"
        delay(500)
        send("Buzz!")
    }
}

suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    select<Unit> { //<Unit>意味着 select 表达式不返回任何结果
        fizz.onReceive { value -> println("fizz -> '$value'") }
        buzz.onReceive { value -> println("buzz -> '$value'") }
    }
}*/
//endregion

//region通道关闭时select
/*suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
        select {
            a.onReceiveOrNull { value ->
                if (value == null) {
                    "Channel 'a' is closed"
                } else {
                    "a -> '$value'"
                }
            }
            b.onReceiveOrNull { value ->
                if (value == null) {
                    "Channel 'b' is closed"
                } else {
                    "b -> '$value'"
                }
            }
        }*/
//endregion

//regionSelect以发送
/*fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
    for (num in 1..10) { //生产从1到10的10个数值
        delay(100) //延迟100毫秒
        select<Unit> {
            onSend(num) {} //发送到主通道
            side.onSend(num) {} //或者发送到side通道
        }
    }
}*/
//endregion

//regionSelect延迟值
/*fun CoroutineScope.asyncString(time: Long) = async {
    delay(time)
    "Waited for $time ms"
}

fun CoroutineScope.asyncStringList(): List<Deferred<String>> {
    val random = Random(2)
    return List(12) {
        val randomTime = random.nextLong(1000, 5000)
        println("randomTime = $randomTime")
        asyncString(randomTime)
    }
}*/
//endregion

//region在延迟值通道上切换
/*fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
    var current = input.receive() //从第一个接收到的延迟值开始
    while (isActive) { //循环直到被取消或关闭
        val next = select<Deferred<String>?> { //从这个 select 中返回下一个延迟值或 null
            input.onReceiveOrNull { update: Deferred<String>? ->
                println("onReceiveOrNull $update ${System.currentTimeMillis()}")
                update //替换下一个要等待的值
            }
            current.onAwait { value ->
                send(value) //发送当前延迟生成的值
                println("onAwait $value ${System.currentTimeMillis()}")
                input.receiveOrNull() //然后使用从输入通道得到的下一个延迟值
            }
        }
        if (next == null) {
            println("Channel was closed ${System.currentTimeMillis()}")
            break //跳出循环
        } else {
            current = next
            println("else ${System.currentTimeMillis()}")
        }
    }
}

fun CoroutineScope.asyncString(str: String, time: Long) = async {
    delay(time)
    println("asyncString $time $str ${System.currentTimeMillis()}")
    str
}*/
//endregion

fun main() = runBlocking {
    //region在通道中select
    /*val fizz = fizz()
    val buzz = buzz()
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren() //取消 fizz 和 buzz 协程*/
    //endregion

    //region通道关闭时select
    /*val a = produce<String> {
        repeat(4) { send("Hello $it") }
    }

    val b = produce<String> {
        repeat(4) { send("World $it") }
    }

    repeat(8) { //打印最早的八个结果
        println(selectAorB(a, b))
    }

    coroutineContext.cancelChildren()*/
    //endregion

    //regionSelect以发送
    /*val side = Channel<Int>() //分配 side 通道
    launch {//对于 side 通道来说，这是一个很快的消费者
        side.consumeEach { println("Side channel has $it") }
    }
    produceNumbers(side).consumeEach {
        println("Consuming $it")
        delay(250) //不要着急，让我们正确消化被发送来的数字
    }
    println("Done consuming")
    coroutineContext.cancelChildren()*/
    //endregion

    //regionSelect延迟值
    /*val list = asyncStringList()
    val result = select<String> {
        list.withIndex().forEach { (index, deferred) ->
            deferred.onAwait { answer ->
                "Deferred $index produced answer $answer"
            }
        }
    }
    println(result)
    val countActive = list.count { it.isActive }
    println("$countActive coroutines are still active")*/
    //endregion

    //region在延迟值通道上切换
    /*val chan = Channel<Deferred<String>>() //测试使用的通道
    launch { //启动打印协程
        for (s in switchMapDeferreds(chan))
            println("launch $s ${System.currentTimeMillis()}") //打印每个获得的字符串
    }
    println(System.currentTimeMillis())
    chan.send(asyncString("BEGIN", 100))
    delay(200) //充足的时间来生产"BEGIN"
    println("delay 200  ${System.currentTimeMillis()}")

    chan.send(asyncString("Slow", 500))
    delay(100) //不充足的时间来生产"Slow"
    println("delay 100  ${System.currentTimeMillis()}")

    chan.send(asyncString("Replace", 101))
    delay(500) //在最后一个前给它一点时间
    println("delay 500  ${System.currentTimeMillis()}")

    chan.send(asyncString("END", 500))
    delay(1000) //给执行一段时间
    println("delay 1000  ${System.currentTimeMillis()}")

    chan.close() //关闭通道
    delay(500) //然后等待一段时间来让它结束
    println("delay 500  ${System.currentTimeMillis()}")*/
    //endregion
}