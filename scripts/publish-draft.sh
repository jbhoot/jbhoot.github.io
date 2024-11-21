#!/usr/bin/env bash

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: $0 path/to/draft path/to/site/dir"
  echo "Example: $0 drafts/xyz.md site"
  exit 1
fi

target_file=$1
site_dir=$2

published_year=$(date +%Y)
# timestamp in RFC-3339, UTC format
published_stamp=$(date --utc +'%Y-%m-%dT%H:%M:%SZ')
published_displayed=$(date '+%dXX %B %Y' | sed -e 's/11XX/11th/' -e 's/12XX/12th/' -e 's/13XX/13th/' -e 's/1XX/1st/' -e 's/2XX/2nd/' -e 's/3XX/3rd/' -e 's/XX/th/')
published_ele=$(htmlq -f "$target_file" ".dt-published")

sed -i.bkp -E "s|$published_ele|<time class=\"dt-published\" datetime=\"$published_stamp\">$published_displayed</time>|" "$target_file"
rm "$target_file".bkp

year_dir=$site_dir/$published_year

mkdir -p "$year_dir"
mv "$target_file" "$year_dir"

echo "Published draft $target_file to $year_dir"

exit 0
