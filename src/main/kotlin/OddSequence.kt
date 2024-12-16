class OddSequence<T> (private val source: Sequence<T>): Sequence<T> {
    override fun iterator(): Iterator<T> = OddPositionIterator(source)
}

internal class OddPositionIterator<T>(source: Sequence<T>) : AbstractIterator<T>() {
    private var index = 1
    private val iterator = source.iterator()

    override fun computeNext() {
        while (iterator.hasNext()) {
            val next = iterator.next()

            if (tryAdd(next)) return
        }
        done()
    }

    private fun tryAdd(next: T): Boolean {
        val isEligible = index % 2 != 0
        if (isEligible) setNext(next)

        index++

        return isEligible
    }
}

fun <T> Sequence<T>.oddPositions() = OddSequence(this)

fun main() {
    (1..4).asSequence()
        .oddPositions()
        .onEach { println("filtered element: $it") }
        .toList()
}