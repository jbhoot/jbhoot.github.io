<article itemscope itemtype="https://schema.org/Article" itemid="urn:uuid:b1319873-65a5-49e3-82aa-334e3fb7e353" class="h-entry">

<hgroup>

<h1 class="p-name">Effective Scala, part 8 - OO concepts</h1>

<p>Posted on <a class="u-url" href=""><time class="dt-published" datetime="2024-05-14">14 May 2024</time></a> in 
<a class="p-category" href="" rel="tag">Effective Scala series</a>
<a class="p-category" href="" rel="tag">OOP</a>
</p>

</hgroup>

<div class="e-content">


Flip the perspective on tools like interfaces and traits. Don't see them as a way for objects to share an interface, but as an abstraction over implementations.

So, not: DatabaseAccess -> (InMemoryDatabase, PhysicalDatabase)

but:     DatabaseAccess <- (InMemoryDatabase, PhysicalDatabase)

An interface is more usefully seen as:

- DatabaseAccess creates an abstraction barrier so we can ignore which database is used underneath

than as:

- DatabaseAccess allows various databases to share the interface.

The former encourages the view/use of interface as an abstraction over implementation.

</div>
</article>
