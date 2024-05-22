

```
# rebuild the system using a remote flake
# it'll fetch and build as the current user (jysh) and will need sudo only to deploy the system
# so jysh's ssh public key suffices to fetch the flake from github.
# no need to store server's root's ssh public key on github.
jysh@server $ nixos-rebuild switch --flake "git+ssh://git@github.com/jyssh/env#sourcehub" --use-remote-sudo
```
