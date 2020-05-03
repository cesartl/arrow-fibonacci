package org.ctl

import arrow.typeclasses.Monoid
import java.math.BigInteger

data class Matrix22(
    val x00: BigInteger,
    val x01: BigInteger,
    val x10: BigInteger,
    val x11: BigInteger
) {
    constructor(x00: Int, x01: Int, x10: Int, x11: Int) : this(
        x00.toBigInteger(),
        x01.toBigInteger(),
        x10.toBigInteger(),
        x11.toBigInteger()
    )

    companion object {
        val identity = Matrix22(BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE)
    }
}

interface Matrix22Monoid : Monoid<Matrix22> {
    override fun empty(): Matrix22 = Matrix22.identity
    override fun Matrix22.combine(b: Matrix22): Matrix22 {
        val (l00, l01, l10, l11) = this
        val (r00, r01, r10, r11) = b
        return Matrix22(
            l00 * r00 + l01 * r10,
            l00 * r01 + l01 * r11,
            l10 * r00 + l11 * r10,
            l10 * r01 + l11 * r11
        )
    }
}

fun Matrix22.Companion.monoid(): Monoid<Matrix22> = object : Matrix22Monoid {}