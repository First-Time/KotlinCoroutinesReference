package com.lyf.coroutine.asynchronousflow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

//region挂起函数
/*suspend fun simple(): List<Int> {
    delay(1000) //假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}*/
//endregion

//region流
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000) //假设我们在这里做了一些有用的事情
        emit(i) //发送下一个值
    }
}*/
//endregion

//region流是冷的
/*fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(1000) //假设我们在这里做了一些有用的事情
        emit(i) //发送下一个值
    }
}*/
//endregion

//region流取消基础
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000) //假设我们在这里做了一些有用的事情
        println("Emitting $i")
        emit(i) //发送下一个值
    }
}*/
//endregion

//region过度流操作符
/*suspend fun performRequest(request: Int): String {
    delay(1000) //模仿长时间运行的异步工作
    return "response $request"
}*/
//endregion

//region限长操作符
/*fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}*/
//endregion

//region流上下文
/*fun simple(): Flow<Int> = flow {
    log("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}*/

//region withContext发出错误
/*fun simple(): Flow<Int> = flow {
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(1000) //假装我们以消耗CPU的方式进行计算
            emit(i) //发射下一个值
        }
    }
}*/
//endregion

//region flowOn操作符
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(1000) //假装我们以消耗CPU的方式进行计算
        log("Emitting $i")
        emit(i) //发射下一个值
    }
}.flowOn(Dispatchers.Default) //在流构建器中改变消耗CPU代码上下文的正确方式*/
//endregion

//endregion

//region缓冲
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000) //假装我们异步等待了100毫秒
        emit(i) //发射下一个值
    }
}*/
//endregion

//region收集器try与catch
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) //发射下一个值
    }
}*/
//endregion

//region一切都已捕获
/*fun simple(): Flow<String> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) //发射下一个值
    }
}.map { value ->
    check(value <= 1) { "Crashed on $value" }
    "string $value"
}*/
//endregion

//region异常透明性
/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) //发射下一个值
    }
}*/
//endregion

//region流完成

//region命令式finally块
//fun simple(): Flow<Int> = (1..3).asFlow()
//endregion

//region声明式处理
/*fun simple(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}*/
//endregion

//region成功完成
//fun simple(): Flow<Int> = (1..3).asFlow()
//endregion

//endregion

//region启动流
//fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(1000) } //模仿事件流
//endregion

//region流取消检测
fun foo(): Flow<Int> = flow {
    for (i in 1..5) {
        println("Emitting $i")
        emit(i)
    }
}
//endregion

