package org.ctl

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

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
        val results = mutableListOf<Pair<BigInteger, Long>>()
        while (i < 50000000.toBigInteger()) {
            results.add(i to time { Fibonacci.fibonacciFast(i) })
            i *= BigInteger.TWO
        }
        println("n,time")
        results.forEach { (n, time) ->
            print("$n,$time")
            println()
        }
    }

    private fun <T> time(f: () -> T): Long {
        val start = System.currentTimeMillis()
        f()
        return System.currentTimeMillis() - start
    }
}