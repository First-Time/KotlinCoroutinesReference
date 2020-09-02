package com.lyf.coroutine.contextdispatcher

import kotlinx.coroutines.*

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main() = runBlocking<Unit>(CoroutineName("main")) {
    //region调度器与线程
//    launch { println("main runBlocking  ：I'm working in ${Thread.currentThread().name}") } //运行在父协程的上下文中，即 runBlocking 主协程
//    launch(Dispatchers.Unconfined) { println("Unconfined：I'm working in ${Thread.currentThread().name}") } //不受限的，将工作在主线程中
//    launch(Dispatchers.Default) { println("Default：I'm working in ${Thread.currentThread().name}") } //将会获取默认调度器
//    launch(newSingleThreadContext("MyOwnThread")) { println("newSingleThreadContext：I'm working in ${Thread.currentThread().name}") } //将使它获得一个新的线程
    //endregion

    //region非受限调度器VS受限调度器
    /*launch(Dispatchers.Unconfined) { // 非受限的————将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch { // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        println(KotlinVersion.CURRENT)
    }*/
    //endregion

    //region调试协程与线程————用日志调试
    /*val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")*/
    //endregion

    //region在不同线程间跳转
    /*newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }*/
    //endregion

    //region上下文中的作业
//    println("My job is ${coroutineContext[Job]}")
    //endregion

    //region子协程
    //启动一个协程来处理某种传入请求（request）
    /*val request = launch {
        //孵化了两个作业，其中一个通过GlobalScope启动
        GlobalScope.launch {
            println("job1：I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1：I am not affected by cancellation of the request")
        }
        //另一个则承袭了父协程的上下文
        launch {
            println("job2：I am a child of the request coroutine")
            delay(1000)
            println("job2：I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() //取消请求（request）的执行
    delay(1000) //延迟一秒钟来看看发生了什么
    println("main：Who has survived request cancellation?")*/
    //endregion

    //region父协程的职责
    //启动一个协程来处理某种传入请求（request）
    /*val request = launch {
        repeat(3) { i -> //启动少量的自作业
            launch {
                delay(i * 200L) //延迟200毫秒、400毫秒、600毫秒的时间
                println("Coroutine $i is done")
            }
        }
        println("request：I'm done and I don't explicitly join my child that are still active")
    }
    request.join()
    println("Now processing of the request is complete")*/
    //endregion

    //region命名协程以用于调试
    /*log("Started main coroutine ${System.currentTimeMillis()}")
    // run two background value computations
//    val v1 = async(CoroutineName("v1coroutine"), CoroutineStart.LAZY) {
//    val v1 = async(Dispatchers.Default + CoroutineName("v1coroutine")) {
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        log("Computing v1 ${System.currentTimeMillis()}")
        252
    }
//    val v2 = async(CoroutineName("v2coroutine"), CoroutineStart.LAZY) {
//    val v2 = async(Dispatchers.Default + CoroutineName("v2coroutine")) {
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        log("Computing v2 ${System.currentTimeMillis()}")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}  ${System.currentTimeMillis()}")*/
    //endregion

    //region组合上下文中的元素
    /*launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }*/
    //endregion

    //region协程作用域
    /*val activity = Activity()
    activity.doSomething() //运行测试函数
    println("Launched coroutines")
    delay(500) //延迟半秒钟
    println("Destroying activity!")
    activity.destroy() //取消所有的协程
    delay(1000) //为了在视觉上确认它们没有工作*/
    //endregion

    //region线程局部数据
    /*val threadLocal = ThreadLocal<String?>() // declare thread-local variable
    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//    val job = launch(Dispatchers.Default) {
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")*/
    //endregion
}

//region协程作用域
class Activity {
    private val mainScope = CoroutineScope(Dispatchers.Default)

    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        //在示例中启动了 10 个协程，且每个都工作了不同的时长
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L) //延迟 200 毫秒、400毫秒、600毫秒等不同的时间
                println("Coroutine $i is done")
            }
        }
    }
}
//endregion