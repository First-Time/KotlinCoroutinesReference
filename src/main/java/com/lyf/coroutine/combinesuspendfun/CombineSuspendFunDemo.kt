package com.lyf.coroutine.combinesuspendfun

import kotlinx.coroutines.*

class CombineSuspendFunDemo {
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000) //假设我们在这里做了一些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000) //假设我们在这里做了一些有用的事
    return 29
}

//region async风格的函数
//somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
fun somethingUsefulOneAsync() = GlobalScope.async { doSomethingUsefulOne() }

//somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
fun somethingUsefulTwoAsync() = GlobalScope.async { doSomethingUsefulTwo() }
//endregion

//region使用async的结构化并发
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}
//endregion

fun main() = runBlocking<Unit> {
    //region默认顺序调用
    /*val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")*/
    //endregion

    //region使用async并发
    /*val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")*/
    //endregion

    //region惰性启动的async
    /*val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        //执行一些计算
        one.start()
        two.start()
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")*/
    //endregion

    //region使用async的结构化并发
    /*val time = measureTimeMillis { println("The answer is ${concurrentSum()}") }
    println("Completed in $time ms")*/

    try {
        failedConcurrentSum()
    } finally {
        println("Computation failed with ArithmeticException")
    }
    //endregion
}

//region使用async的结构化并发
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async {
        try {
            delay(Long.MAX_VALUE)
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Two child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

//endregion