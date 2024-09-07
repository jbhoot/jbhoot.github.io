#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: $0 path/to/draft path/to/site/dir"
  echo "Example: $0 drafts/xyz.md site"
  exit 1
fi

target_file=$1
site_dir=$2

published_year=$(date +%Y)
published_stamp=$(date '+%Y-%m-%d')
published_displayed=$(date '+%d %b %Y')
published_ele=$(htmlq -f drafts/cool-ways-to-get-uuid.html ".dt-published")

sed -i.bkp -E "s|$published_ele|<time class=\"dt-published \" datetime=\"$published_stamp\">$published_displayed</time>|" "$target_file"
rm "$target_file".bkp

year_dir=$site_dir/$published_year

mkdir -p "$year_dir"
mv "$target_file" "$year_dir"

echo "Published draft $target_file to $year_dir"

exit 0
