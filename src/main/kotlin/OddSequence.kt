class OddSequence<T> (private val source: Sequence<T>): Sequence<T> {
    override fun iterator(): Iterator<T> = OddPositionIterator(source)
}

internal class OddPositionIterator<T>(source: Sequence<T>) : AbstractIterator<T>() {
    private var index = 0
    private val iterator = source.iterator()

    override fun computeNext() {
        while (iterator.hasNext()) {
            index++

            val next = iterator.next()

            if (isEligible()) {
                setNext(next)
                return
            }
        }
        done()
    }

    private fun isEligible() = index % 2 != 0
}

fun <T> Sequence<T>.takeOddPositions() = OddSequence(this)

fun main() {
    (1..100).asSequence()
        .takeOddPositions()
        .onEach { println("filtered element: $it") }
        .toList()
}