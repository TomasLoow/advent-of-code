package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

/** A look and see sequence can be decomposed into "atoms". See
 * https://en.wikipedia.org/wiki/Look-and-say_sequence#Cosmological_decay
 *
 * This can allow us to do calculations on the sequence without actually
 * working with it, since it becomes obnoxiously huge.
 *
 * Thank you, Conway, you delightful old weirdo!
 */
private typealias Atom = String

val atomMap = mapOf(
    "H" to "22",
    "He" to "13112221133211322112211213322112",
    "Li" to "312211322212221121123222112",
    "Be" to "111312211312113221133211322112211213322112",
    "B" to "1321132122211322212221121123222112",
    "C" to "3113112211322112211213322112",
    "N" to "111312212221121123222112",
    "O" to "132112211213322112",
    "F" to "31121123222112",
    "Ne" to "111213322112",
    "Na" to "123222112",
    "Mg" to "3113322112",
    "Al" to "1113222112",
    "Si" to "1322112",
    "P" to "311311222112",
    "S" to "1113122112",
    "Cl" to "132112",
    "Ar" to "3112",
    "K" to "1112",
    "Ca" to "12",
    "Sc" to "3113112221133112",
    "Ti" to "11131221131112",
    "V" to "13211312",
    "Cr" to "31132",
    "Mn" to "111311222112",
    "Fe" to "13122112",
    "Co" to "32112",
    "Ni" to "11133112",
    "Cu" to "131112",
    "Zn" to "312",
    "Ga" to "13221133122211332",
    "Ge" to "31131122211311122113222",
    "As" to "11131221131211322113322112",
    "Se" to "13211321222113222112",
    "Br" to "3113112211322112",
    "Kr" to "11131221222112",
    "Rb" to "1321122112",
    "Sr" to "3112112",
    "Y" to "1112133",
    "Zr" to "12322211331222113112211",
    "Nb" to "1113122113322113111221131221",
    "Mo" to "13211322211312113211",
    "Tc" to "311322113212221",
    "Ru" to "132211331222113112211",
    "Rh" to "311311222113111221131221",
    "Pd" to "111312211312113211",
    "Ag" to "132113212221",
    "Cd" to "3113112211",
    "In" to "11131221",
    "Sn" to "13211",
    "Sb" to "3112221",
    "Te" to "1322113312211",
    "I" to "311311222113111221",
    "Xe" to "11131221131211",
    "Cs" to "13211321",
    "Ba" to "311311",
    "La" to "11131",
    "Ce" to "1321133112",
    "Pr" to "31131112",
    "Nd" to "111312",
    "Pm" to "132",
    "Sm" to "311332",
    "Eu" to "1113222",
    "Gd" to "13221133112",
    "Tb" to "3113112221131112",
    "Dy" to "111312211312",
    "Ho" to "1321132",
    "Er" to "311311222",
    "Tm" to "11131221133112",
    "Yb" to "1321131112",
    "Lu" to "311312",
    "Hf" to "11132",
    "Ta" to "13112221133211322112211213322113",
    "W" to "312211322212221121123222113",
    "Re" to "111312211312113221133211322112211213322113",
    "Os" to "1321132122211322212221121123222113",
    "Ir" to "3113112211322112211213322113",
    "Pt" to "111312212221121123222113",
    "Au" to "132112211213322113",
    "Hg" to "31121123222113",
    "Tl" to "111213322113",
    "Pb" to "123222113",
    "Bi" to "3113322113",
    "Po" to "1113222113",
    "At" to "1322113",
    "Rn" to "311311222113",
    "Fr" to "1113122113",
    "Ra" to "132113",
    "Ac" to "3113",
    "Th" to "1113",
    "Pa" to "13",
    "U" to "3",
)
val decayMap = mapOf(
    "H" to listOf("H"),
    "He" to listOf("Hf", "Pa", "H", "Ca", "Li"),
    "Li" to listOf("He"),
    "Be" to listOf("Ge", "Ca", "Li"),
    "B" to listOf("Be"),
    "C" to listOf("B"),
    "N" to listOf("C"),
    "O" to listOf("N"),
    "F" to listOf("O"),
    "Ne" to listOf("F"),
    "Na" to listOf("Ne"),
    "Mg" to listOf("Pm", "Na"),
    "Al" to listOf("Mg"),
    "Si" to listOf("Al"),
    "P" to listOf("Ho", "Si"),
    "S" to listOf("P"),
    "Cl" to listOf("S"),
    "Ar" to listOf("Cl"),
    "K" to listOf("Ar"),
    "Ca" to listOf("K"),
    "Sc" to listOf("Ho", "Pa", "H", "Ca", "Co"),
    "Ti" to listOf("Sc"),
    "V" to listOf("Ti"),
    "Cr" to listOf("V"),
    "Mn" to listOf("Cr", "Si"),
    "Fe" to listOf("Mn"),
    "Co" to listOf("Fe"),
    "Ni" to listOf("Zn", "Co"),
    "Cu" to listOf("Ni"),
    "Zn" to listOf("Cu"),
    "Ga" to listOf("Eu", "Ca", "Ac", "H", "Ca", "Zn"),
    "Ge" to listOf("Ho", "Ga"),
    "As" to listOf("Ge", "Na"),
    "Se" to listOf("As"),
    "Br" to listOf("Se"),
    "Kr" to listOf("Br"),
    "Rb" to listOf("Kr"),
    "Sr" to listOf("Rb"),
    "Y" to listOf("Sr", "U"),
    "Zr" to listOf("Y", "H", "Ca", "Tc"),
    "Nb" to listOf("Er", "Zr"),
    "Mo" to listOf("Nb"),
    "Tc" to listOf("Mo"),
    "Ru" to listOf("Eu", "Ca", "Tc"),
    "Rh" to listOf("Ho", "Ru"),
    "Pd" to listOf("Rh"),
    "Ag" to listOf("Pd"),
    "Cd" to listOf("Ag"),
    "In" to listOf("Cd"),
    "Sn" to listOf("In"),
    "Sb" to listOf("Pm", "Sn"),
    "Te" to listOf("Eu", "Ca", "Sb"),
    "I" to listOf("Ho", "Te"),
    "Xe" to listOf("I"),
    "Cs" to listOf("Xe"),
    "Ba" to listOf("Cs"),
    "La" to listOf("Ba"),
    "Ce" to listOf("La", "H", "Ca", "Co"),
    "Pr" to listOf("Ce"),
    "Nd" to listOf("Pr"),
    "Pm" to listOf("Nd"),
    "Sm" to listOf("Pm", "Ca", "Zn"),
    "Eu" to listOf("Sm"),
    "Gd" to listOf("Eu", "Ca", "Co"),
    "Tb" to listOf("Ho", "Gd"),
    "Dy" to listOf("Tb"),
    "Ho" to listOf("Dy"),
    "Er" to listOf("Ho", "Pm"),
    "Tm" to listOf("Er", "Ca", "Co"),
    "Yb" to listOf("Tm"),
    "Lu" to listOf("Yb"),
    "Hf" to listOf("Lu"),
    "Ta" to listOf("Hf", "Pa", "H", "Ca", "W"),
    "W" to listOf("Ta"),
    "Re" to listOf("Ge", "Ca", "W"),
    "Os" to listOf("Re"),
    "Ir" to listOf("Os"),
    "Pt" to listOf("Ir"),
    "Au" to listOf("Pt"),
    "Hg" to listOf("Au"),
    "Tl" to listOf("Hg"),
    "Pb" to listOf("Tl"),
    "Bi" to listOf("Pm", "Pb"),
    "Po" to listOf("Bi"),
    "At" to listOf("Po"),
    "Rn" to listOf("Ho", "At"),
    "Fr" to listOf("Rn"),
    "Ra" to listOf("Fr"),
    "Ac" to listOf("Ra"),
    "Th" to listOf("Ac"),
    "Pa" to listOf("Th"),
    "U" to listOf("Pa")
)

class Day10Problem() : DailyProblem<Long>() {

    override val number = 10
    override val year = 2015
    override val name = "Elves Look, Elves Say"

    private lateinit var startAtom: Atom


    /* cache for the lenDecay function to speed it up greatly */
    private val cache = emptyMutableMap<Int, MutableMap<Atom, Long>>()

    fun lenAfterDecay(atom: Atom, steps: Int): Long {
        if (atom in cache[steps]!!) return cache[steps]!![atom]!!
        if (steps == 0) {
            return atomMap[atom]!!.length.toLong()
        }
        val decayed = decayMap[atom]!!
        val size = decayed.sumOf { lenAfterDecay(it, steps - 1) }
        cache[steps]!![atom] = size
        return size
    }

    private fun resetCache() {
        cache.clear()
        (0..50).forEach { cache[it] = emptyMutableMap() }
    }

    override fun commonParts() {
        val input = getInputText().nonEmptyLines().single()
        startAtom = atomMap.filter { (_, v) -> v == input }.map { it.key }.single()
    }

    override fun part1(): Long {
        resetCache()
        return lenAfterDecay(startAtom, 40)
    }

    override fun part2(): Long {
        resetCache()
        return lenAfterDecay(startAtom, 50)
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.runBoth(100)
}