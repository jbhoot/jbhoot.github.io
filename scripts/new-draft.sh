#!/bin/bash

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

echo "<article
  class='h-entry'
  itemid='urn:uuid:$(uuidgen | tr '[:upper:]' '[:lower:]')'
  itemscope
  itemtype='https://schema.org/Article'>

<hgroup>
  <h1 class='p-name'></h1>
  <p>Posted on <a class='u-url' href=''><time class='dt-published' datetime=''></time></a> in <a class='p-category' href='' rel='tag'></a></p>
</hgroup>

<p class='e-summary'></p>

<div class='e-content'>
</div>

</article>" > "$draft"

# replace single quotes with the conventional double quotes used in HTML in the draft.
sed -i.bkp -E "s/'/\"/g" "$draft"
rm "$draft".bkp

echo "Draft $draft created. Happy writing!"

exit 0