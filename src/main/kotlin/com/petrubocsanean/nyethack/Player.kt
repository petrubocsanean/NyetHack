package com.petrubocsanean.nyethack

import Fightable
import Loot
import numVowels

class Player(
    initialName: String,
    val homeTown: String = "Neversummer",
    override var healthPoints: Int,
    val isImmortal: Boolean
) : Fightable {
    override var name = initialName
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }

    constructor(name: String) : this(initialName = name, healthPoints = 100, isImmortal = false) {
        if (name.equals("Jason", ignoreCase = true)) {
            healthPoints = 500
        }
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence")
    }

    val title: String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable"
            name.none { it.isLetter() } -> "The Witness Protection Member"
            name.numVowels > 4 -> "The master of Vowels"
            else -> "The Renowned Hero"
        }

    init {
        require(healthPoints > 0) { "healthPoints must be greater than zero" }
        require(name.isNotBlank()) { "Player must have a name" }
    }

    val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophecy upon $name")
        "An interpid hero from $homeTown shall some day "+ listOf("form an unlikely bond between two warring factions", "take possession of an otherworldly blade",
        "bring the gift of creation back to the world",
        "best the world-eater").random()
    }

    val inventory = mutableListOf<Loot>()

    var gold = 0

    override val diceCount: Int = 3

    override val diceSides: Int = 4

    fun changeName(newName: String) {
        narrate("$name legally changes heir name to $newName")
        name = newName
    }

    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told madrigal, \"${prophecy}\"")
    }

    override fun takeDamage(damage: Int) {
        if (!isImmortal) {
            healthPoints -= damage
        }
    }
}