<meta itemprop="itemid" content="urn:uuid:dea8aaf0-4066-43bd-8b01-a5b482609206">
<meta itemprop="dt-published" content="2024-10-01T07:22:39Z">
<meta itemprop="p-category" content="Philosophy of programming">


  <h1>Different kinds of simple</h1>

<div class="e-content">

This post is syndicated from [my response](https://lobste.rs/s/8ep75v/how_1_software_engineer_outperforms_138#c_kjyns6) to a Lobste.rs story titled [How 1 Software Engineer Outperforms 138 - Lichess Case Study](https://lobste.rs/s/8ep75v/how_1_software_engineer_outperforms_138).

The video author talked about _simple_ in terms of fewer lines of code, as in "fewer the features, fewer the lines of code, simpler the codebase". Recently, I chose another kind of _simple_ that increased the lines of code and duplication.

There was a segment implemented in a complex workflow, which contained enough conditionals that I could identify four sub-workflows, which merged back into one at the end of the segment.

When time came to add more conditionals to the workflow, I had a choice to keep going with a single flow, handling conditionals as and when a branch reared its head. This would keep the lines of code to a minimum. But it would also have made the code difficult to follow to someone who was introduced to it for the first time, or even to someone who revisited it after some time.

Or I could split that segment into distinct sub-workflows, at the cost of some duplication. More lines of code, but easier to follow. Later on, once I identify the duplicate (i.e., common) components in those sub-workflows, I could extract some of them to reduce the duplication.

I chose the latter approach. The segment ended up being split into _eight_ distinct sub-workflows. One clear benefit was that the number of bugs in the existing implementation reduced, because logic unique to each sub-workflow got isolated. For the same reason, the code became easy to follow. I hope this choice remains the right choice further down the line. I feel this approach would be easier to follow to a later re-visitor or to a new person.

An unaffiliated side-note: the above re-factoring became possible due to the statically typed language the code was implemented in – ReScript, which has an ML-like type system. It gave me a high degree of confidence in making a fearless re-factoring.

</div>


