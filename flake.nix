{
  description = "Flake to manage the artifacts related to my personal website";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs?rev=1daf1298306546c39b3ed0e336a63a02339d4d0a";
  };

  outputs = {self, ...} @ inputs: let
    systems = ["x86_64-darwin" "aarch64-darwin" "x86_64-linux"];
    createDevShell = system: let
      pkgs = import inputs.nixpkgs {
        inherit system;
      };
    in
      pkgs.mkShell {
        buildInputs =
          [
            pkgs.soupault
            # pkgs.opam
          ];

        shellHook = ''
          # eval $(opam env)
        '';
      };
  in {
    devShell = inputs.nixpkgs.lib.genAttrs systems createDevShell;
  };
}
