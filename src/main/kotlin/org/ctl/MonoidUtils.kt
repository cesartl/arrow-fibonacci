package org.ctl

import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup
import java.math.BigInteger

/**
 * Exponentiation by squaring
 * https://en.wikipedia.org/wiki/Exponentiation_by_squaring
 */
fun <T> Monoid<T>.combineTimes(m: T, k: BigInteger): T {
    if (k == BigInteger.ZERO) {
        return empty()
    }
    if (k == BigInteger.ONE) {
        return m
    }
    var y = empty()
    var x = m
    var n = k
    while (n > BigInteger.ONE) {
        if (n.testBit(0)) {
            // n is odd
            y = y.combine(x)
            x = x.combine(x)
            n -= BigInteger.ONE
        } else {
            // n is even
            x = x.combine(x)
        }
        n /= BigInteger.TWO
    }
    return x.combine(y)
}

interface IntMultSemigroup : Semigroup<Int> {
    override fun Int.combine(b: Int): Int = this * b
}

interface IntMultMonoid : Monoid<Int>, IntMultSemigroup {
    override fun empty(): Int = 1
}

fun Int.Companion.multMonoid(): Monoid<Int> =
    object : IntMultMonoid {}