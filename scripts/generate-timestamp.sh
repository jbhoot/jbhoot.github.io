#! /bin/bash

# needs GNU coreutils. macOS's default date is POSIX which doesn't support the required syntax.
stamp=$(date --utc +"%Y-%m-%dT%H:%M:%SZ")
echo "$stamp"
echo "$stamp" | pbcopy
echo "Timestamp copied to clipboard"
