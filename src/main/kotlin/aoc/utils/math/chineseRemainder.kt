package aoc.utils.math

import aoc.utils.extensionFunctions.product
import java.math.BigInteger

/**
 * Solves the Chinese remainder theorem for a given list of modulo and remainder pairs
 * with BigInteger values.
 * The function calculates the smallest non-negative solution that satisfies all given congruences.
 *
 * @param modulusAndRems A list of pairs, where each pair consists of a modulus (first)
 * and a corresponding remainder (second). Both values are of the type BigInteger.
 *
 * @return The smallest non-negative solution as a BigInteger that satisfies all the
 * provided congruences. Ie `return-value % modulosAndRems[i].first == modulosAndRems[i].second` for all i.
 */
fun chineseRemainder(modulusAndRems: List<Pair<BigInteger, BigInteger>>): BigInteger {
    require(!modulusAndRems.isEmpty()) { "list must not be empty" }

    val prod = modulusAndRems.map { it.first }.product()
    val sum = modulusAndRems
        .fold(BigInteger.ZERO) { acc, (mod, rem) ->
            val p = prod / mod
            acc + rem * p.multInv(mod) * p
        }
    return sum % prod
}

/**
 * Solves the Chinese remainder theorem for a given list of modulo and remainder pairs
 * with Long values.
 *
 * @param modulusAndRems A list of pairs, where each pair consists of a modulus (first)
 * and a corresponding remainder (second). Both values are of type Long.
 *
 * @return The smallest non-negative solution as a Long that satisfies all the
 * provided congruences. Ie `return-value % modulosAndRems[i].first == modulosAndRems[i].second` for all i.
 */
fun chineseRemainder(modulusAndRems: List<Pair<Long, Long>>): Long {
    require(!modulusAndRems.isEmpty()) { "list must not be empty" }

    val prod = modulusAndRems.map { it.first }.reduce { acc, mod -> acc * mod }
    val sum = modulusAndRems
        .fold(0L) { acc, (mod, rem) ->
            val p = prod / mod
            acc + rem * p.multInv(mod) * p
        }
    return (sum % prod + prod) % prod
}

/*
* Solves the Chinese remainder theorem for a given list of modulo and remainder pairs
* with Int values.
*
* @param modulusAndRems A list of pairs, where each pair consists of a modulus (first)
* and a corresponding remainder (second). Both values are of type Long.
*
* @return The smallest non-negative solution as an Int that satisfies all the
* provided congruences. I.e. `return-value % modulosAndRems[i].first == modulosAndRems[i].second` for all i.
*/
fun chineseRemainder(modulusAndRems: List<Pair<Int, Int>>): Int {
    require(!modulusAndRems.isEmpty()) { "list must not be empty" }

    val prod = modulusAndRems.map { it.first }.reduce { acc, mod -> acc * mod }
    val sum = modulusAndRems
        .fold(0) { acc, (mod, rem) ->
            val p = prod / mod
            acc + rem * p.multInv(mod) * p
        }
    return (sum % prod + prod) % prod
}
