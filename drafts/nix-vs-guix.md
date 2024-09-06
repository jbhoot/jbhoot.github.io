<article itemscope itemtype="https://schema.org/Article" itemid="urn:uuid:145070ee-1327-4159-a9aa-51c2e06aa2d5" class="h-entry">

## Why I have chosen the Nix ecosystem for now

- Guix is *very* slow at operations

  Binary substitutes download at not more than 300 kb/s.

  `guix pull` is also very slow.

- My priority on learning Linux administration

  Guix benefits from their rule of using Guile for everything. The resulting interface is very consistent and pleasant, with few frustrating gotchas.

  In contrast, Nix ecosystem is a hotchpotch of Nix lang and bash, organised with inconsistent and organically evolved conventions.
  However, Nix/bash is closer to my focus on learning Linux administration than Guile is.

- Purely functional language
  
  Both Nix and Guile are dynamically typed, so they are equal to me in that aspect.

  However, Nix is a pure functional programming language, while Guile is *not* one.

  Guix guidelines mandates that Guile code should be used in a purely functional manner, but its in no way a real constraint. While the Guile used in Guix is essentially an embedded pure functional DSL, and while the Guix guidelines mandates that Guile code should be pure functional, there is nothing stopping anyone using the impure parts of Guile in Guix. Andrew Tropin provided an example on Telegram:

  > Guile, unlike nixlang, is not a purely functional language in general. But where nix uses nixlang, guix only uses a purely functional subset of guile. I would say that nix is nixlang + bash, and guix is pure guile + impure guile.

  > guix only uses a purely functional subset of guile

  > Not exactly, you can easily do

  ```lisp
  (inputs 
    (if (getenv "WEATHER_IS_GOOD)
      (list glibc)
      (list muscl)))

  and it's a kinda problem, because you can't currently guarantee a hermetic evaluation of package definitions.
  ```

  In contract, Nix is purely functional and restricted *by design*. This choice fits its purpose like a hand in a glove.

## Conclusion

At this point, my career priorities trump my desire to manage my whole system declaratively, with a single language, no less a Lisp.

</article>
