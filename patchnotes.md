# UnstableSMP Patch Notes

## v1.2.0

### Features
- **Version Command**: Added `/unstable version` to quickly check the running plugin version and authors.
- **Console Colors**: The plugin now outputs colored messages to the server console during startup, shutdown, and other operations, making logs easier to read.

## v1.1.6

### Features
- **Adaptive Render Distance**: Automatically adjusts server view distance based on player count and TPS to maintain performance.
- **Disguise System**: Added /disguise <skinName> [player] to change player skins using Mojang's API.
- **Visuals**:
    - **Gradual Progress Bars**: New UI for Updater and Resource Pack loading with [|||  ] style and Red-Green text gradients.
    - **Configurable Prefix**: Added a fancy hex-color prefix to all plugin messages (configurable).
    - **Resource Pack Loading**: Added an animated loading bar while the resource pack is downloading.

## v1.1.4

### Fixes
- **Critical Persistence Fix**: Fixed a bug where players would respawn at the world spawn (or void) if they disconnected/crashed while in the Resource Pack "Waiting Room". The plugin now correctly ignores the temporary void location and preserves the player's last valid logout spot.

## v1.1.3

### Features
- **Save-on-Disable**: Plugin safeguards player data on server stop.
- **Mace Nerf**: Reduced damage (x0.66) and patched swap exploits with ProtocolLib.
- **Wind Burst Patch**: Prevents infinite launch exploits.
- **Visuals**: All messages now support colors.

## v1.1.0 - v1.1.2

- **ProtocolLib**: Added dependency for advanced packet handling.
- **Resource Pack Waiting Room**: Secure area for pack loading.
- **Small Caps**: Stylized text support.
