<hgroup>

# Effective Scala, part 8 - OO concepts

<p><time datetime="2024-05-14">14 May 2024</time></p>

</hgroup>

Flip the perspective on tools like interfaces and traits. Don't see them as a way for objects to share an interface, but as an abstraction over implementations.

So, not: DatabaseAccess -> (InMemoryDatabase, PhysicalDatabase)

but:     DatabaseAccess <- (InMemoryDatabase, PhysicalDatabase)

An interface is more usefully seen as:

- DatabaseAccess creates an abstraction barrier so we can ignore which database is used underneath

than as:

- DatabaseAccess allows various databases to share the interface.

The former encourages the view/use of interface as an abstraction over implementation.
