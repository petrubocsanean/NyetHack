package com.petrubocsanean.nyethack

import Goblin
import Loot
import LootBox
import Monster

open class Room(val name: String) {

    protected open val status = "Calm"

    open val lootBox: LootBox<Loot> = LootBox.random()

    open fun description() = name

    open fun enterRoom() {
        narrate("There is nothing to do here")
    }
}

open class MonsterRoom(
    name: String,
    var monster: Monster? = Goblin()
) : Room(name) {

    override fun description(): String = super.description() + " (Creature: ${monster?.description ?: "None"}"

    override fun enterRoom() {
        if(monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
    }
}