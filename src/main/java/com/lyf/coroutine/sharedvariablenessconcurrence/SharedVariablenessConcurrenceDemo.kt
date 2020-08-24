package com.lyf.coroutine.sharedvariablenessconcurrence

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.count
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

//region问题、以细粒度限制线程、以粗粒度限制线程、互斥
/*var counter = 0

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100 //启动的协程数量
    val k = 1000 //每个协程重复执行同一动作的次数
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}*/
//endregion

//region volatile无济于事
/*@Volatile
var counter = 0*/
//endregion

//region线程安全的数据结构
/*var counter = AtomicInteger()

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100 //启动的协程数量
    val k = 1000 //每个协程重复执行同一动作的次数
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}*/
//endregion

//region Actors
//计数器 Actor 的各种类型
suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100 //启动的协程数量
    val k = 1000 //每个协程重复执行同一动作的次数
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

sealed class CounterMsg
object IncCounter : CounterMsg() //递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() //携带回复的请求

//这个函数启动了一个新的计数器 actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 //actor状态
    for (msg in channel) {
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}
//endregion

fun main() = runBlocking<Unit> {
    //region问题
    /*withContext(Dispatchers.Default) {
        massiveRun { counter++ }
    }
    println("count = $counter")*/
    //endregion

    //region线程安全的数据结构
    /*withContext(Dispatchers.Default) {
        massiveRun { counter.incrementAndGet() }
    }
    println("count = $counter")*/
    //endregion

    //region以细粒度限制线程
    /*val counterContext = newSingleThreadContext("CounterContext")
    withContext(Dispatchers.Default) {
        massiveRun {
            withContext(counterContext) {
                counter++
            }
        }
    }
    println("counter = $counter")*/
    //endregion

    //region以粗粒度限制线程
    /*val counterContext = newSingleThreadContext("CounterContext")
    withContext(Dispatchers.Default) {
        withContext(counterContext) {
            massiveRun { counter++ }
        }
    }
    println("counter = $counter")*/
    //endregion

    //region互斥
    /*val mutex = Mutex()
    withContext(Dispatchers.Default) {
        massiveRun {
            mutex.withLock { counter++ }
        }
    }
    println("counter = $counter")*/
    //endregion

    //region Actors
    val counter = counterActor() //创建该 actor
    withContext(Dispatchers.Default) {
        massiveRun { counter.send(IncCounter) }
    }
    //发送一条消息以用来从一个 actor 中获取计数值
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println("counter = ${response.await()}")
    counter.close() //关闭该actor
    //endregion
}