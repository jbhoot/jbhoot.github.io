{
  description = "Flake to manage the artifacts related to my personal website";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-24.05-darwin";
  };

  outputs =
    { self, ... }@inputs:
    let
      systems = [
        "aarch64-darwin"
        "x86_64-linux"
      ];
      createDevShell =
        system:
        let
          pkgs = import inputs.nixpkgs { inherit system; };
        in
        pkgs.mkShell { buildInputs = [ pkgs.soupault ]; };
    in
    {
      devShell = inputs.nixpkgs.lib.genAttrs systems createDevShell;
    };
}
