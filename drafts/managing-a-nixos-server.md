<article itemscope itemtype="https://schema.org/Article" itemid="urn:uuid:eb4df946-3894-4d3d-b1e8-aa4a89cd2e30" class="h-entry">



```
# rebuild the system using a remote flake
# it'll fetch and build as the current user (jysh) and will need sudo only to deploy the system
# so jysh's ssh public key suffices to fetch the flake from github.
# no need to store server's root's ssh public key on github.
jysh@server $ nixos-rebuild switch \
  --flake "git+ssh://git@github.com/jyssh/env#sourcehub" \
  --use-remote-sudo \
  --refresh # fetch the latest tarball instead of a cached tarball
```

The following command to issue a rebuild right from my local machine does not work because my local machine's architecture (x86_64-linux) differs from the remote server's architecture (aarm64-linux).

```
nixos-rebuild switch \
  --flake .#some-configuration \
  --target-host root@target.host \
  --build-host localhost
```

</article>
