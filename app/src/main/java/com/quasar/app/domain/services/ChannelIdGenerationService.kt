package com.quasar.app.domain.services

import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

interface ChannelIdGenerationService {
    fun generateId(): String
}

class ChannelIdGenerationServiceImpl : ChannelIdGenerationService {
    private val adjectives = listOf(
        "quick", "lazy", "sleepy", "noisy", "hungry", "happy", "sad", "angry", "calm", "brave",
        "bright", "clever", "clumsy", "curious", "eager", "gentle", "kind", "lively", "polite", "proud",
        "quiet", "silly", "shy", "wise", "witty", "bold", "cheerful", "fierce", "friendly", "graceful",
        "grumpy", "honest", "jolly", "joyful", "lucky", "merry", "nimble", "proud", "silly", "sneaky",
        "swift", "tall", "tiny", "tricky", "strong", "stubborn", "thoughtful", "understanding", "wild", "zany"
    )

    private val colors = listOf(
        "red", "blue", "green", "yellow", "purple", "orange", "pink", "brown", "black", "white",
        "gray", "turquoise", "teal", "magenta", "cyan", "maroon", "olive", "navy", "lavender", "gold",
        "silver", "bronze", "coral", "peach", "mint", "lilac", "beige", "ivory", "chocolate", "tan",
        "sapphire", "emerald", "ruby", "amethyst", "amber", "aqua", "burgundy", "crimson", "fuchsia", "indigo",
        "jade", "khaki", "lime", "mauve", "ochre", "periwinkle", "plum", "rose", "salmon", "tangerine"
    )

    private val animals = listOf(
        "fox", "dog", "squirrel", "bear", "lion", "tiger", "elephant", "giraffe", "zebra", "kangaroo",
        "monkey", "penguin", "dolphin", "shark", "whale", "turtle", "rabbit", "deer", "moose", "buffalo",
        "wolf", "eagle", "hawk", "owl", "bat", "frog", "snake", "crocodile", "alligator", "lizard",
        "hedgehog", "otter", "beaver", "raccoon", "badger", "skunk", "possum", "porcupine", "flamingo", "peacock",
        "parrot", "swan", "goose", "duck", "pigeon", "robin", "sparrow", "woodpecker", "hummingbird", "toucan"
    )

    override fun generateId(): String {
        val uuid = UUID.randomUUID().toString()
        val random = UUID.nameUUIDFromBytes(uuid.toByteArray()).mostSignificantBits

        val adjective = adjectives[abs(random % adjectives.size).toInt()]
        val color = colors[abs((random / adjectives.size) % colors.size).toInt()]
        val animal = animals[abs((random / (adjectives.size * colors.size)) % animals.size).toInt()]
        val randomNumber = Random.nextInt(100, 999)

        return "$adjective-$color-$animal-$randomNumber"
    }
}