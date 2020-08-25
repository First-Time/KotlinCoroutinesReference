package com.lyf.coroutine.canceltimeout

import kotlinx.coroutines.*

fun main() = runBlocking {
    //region取消协程的执行
    /*val job = launch {
        repeat(10) {
            println("job：I'm sleeping $it ...")
            delay(500)
        }
    }
    delay(3500) //延迟一段时间
    println("main：I'm tired of waiting!")
    job.cancel() //取消该作业
    job.join() //等待作业执行结束
    println("main：Now I can quit.")*/
    //endregion

    //region取消是协作的（job一直在执行，没有挂起，不是协作的，所以不能被取消）
    /*val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { //一个执行计算的循环，只是为了占用CPU
            //每秒打印两次消息
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job：I'm sleeping ${i++} ...")
                nextPrintTime += 500
            }
        }
    }
    delay(1300) //等待一段时间
    println("main：I'm tired of waiting!")
    job.cancelAndJoin() //取消一个作业并且等待它结束
    println("main：Now I can quit.")*/
    //endregion

    //region使计算代码可取消
    /*val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job：I'm sleeping ${i++} ...")
                nextPrintTime += 500
            }
        }
    }
    delay(1300)
    println("main：I'm tired of waiting!")
    job.cancelAndJoin()
    println("main：Now I can quit.")*/
    //endregion

    //region在finally中释放资源
    /*val job = launch() {
        try {
            repeat(1000) {
                println("job：I'm sleeping ...")
                delay(500)
            }
        } finally {
            println("job：I'm running finally")
        }
    }
    delay(1300)
    println("main：I'm tired of waiting!")
    job.cancelAndJoin()
    println("main：Now I can quit.")*/
    //endregion

    //region运行不能取消的代码块
    /*val job = launch() {
        try {
            repeat(1000) {
                println("job：I'm sleeping $it ...")
                delay(500)
            }
        } finally {
            withContext(NonCancellable) {
                println("job：I'm running finally")
                delay(1000)
                println("job：And I've just delayed 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300)
    println("main：I'm tired of waiting!")
    job.cancelAndJoin()
    println("main：Now I can quit.")*/
    //endregion

    //region超时
    /*withTimeout(1300) {
        repeat(1000) {
            println("job：I'm sleeping $it ...")
            delay(500)
        }
    }*/

    /*val result = withTimeoutOrNull(1300) {
        repeat(1000) {
            println("job：I'm sleeping $it ...")
            delay(500)
        }
        "Done" //在它运行得到结果之前取消它
    }
    println("Result is $result")*/
    //endregion
}