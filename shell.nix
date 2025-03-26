let
  nixpkgs = import <nixpkgs> { config = { allowUnfree = true; }; };
in
nixpkgs.mkShell {
  name = "env";
  buildInputs = [
    nixpkgs.webkitgtk_4_1
    nixpkgs.gtk3
    nixpkgs.cairo
    nixpkgs.gdk-pixbuf
    nixpkgs.glib
    nixpkgs.dbus
    nixpkgs.openssl_3
    nixpkgs.librsvg
    nixpkgs.curl
    nixpkgs.wget
    nixpkgs.pkg-config
    nixpkgs.dbus
    nixpkgs.libsoup
    nixpkgs.librsvg
    nixpkgs.cargo-tauri
  ];
}
