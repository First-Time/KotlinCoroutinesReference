package com.lyf.coroutine.basics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutinesBasicsDemo {
}

fun main() = runBlocking {
    //region第一个协程程序
    /*GlobalScope.launch { //在后台启动一个新的协程并继续
        delay(1000) //非阻塞的等待一秒钟（默认时间单位是毫秒）
        println("World") //在延迟后打印输出
    }
    println("Hello,") //协程已在等待时，主线程还在继续
    Thread.sleep(2000) //阻塞主线程两秒钟来保证 JVM 存活*/
    //endregion

    //region桥接阻塞与非阻塞的世界
    /*GlobalScope.launch {
        delay(1000)
        println("World")
    }
    println("Hello,")
    runBlocking { Thread.sleep(2000) }*/
    //endregion

    //region等待一个作业
    /*val job = GlobalScope.launch {
        delay(1000)
        println("World")
    }
    println("Hello,")
    job.join()*/
    //endregion

    //region结构化的并发
    /*launch {
        delay(1000)
        println("World")
    }
    println("Hello,")*/
    //endregion

    //region作用域构建器
    /*launch {
        delay(200)
        println("Task from runBlocking")
    }
    coroutineScope { //创建一个协程作用域
        launch {
            delay(500)
            println("Task from nested launch")
        }
        delay(100)
        println("Task from coroutineScope") //这一行会在内嵌 launch 之前输出
    }
    println("Coroutine scope is over") //这一行在内嵌 launch 执行完毕后才输出*/
    //endregion

    //region提取函数重构
    /*launch {
        doWorld()
    }
    println("Hello,")*/
    //endregion

    //region协程很轻量
    /*repeat(100000) {
        launch {
            delay(5000)
            println(".")
        }
    }*/
    //endregion

    //region全局协程像守护线程
    GlobalScope.launch {
        repeat(1000) {
            println("I'm sleeping $it ......")
            delay(500)
        }
    }
    delay(1300)
    //endregion
}

//region提取函数重构
private suspend fun doWorld() {
    delay(1000)
    println("World")
}
//endregion