#!/bin/bash

# Cool ways to generate uuid:
# 1. Linux: cat /proc/sys/kernel/random/uuid
# 2. Linux and macOS: uuidgen

if [ -z "$1" ]
then
  echo "Usage: $0 /path/to/draft.[md|html]"
  echo "Example: $0 site/drafts/xyz.md"
  echo "Example: $0 site/drafts/abc.html"
  exit 1
fi

draft=$1

if [ -e "$draft" ]
then
  echo "Error: $draft already exists. Please use another name."
  exit 1
fi

echo "<article class='h-entry' itemscope itemtype='https://schema.org/Article' itemid='urn:uuid:$(uuidgen | tr '[:upper:]' '[:lower:]')'>
<hgroup>
<h1 class='p-name'></h1>

<p>Written by <span class='author-photo-placeholder'></span> <a class='p-author h-card' href='https://bhoot.dev/about'>Jayesh Bhoot</a> • Published on <a class='u-url' href=''><time class='dt-published' datetime=''></time></a></p>

<p class='tags'>
<a class='p-category' href='' rel='tag'></a>
</p>
</hgroup>

<p class='e-summary'></p>

<div class='e-content'>

</div>
</article>" > "$draft"

# replace single quotes with the conventional double quotes used in HTML in the draft.
sed -i -E "s/'/\"/g" "$draft"

echo "Draft $draft created. Happy writing!"

exit 0