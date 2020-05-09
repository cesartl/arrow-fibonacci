package org.ctl

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

data class Timed<T>(val timeMs: Long, val value: T)

data class Benchmark(val n: BigInteger, val timeMs: Long, val bits: Int)

internal class FibonacciTest {

    @Test
    internal fun allImplementationsAreEqual() {
        (0 until 20).forEach {
            val n = it.toBigInteger()
            assertEquals(Fibonacci.fibonacciSlow(n), Fibonacci.fibonacciFast(n), "Fibonacci $n")
        }
    }

    @Test
    internal fun fibonacciBigNumbers() {
//        println(Fibonacci.fibonacciFast(1000000.toBigInteger()).bitLength())
        var i = 100000.toBigInteger()
        val results = mutableListOf<Benchmark>()
        while (i < 2000000.toBigInteger()) {
            val (timeMs, v) = time { Fibonacci.fibonacciFast(i) }
            results.add(Benchmark(i, timeMs, v.bitLength()))
            i *= BigInteger.TWO
        }
        println("n,time,bits")
        results.forEach { (n, time, bits) ->
            print("$n,$time,$bits")
            println()
        }
    }

    private fun <T> time(f: () -> T): Timed<T> {
        val start = System.currentTimeMillis()
        val v = f()
        return Timed(System.currentTimeMillis() - start, v)
    }
}