fun main() = runBlocking<Unit> {
    //region表示多个值
    /*fun simple(): List<Int> = listOf(1, 2, 3)
    simple().forEach { value -> println(value) }*/
    //endregion

    //region序列
    /*fun simple(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(100) //假装我们在计算
            yield(i) //产生下一个值
        }
    }
    simple().forEach { value -> println(value) }*/
    //endregion

    //region挂起函数
//    simple().forEach { value -> println(value) }
    //endregion

    //region流
    /*launch {
        //启动并发的协程以验证主线程并未阻塞
        for (i in 1..3) {
            println("I'm not blocked $i")
            delay(1000)
        }
    }
    simple().collect { value -> println(value) }*/
    //endregion

    //region流是冷的
    /*println("Calling simple function...")
    val flow = simple()
    println("Calling collect...")
    flow.collect { value -> println(value) }
    println("Calling collect again...")
    flow.collect { value -> println(value) }*/
    //endregion

    //region流取消基础
    /*withTimeoutOrNull(2500) { simple().collect { value -> println(value) } }
    println("Done")*/
    //endregion

    //region流构建器
//    (1..3).asFlow().collect { value -> println(value) }
    //endregion

    //region过度流操作符
    /*(1..3).asFlow() //一个请求流
            .map { request -> performRequest(request) }
            .collect { response -> println(response) }*/

    //region转换操作符
    /*(1..3).asFlow() //一个请求流
            .transform<Int, String> { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            .collect { response -> println(response) }*/
    //endregion

    //region限长操作符
//    numbers().take(2).collect { value -> println(value) }
    //endregion

    //endregion

    //region末端流操作符
    /*val sum = (1..5).asFlow().map { it * it }.reduce { a, b -> a + b }
    println(sum)*/
    //endregion

    //region流是连续的
    /*(1..5).asFlow().filter {
        println("Filter $it")
        it % 2 == 0
    }.map {
        println("Map $it")
        "string $it"
    }.collect { println("Collect $it") }*/
    //endregion

    //region流上下文
//    simple().collect { value -> log("Collected $value") }

    //region withContext发出错误
//    simple().collect { value -> println(value) }
    //endregion

    //region flowOn操作符
//    simple().collect { value -> log("Collected $value") }
    //endregion

    //endregion

    //region缓冲
    /*val time = measureTimeMillis {
        simple()
                .buffer()
                .collect { value ->
                    delay(3000) //假装我们花了300毫秒来处理它
                    println(value)
                }
    }
    println("Collected in $time ms")*/

    //region合并
    /*val time = measureTimeMillis {
        simple()
                .conflate()
                .collect { value ->
                    delay(3000) //假装我们花了300毫秒来处理它
                    println(value)
                }
    }
    println("Collected in $time ms")*/
    //endregion

    //region处理最新值
    /*val time = measureTimeMillis {
        simple()
                .collectLatest { value -> //取消并重新发射最后一个值
                    println("Collecting $value")
                    delay(3000) //假装我们花了300毫秒来处理它
                    println("Done $value")
                }
    }
    println("Collected in $time ms")*/
    //endregion

    //endregion

    //region Zip
    /*val nums = (1..3).asFlow() //数字 1..3
    val strs = flowOf("one", "two", "three") //字符串
    nums.zip(strs) { a, b -> "$a -> $b" } //组合单个字符串
            .collect { println(it) } //收集并打印*/
    //endregion

    //region Combine
    /*val nums = (1..3).asFlow().onEach { delay(300) } //发射数字1..3 间隔300毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } //每400毫秒发射一次字符串
    val startTime = System.currentTimeMillis() //记录开始的时间
    nums.zip(strs) { a, b -> "$a -> $b" } //使用zip组合单个字符串
            .collect { value -> //收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    nums.combine(strs) { a, b -> "$a -> $b" } //使用combine组合单个字符串
            .collect { value -> //收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
    //endregion

    //region展平流

    /*fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i：First")
        delay(500) //等待500毫秒
        emit("$i：Second")
    }*/

//    (1..3).asFlow().map { requestFlow(it) }

    //region flatMapConcat
    /*val startTime = System.currentTimeMillis() //记录开始时间
    (1..3).asFlow().onEach { delay(100) } //每100毫秒发射一个数字
            .flatMapConcat { requestFlow(it) }
            .collect { value -> //收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
    //endregion

    //region flatMapMerge
    /*val startTime = System.currentTimeMillis() //记录开始时间
    (1..3).asFlow().onEach { delay(100) } //每100毫秒发射一个数字
            .flatMapMerge { requestFlow(it) }
            .collect { value -> //收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
    //endregion

    //region flatMapLatest
    /*val startTime = System.currentTimeMillis() //记录开始时间
    (1..30).asFlow().onEach { delay(100) } //每100毫秒发射一个数字
            .flatMapLatest { requestFlow(it) }
            .collect { value -> //收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
    //endregion

    //endregion

    //region异常流

    //region收集器try与catch
    /*try {
        simple().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Exception) {
        println("Caught $e")
    }*/
    //endregion

    //region一切都已捕获
    /*try {
        simple().collect { value -> println(value) }
    } catch (e: Exception) {
        println("Caught $e")
    }*/
    //endregion

    //endregion

    //region异常透明性
    /*simple()
            .catch { e -> emit("Caught $e") } //发射一个异常
            .collect { value -> println(value) }*/

    //region透明捕获
    /*simple().catch { e -> println("Caught $e") }
            .collect { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }*/
    //endregion

    //region声明式捕获
    /*simple()
            .onEach { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }
            .catch { e -> println("Caught $e") }
            .collect()*/
    //endregion

    //endregion

    //region流完成

    //region命令式finally块
    /*try {
        simple().collect { value -> println(value) }
    } finally {
        println("Done")
    }*/
    //endregion

    //region声明式处理
    /*simple()
            .onCompletion { println("Done") }
            .collect { value -> println(value) }*/

    /*simple().onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
            .catch { cause -> println("Caught exception $cause") }
            .collect { value -> println(value) }*/
    //endregion

    //region成功完成
    /*simple().onCompletion { cause -> println("Flow completed with $cause") }
            .collect { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }*/
    //endregion

    //endregion

    //region启动流
    /*events()
            .onEach { event -> println("Event $event") }
//            .collect() //等待留收集
            .launchIn(this) //在单独的协程中执行流
    println("Done")*/
    //endregion

    //region流取消检测
    /*foo().collect { value ->
        if (value == 3) cancel()
        println(value)
    }*/

    /*(1..5).asFlow().collect { value ->
        if (value == 3) cancel()
        println(value)
    }*/

    //region让繁忙的流可取消
    /*(1..5).asFlow().cancellable().collect { value ->
        if (value == 3) cancel()
        println(value)
    }*/
    //endregion

    //endregion
}