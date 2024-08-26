<article itemscope itemtype="https://schema.org/Article" itemid="urn:uuid:f528a128-7513-4539-8791-4dfa7fbcb8c5" class="h-entry">

<hgroup>

# Helix tips and tricks

<time datetime="2024-05-11">11 May 2024</time>

</hgroup>

## Avoiding the append trap

Vim's `a` means that you just intend to "append" some text after the cursor position.

Helix's `a` means *append to current selection*. The execution looks jarring to a vim user because the current selection keeps dragging along with your typed in characters.

I still don't know the utility of this feature, but I tend to avoid it as much as possible. So no `a` for me in Helix.

vim's `a` (append) -> `wi` (go to next word, then insert before it)

`0i` -> I (exists in vim too, but I didn't use it very much)

`$a` -> A (exists in vim too, but I didn't use it very much)



</article>
