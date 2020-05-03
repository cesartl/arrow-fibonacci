package org.ctl

import java.math.BigInteger

object Fibonacci {
    private val companionMatrix = Matrix22(0, 1, 1, 1)

    fun fibonacciFast(n: BigInteger): BigInteger {
        val m = Matrix22.monoid().run {
            combineTimes(companionMatrix, n)
        }
        return m.x01
    }

    fun fibonacciSlow(n: BigInteger): BigInteger {
        return if (n <= BigInteger.ONE) {
            n
        } else {
            fibonacciSlow(n - BigInteger.ONE) + fibonacciSlow(n - BigInteger.TWO)
        }
    }
}