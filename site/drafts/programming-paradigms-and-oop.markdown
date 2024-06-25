<hgroup>

# Programming Paradigms and OOP

<time datetime="2024-05-01">01 May 2024</time>

</hgroup>

A computer program is a set of instructions that a computer can execute.

Source code represents the textual, human comprehensible form of a computer program.

The elements of a computer program are data structures and operations applied to data structures.

A programming paradigms propose various approaches to the nature of instructions, and on defining and organising data structures and operations in the source code.

Unstructured programming

Structured programming - providing structure to the elements of a program (data and operations on data) to lend it clarity and maintainability.

imperative programming (sits at a lower level of abstraction than declarative programming, how instructions, primarily statements)
    procedural programming (organise code through procedures that use imperative paradigm)
    imperative OO
        C++, Java, C#

declarative programming (sits at a higher level of abstraction, what instructions, primarily expressions)
    functional programming (organisation through functions that strive to use declarative paradigm)
      OCaml, Haskell, SQL, Configuration languages
    reactive programming
    good OO
      Smalltalk, Scala?

## Other axes

type-safety
immutability - immutability by default

In reality, most languages are a mix of paradigms, where a paradigm may be preferred to others. I like to define them a x-first. For example, OCaml is functional-first, but still provides imperative constructs.

Where does OO lie in this heirarchy?

Personally, I think a robust type system and immutability-first or immutability-by-default are the most crucial characteristics in a language, no matter the paradigm.
