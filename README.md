## Pending articles

- story from the trenches: write blog post about nginx frontend application/encoded binary to mjs
- ssh git multiple github accounts same machine

## Debugging soupault build

```
# In one terminal window
$ make dev > debug.log

# Now, any `print()` statements in a lua script will spit their output in debug.log.

# To keep track of log in realtime, run in another terminal window:
$ tail -n 10 -f debug.log 
```