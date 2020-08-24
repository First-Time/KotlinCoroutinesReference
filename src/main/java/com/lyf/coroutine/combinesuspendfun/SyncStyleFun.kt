package com.lyf.coroutine.combinesuspendfun

import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class SyncStyleFun {
}

//注意，在这个示例中我们在"main"函数的右边没有加上"runBlocking"
private fun main() {
    val time = measureTimeMillis {
        //我们可以在协程外面启动异步执行
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        //但是等待结果必须调用其它的挂起或者阻塞
        //当我们等待结果的时候，这里我们使用"runBlocking { ... }"来阻塞主线程
        runBlocking { println("The answer is ${one.await() + two.await()}") }
    }
    println("Completed in $time ms")
}