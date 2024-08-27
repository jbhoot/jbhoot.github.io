<article itemscope itemtype="https://schema.org/Article" itemid="urn:uuid:aa8b98ca-36f9-4a95-9b18-2b8a75335e17" class="h-entry">

<hgroup>

# Memorising branch order in git rebase

<p> Published by <a class="p-author h-card" href="https://bhoot.dev/about">Jayesh Bhoot</a> on <time class="dt-published" datetime="2024-03-05">05 Mar 2024</time> </p>

<p class="tags"><span>git</span></p>

</hgroup>

In a `git rebase x` command, is `x` being rebased on the current branch, or vice versa?

I mentally always read <i>rebase</i> as <i>rebase on top of</i>.

So, `git rebase master` reads as <strong><i>git, rebase on top of master</i></strong>, i.e., rebase the current branch on top of master.

But what about the alternate version of the command: `git rebase master feat/x`?

Well, git always rebases the *current* branch.

So `git rebase master feat/x` is actually `git checkout feat/x && git rebase master`. So the reading <i>git rebase on top of master</i> still applies.

A bit clunky, but good enough for me.

</article>