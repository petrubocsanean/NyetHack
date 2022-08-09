package com.petrubocsanean.nyethack

import Key
import LootBox
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val menuData = File("data/tavern-menu-data").readText().split("\n").map { it.split(",") }

private val menuItems = menuData.map { (_, name, _) -> name}

private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }

private  val menuItemsTypes = menuData.associate { (type, name, _) -> name to type }

class Tavern : Room(TAVERN_NAME)
{
    val patrons: MutableSet<String> = firstNames.shuffled().zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName"}.toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        player.name to 4.50,
        *patrons.map { it to 6.0 }.toTypedArray()
    )

    val itemOfTheDay = patrons.flatMap { getFavoriteMenuItems(it) }.random()

    override val status = "Busy"

    override val lootBox: LootBox<Key> = LootBox(Key("key to Nogartse's evil lair"))

    override fun enterRoom() {
        narrate("${player.name} enters $TAVERN_NAME")
        narrate("There are several items for sale:")
        narrate(menuItems.joinToString())
        narrate("The item of the day is the $itemOfTheDay")

        narrate("${player.name} sees several patrons in the tavern:")
        narrate(patrons.joinToString())

        placeOrder(patrons.random(), menuItems.random())

    }

    private fun placeOrder(patronName: String, menuItemName: String) {
        val itemPrice = menuItemPrices.getValue(menuItemName)

        narrate("$patronName speaks with $TAVERN_MASTER to place an order")
        if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
            val action = when (menuItemsTypes[menuItemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "serves"
                else -> "hands"
            }
            narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
            narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
            patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
            patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice

        } else {
            narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
        }
    }
}

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsowrth", "Baggins", "Downstrider")


private fun getFavoriteMenuItems(patron: String): List<String> {
    return when(patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemsTypes[menuItem]?.contains("desert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(1..2))
    }
}


private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach {
        (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}