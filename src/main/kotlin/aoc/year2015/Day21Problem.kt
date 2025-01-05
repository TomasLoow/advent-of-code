package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.allUnorderedPairs
import aoc.utils.parseAllPositiveInts
import kotlin.math.max
import kotlin.time.ExperimentalTime

data class RPGEntity(val hitpoints: Int, val damage: Int, val armor: Int)
data class RPGItem(val name: String, val cost: Int, val damage: Int, val armor: Int)

val weapons = listOf(
    RPGItem("Dagger", 8, 4, 0),
    RPGItem("Shortsword", 10, 5, 0),
    RPGItem("Warhammer", 25, 6, 0),
    RPGItem("Longsword", 40, 7, 0),
    RPGItem("Greataxe", 74, 8, 0),
)
val armors = listOf(
    RPGItem("Leather", 13, 0, 1),
    RPGItem("Chainmail", 31, 0, 2),
    RPGItem("Splintmail", 53, 0, 3),
    RPGItem("Bandedmail", 75, 0, 4),
    RPGItem("Platemail", 102, 0, 5),
)

val rings = listOf(
    RPGItem("Damage +1", 25, 1, 0),
    RPGItem("Damage +2", 50, 2, 0),
    RPGItem("Damage +3", 100, 3, 0),
    RPGItem("Defense +1", 20, 0, 1),
    RPGItem("Defense +2", 40, 0, 2),
    RPGItem("Defense +3", 80, 0, 3),
)

class Day21Problem : DailyProblem<Int>() {

    override val number = 21
    override val year = 2015
    override val name = "RPG Simulator 20XX"

    private lateinit var enemy: RPGEntity
    private lateinit var buyingOptionsSortedByCost: List<Pair<RPGEntity, Int>>

    override fun commonParts() {
        val (hp, damage, armor) = parseAllPositiveInts(getInputText())
        enemy = RPGEntity(hp, damage, armor)
        buyingOptionsSortedByCost = shopItems().sortedBy { it.second }.toList()
    }

    private fun shopItems(): Sequence<Pair<RPGEntity, Int>> {
        return sequence {
            weapons.forEach { w ->
                (armors + null).forEach { a ->
                    (rings + null + null).allUnorderedPairs().forEach { (r1, r2) ->
                        val cost = w.cost + (a?.cost ?: 0) + (r1?.cost ?: 0) + (r2?.cost ?: 0)
                        val dmg = w.damage + (a?.damage ?: 0) + (r1?.damage ?: 0) + (r2?.damage ?: 0)
                        val armor = w.armor + (a?.armor ?: 0) + (r1?.armor ?: 0) + (r2?.armor ?: 0)
                        yield(RPGEntity(100, dmg, armor) to cost)
                    }
                }
            }
        }
    }

    fun simulate(hero: RPGEntity, enemy: RPGEntity): Boolean {
        var heroHp = hero.hitpoints
        var enemyHp = enemy.hitpoints
        val heroDmg = max(hero.damage - enemy.armor, 1)
        val enemyDamage = max(enemy.damage - hero.armor, 1)
        while (true) {
            enemyHp -= heroDmg
            if (enemyHp <= 0) return true
            heroHp -= enemyDamage
            if (heroHp <= 0) return false
        }
    }

    override fun part1(): Int {
        val x = buyingOptionsSortedByCost.first { simulate(it.first, enemy) }
        return x.second
    }

    override fun part2(): Int {
        val x = buyingOptionsSortedByCost.last {  ! simulate(it.first, enemy) }
        return x.second
    }
}

val day21Problem = Day21Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day21Problem.testData = false
    day21Problem.runBoth(100)
}