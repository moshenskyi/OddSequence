# Creating a Custom Kotlin Sequence Operator

Many of us use sequences in our projects and work with a variety of operators, but do you know how they work under the hood? Let’s create a custom operator that filters only odd-indexed elements and explore how to implement it, following examples from the Kotlin Sequence API.

## How Sequences Work in Kotlin

Each `Sequence` in Kotlin is an interface that exposes an `Iterator`:

```kotlin
public interface Sequence<out T> {
    public operator fun iterator(): Iterator<T>
}
```

To implement a custom Sequence operator, we can create a class that implements this interface. All we need is to iterate through the elements, apply transformations, and return a resulting sequence.

## Implementation

Here’s our class:

```kotlin
class OddSequence<T>(private val source: Sequence<T>) : Sequence<T> {
    override fun iterator(): Iterator<T> = OddPositionIterator(source)
}
```

This class uses a form of the **decorator pattern** — we decorate the iteration process of the upstream sequence. The `OddSequence` class takes the upstream sequence, iterates over it, selects elements at odd positions, and passes them to its iterator.

Now, we need a specialized iterator to check whether an element’s index is odd or even. To simplify the implementation, we use `AbstractIterator`, which is a utility class that helps calculate the next element lazily when `next()` or `hasNext()` is called.

Here’s what the iterator looks like. It iterates through the upstream sequence, finds the next odd-positioned element, and adds it using `setNext()`. When no elements remain, we call `done()`.

```kotlin
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
```

At this point, you might be wondering, “How do I use this in practice?” It’s simple:

```kotlin
fun <T> Sequence<T>.takeOddPositions() = OddSequence(this)

fun main() {
    (1..100).asSequence()
        .takeOddPositions()
        .onEach { println("Filtered element: $it") }
        .toList()
}
```

You just create an extension function on `Sequence` to apply the operator and you’re ready to go.

### Conclusion

By exploring the implementation of operators you use in your code, you'll gain a deeper understanding of how they work. You'll often encounter similar patterns involving iterators, extension functions, and lazy computation. Some iterators may include predicates, transformation functions, or parameters for dropping or taking elements, but the fundamental concepts remain the same.
