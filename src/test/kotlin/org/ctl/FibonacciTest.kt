package org.ctl

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

data class Timed<T>(val timeMs: Long, val value: T)

data class FibonacciImpl(val name: String, val f: (BigInteger) -> BigInteger)

data class Benchmark(val n: BigInteger, val timeMs: Long, val bits: Int, val implName: String)

fun FibonacciImpl.benchmark(n: BigInteger): Benchmark {
    val (timeMs, v) = time { f(n) }
    return Benchmark(n, timeMs, v.bitLength(), name)
}

internal class FibonacciTest {

    @Test
    internal fun allImplementationsAreEqual() {
        (0 until 20).forEach {
            val n = it.toBigInteger()
            val base = Fibonacci.fibonacciSlow(n)
            assertEquals(base, Fibonacci.fibonacciFast(n), "Fibonacci $n")
            assertEquals(base, Fibonacci.fibobacciMemo(n), "Fibonacci $n")
        }
    }

    @Test
    internal fun matrix() {
        (0 until 20).forEach {
            val n = it.toBigInteger()
            println(Fibonacci.fibonacciMatrix(n))
        }
    }

    @Test
    internal fun memo() {
        (0 until 20).forEach {
            val n = it.toBigInteger()
            println(Fibonacci.fibobacciMemo(n))
        }
    }

    @Test
    internal fun fibonacciBigNumbers() {
        val start = 500000.toBigInteger()
        val end = 50000000.toBigInteger()
        val fibo = FibonacciImpl("log") { Fibonacci.fibonacciFast(it) }
        val results2 = benchmark(start, end, fibo)
//        val results1 = benchmark(start, end) { Fibonacci.fibobacciMemo(it) }
//        println("Memo")
//        pringResults(results1)
        println()
        println("Monoid")
        pringResults(results2)
    }

    @Test
    internal fun comparison() {
        val fibos = listOf(
//            FibonacciImpl("recursive") { Fibonacci.fibonacciSlow(it) },
            FibonacciImpl("linear") { Fibonacci.fibobacciMemo(it) },
            FibonacciImpl("log") { Fibonacci.fibonacciFast(it) }
        )
        val start = 40000.toBigInteger()
        val end = 800000.toBigInteger()
        val rounds = 3
        val warmup = 2
        val results = mutableMapOf<BigInteger, MutableMap<String, MutableList<Long>>>()

        (0 until rounds + warmup).forEach { _ ->
            fibos.forEach { fibo ->
                val r = benchmark(start, end, fibo)
                r.forEach { (n, timeMs) ->
                    results.computeIfAbsent(n) { mutableMapOf() }.computeIfAbsent(fibo.name) { mutableListOf() }
                        .add(timeMs)
                }
            }
        }

        println("n," + fibos.joinToString(separator = ",") { it.name })
        results.entries.sortedBy { it.key }.forEach { (n, byFibo) ->
            val s = fibos
                .map { it.name }
                .map { byFibo[it] ?: mutableListOf() }
                .map { it.drop(warmup).average().toLong() }
                .joinToString(separator = ",")
            println("$n,$s")
        }
    }

    @Test
    internal fun multipleBenchmark() {
        val rounds = 5
        val start = 100000.toBigInteger()
        val end = 200000000.toBigInteger()
        val warmup = 2
        val fibo = FibonacciImpl("log") { Fibonacci.fibonacciFast(it) }

        val results = mutableMapOf<BigInteger, MutableList<Long>>()
        (0 until rounds + warmup).forEach {
            val r = benchmark(start, end, fibo)
            r.forEach { (n, timeMs) ->
                results.computeIfAbsent(n) { mutableListOf() }.add(timeMs)
            }
        }
        results.entries.sortedBy { it.key }.forEach { (n, times) ->
            println("$n,${times.drop(warmup).joinToString(separator = ",")}")
        }

    }
}

private fun pringResults(results: MutableList<Benchmark>) {
    println("n,time,bits")
    results.forEach { (n, time, bits) ->
        print("$n,$time,$bits")
        println()
    }
}

private fun benchmark(
    start: BigInteger,
    end: BigInteger,
    fibo: FibonacciImpl
): MutableList<Benchmark> {
    var i1 = start
    val results = mutableListOf<Benchmark>()
    while (i1 < end) {
        results.add(fibo.benchmark(i1))
        i1 *= BigInteger.TWO
    }
    return results
}

private fun <T> time(f: () -> T): Timed<T> {
    val start = System.currentTimeMillis()
    val v = f()
    return Timed(System.currentTimeMillis() - start, v)
}
