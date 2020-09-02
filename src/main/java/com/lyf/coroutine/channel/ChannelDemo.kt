package com.lyf.coroutine.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

//region管道
/*fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) {
        send(x++) //从流中开始从1生产无穷多个整数
    }
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}*/
//endregion

//region使用管道的素数
/*fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) {
        println("numbersFrom：x = $x ${System.currentTimeMillis()}")
        send(x++)
    } //开启了一个无限的整数流
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers) {
        println("filter：x = $x prime = $prime ${System.currentTimeMillis()}")
        if (x % prime != 0) {
            println("filter：send x = $x ${System.currentTimeMillis()}")
            send(x)
        }
    }
}*/
//endregion

//region扇出
/*fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1 //从1开始
    while (true) {
        send(x++) //产生下一个数字
        delay(100) //等待0.1秒
    }
}

fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
}*/
//endregion

//region扇入
/*suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}*/
//endregion

//region通道是公平的
/*data class Ball(var hits: Int)
suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) {
        ball.hits++
        println("$name $ball")
        delay(300) //等待一段时间
        table.send(ball) //将球发送回去
    }
}*/
//endregion

fun main() = runBlocking {
    //region通道基础
    /*val channel = Channel<Int>()
    launch {
        for (i in 1..5) channel.send(i * i)
    }
    repeat(5) { println(channel.receive()) }
    println("Done")*/
    //endregion

    //region关闭与迭代通道
    /*val channel = Channel<Int>()
    launch {
        for (i in 1..5) channel.send(i * i)
        channel.close()
    }
    for (y in channel) {
        println(y)
    }
    println("Done")*/
    //endregion

    //region构建通道生产者
    /*fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce { for (x in 1..5) send(x * x) }
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done")*/
    //endregion

    //region管道
    /*val numbers = produceNumbers() //从1开始生成整数
    val squares = square(numbers) //整数求平方
    repeat(5) { println(squares.receive()) } //输出前5个
    println("Done") //至此已结束
    coroutineContext.cancelChildren() //取消子协程*/
    //endregion

    //region使用管道的素数
    /*var cur = numbersFrom(2)
    repeat(10) {
        println("repeat：$it ${System.currentTimeMillis()}")
        val prime = cur.receive()
        println("==========prime = $prime========== ${System.currentTimeMillis()}")
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()*/
    //endregion

    //region扇出
    /*val producer = produceNumbers()
    repeat(5) { launchProcessor(it, producer) }
    delay(950)
    producer.cancel() //取消协程生产者从而将它们全部杀死*/
    //endregion

    //region扇入
    /*val channel = Channel<String>()
    launch { sendString(channel, "foo", 200) }
    launch { sendString(channel, "BAR!", 500) }
    repeat(10) {
        println(channel.receive())
    }
    coroutineContext.cancelChildren() //取消所有子协程来让主协程结束*/
    //endregion

    //region带缓冲的通道
    /*val channel = Channel<Int>(4) //启动带缓冲的通道
    val sender = launch { //启动发送者协程
        repeat(10) {
            println("Sending $it") //在每一个元素发送前打印它们
            channel.send(it) //将在缓冲区被占满时挂起
        }
    }
    //没有接收到东西...只是等待...
    delay(1000)
    sender.cancel() //取消发送者协程*/
    //endregion

    //region通道是公平的
    /*val table = Channel<Ball>() //一个共享的table（桌子）
    launch { player("ping", table) }
    launch { player("pong", table) }
    table.send(Ball(0)) //乒乓球
    delay(2000) //延迟1秒钟
    coroutineContext.cancelChildren() //游戏结束，取消它们*/
    //endregion

    //region计时器通道
    /*val tickerChannel = ticker(100, 0, mode = TickerMode.FIXED_PERIOD) //创建计时器通道
    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Initial element is available immediately: $nextElement") //没有初始化延迟

    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } //all subsequent elements have 100ms delay
    println("Next element is not ready in 50 ms: $nextElement")

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 100 ms: $nextElement")

    // Emulate large consumption delays（模拟大量消费延迟）
    println("Consumer pauses for 150ms")
    delay(150)
    // Next element is available immediately（下一个元素立即可用）
    nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Next element is available immediately after large consumer delay: $nextElement")
    // Note that the pause between `receive` calls is taken into account and next element arrives faster
    // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

    tickerChannel.cancel() // indicate that no more elements are needed（表明不再需要更多的元素）*/
    //endregion
}