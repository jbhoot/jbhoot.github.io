#!/bin/bash

# fill the published tag

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: $0 path/to/draft path/to/site/dir"
  echo "Example: $0 drafts/xyz.md site"
  exit 1
fi

target_file=$1
site_dir=$2

publish_year=$(date +%Y)

year_dir=$site_dir/$publish_year

mkdir -p "$year_dir"

mv "$target_file" "$year_dir"

echo "Published draft $target_file to $year_dir"

exit 0
