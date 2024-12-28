package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.allSumsOfLength
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.truncPositive
import kotlin.time.ExperimentalTime

class Day15Problem : DailyProblem<Int>() {

    override val number = 15
    override val year = 2015
    override val name = "Science for Hungry People"

    private lateinit var ingredients: List<Ingredient>
    private lateinit var amountsList: List<List<Int>>

    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    )

    override fun commonParts() {
        val re = """(.+): capacity (.*), durability (.*), flavor (.*), texture (.*), calories (.*)""".toRegex()
        ingredients = getInputText().nonEmptyLines().map { line ->
            val (name, capacity, durability, flavor, texture, calories) = re.matchEntire(line)!!.destructured
            Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
        }
        amountsList = 100.allSumsOfLength(4)

    }

    private fun score(amounts: List<Int>): Int {
        data class MixingBowl(
            var capacity: Int,
            var durability: Int,
            var flavor: Int,
            var texture: Int,
        )

        val recipe: List<Pair<Int, Ingredient>> = amounts.zip(ingredients)
        val bowl = MixingBowl(0, 0, 0, 0)
        recipe.forEach { (count, ing) ->
            bowl.capacity += count * ing.capacity
            bowl.durability += count * ing.durability
            bowl.flavor += count * ing.flavor
            bowl.texture += count * ing.texture
        }

        return bowl.capacity.truncPositive() * bowl.durability.truncPositive() * bowl.flavor.truncPositive() * bowl.texture.truncPositive()
    }

    private fun has500Calories(amounts: List<Int>): Boolean {
        return amounts.zip(ingredients).sumOf { (a, i) -> a * i.calories } == 500
    }


    override fun part1(): Int {
        return amountsList.maxOf { score(it) }
    }

    override fun part2(): Int {
        return amountsList.filter { has500Calories(it) }.maxOf { score(it) }
    }
}

val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.runBoth(100)
}
