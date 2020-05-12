package org.ctl

import java.math.BigInteger

object Fibonacci {
    private val companionMatrix = Matrix22(0, 1, 1, 1)

    fun fibonacciMatrix(n: BigInteger): Matrix22 {
        return Matrix22.monoid().run {
            combineTimes(companionMatrix, n)
        }
    }

    fun fibonacciFast(n: BigInteger): BigInteger {
        return fibonacciMatrix(n).x01
    }

    fun fibobacciMemo(n: BigInteger): BigInteger {
        var nMinus2 = BigInteger.ZERO
        var nMinus1 = BigInteger.ONE
        if (n <= BigInteger.ONE) {
            return n
        }
        var i = BigInteger.ZERO
        while (i < n) {
            i += BigInteger.ONE
            nMinus1 += nMinus2
            nMinus2 = nMinus1 - nMinus2
        }
        return nMinus2
    }

    fun fibonacciSlow(n: BigInteger): BigInteger {
        return if (n <= BigInteger.ONE) {
            n
        } else {
            fibonacciSlow(n - BigInteger.ONE) + fibonacciSlow(n - BigInteger.TWO)
        }
    }
}