#!/usr/bin/env bash

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

echo "<meta itemprop='itemid' content='urn:uuid:$(uuidgen | tr '[:upper:]' '[:lower:]')'>
<!-- date --utc +'%Y-%m-%dT%H:%M:%SZ' -->
<meta itemprop='dt-published' content=''>
<meta itemprop='p-category' content=''>

<h1></h1>

<p class='e-summary'></p>

<div class='e-content'>
</div>" > "$draft"

# replace single quotes with the conventional double quotes used in HTML in the draft.
sed -i.bkp -E "s/'/\"/g" "$draft"
rm "$draft".bkp

echo "Draft $draft created. Happy writing!"

exit 0