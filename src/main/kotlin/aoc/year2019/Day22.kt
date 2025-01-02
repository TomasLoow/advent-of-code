package aoc.year2019

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.odd
import aoc.utils.math.multInv
import java.math.BigInteger
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

/**
 * The key is that
 * All the types of shuffles can be represented as an affine transformation newPos ≡ (k*pos + m) (mod deckSize)
 * So instead of looking at individual cards, we combine affine transformations and only apply them as the last step.
 *
 */


/** an affine transform y = kx+m (mod modulus) */
private class Affine(val k: BigInteger, val m: BigInteger, val modulus: BigInteger) {
    fun apply(x: BigInteger) = (k * x + m + modulus) % modulus
}


/** combine(g,f)(x) = g(f(x)) **/
private fun combine(g: Affine, f: Affine): Affine {
    val modulus = f.modulus
    val newK = f.k * g.k
    val newM = f.apply(g.m)
    return Affine((newK + modulus) % modulus, (newM + modulus) % modulus, modulus)
}

private fun combine(fs: List<Affine>): Affine =
    fs.reduce { acc, affine -> combine(acc, affine) }


private fun pow(f: Affine, n: BigInteger): Affine {
    if (n == BigInteger.ONE) return f
    val g = pow(f, n.shr(1))

    if (n.odd) return combine(f, combine(g, g))
    else return combine(g, g)
}

class Day22Problem : DailyProblem<BigInteger>() {

    override val number = 22
    override val year = 2019
    override val name = "Slam Shuffle"

    private lateinit var inputLines: List<String>
    private var deckSizePt1 by Delegates.notNull<BigInteger>()

    override fun commonParts() {

        if (testData) {
            deckSizePt1 = 10.toBigInteger()
        } else {
            deckSizePt1 = 10007.toBigInteger()
        }
        inputLines = getInputText().nonEmptyLines()
    }

    private fun parseShuffles(size: BigInteger): List<Affine> {
        return inputLines.map { line ->
            if (line == "deal into new stack") Affine(-1.toBigInteger(), size - 1.toBigInteger(), size)
            else if ("increment" in line) {
                val k = line.removePrefix("deal with increment ").toBigInteger()
                Affine(k, 0.toBigInteger(), size)
            } else {
                val m = line.removePrefix("cut ").toBigInteger()
                Affine(1.toBigInteger(), -m, size)
            }
        }
    }


    override fun part1(): BigInteger {
        val shuffles = parseShuffles(deckSizePt1)
        val final = combine(shuffles)
        return final.apply(2019.toBigInteger())
    }


    override fun part2(): BigInteger {
        val deckSizePt2 = 119315717514047.toBigInteger()
        val shuffleCount = 101741582076661.toBigInteger()

        val shuffles = parseShuffles(deckSizePt2)

        val onePass = combine(shuffles)
        val final = pow(onePass, n = shuffleCount)

        /*
        Math time!
            kx + m = 2020
            kx = 2020 - m
            k.multInv(modulus)*kx = k.multInv(modulus)*(2020 - m)
            x = k.multInv(modulus)*(2020 - m)
        */
        val k = final.k
        val m = final.m
        val kInv = k.multInv(deckSizePt2)
        return kInv * (2020.toBigInteger() - m + deckSizePt2) % deckSizePt2
    }
}

val day22Problem = Day22Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day22Problem.testData = false
    day22Problem.runBoth(100)
}