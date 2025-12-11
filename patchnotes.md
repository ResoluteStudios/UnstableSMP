# UnstableSMP Patch Notes

## v1.1.5

### Features
- **Adaptive Render Distance**: Automatically adjusts server view distance based on player count and TPS to maintain performance.
- **Disguise System**: Added `/disguise <skinName> [player]` to change player skins using Mojang's API.
- **Visuals**:
    - **Gradual Progress Bars**: New UI for Updater and Resource Pack loading with [███░░] style and Red-Green text gradients.
    - **Configurable Prefix**: Added a fancy hex-color prefix to all plugin messages (configurable).
    - **Resource Pack Loading**: Added an animated loading bar while the resource pack is downloading.

### Fixes
- **Mace Nerf**: Verified and polished combat nerf logic.

## v1.1.4

### Fixes
- **Critical Persistence Fix**: Fixed a bug where players would respawn at the world spawn (or void) if they disconnected/crashed while in the Resource Pack "Waiting Room". The plugin now correctly ignores the temporary void location and preserves the player's last valid logout spot.
- **Spawn Logic**: Players joining for the first time appear at world spawn. Returning players appear at their last saved location.

## v1.1.3

### Features
- Added save-on-disable functionality to ensure player locations are saved when the server stops or reloads.
- Implemented ProtocolLib packet listener to prevent Mace swap exploits.
- Added logic to temporarily disable the Wind Burst enchantment during Mace attacks to prevent the launch effect.
- Updated all plugin messages to use color codes (green for success, red for error).

### Fixes
- Fixed an issue where players might respawn at the wrong location after a server crash.
- Fixed an issue where the Mace nerf could be bypassed by swapping items.

## v1.1.2

### Features
- Added a waiting room for resource pack loading. Players are hidden and in spectator mode until they accept the pack.
- Added /unstable skiprp <player> command to skip the resource pack check.
- Reduced Mace damage to 66% of original value.

### Fixes
- Fixed various deprecation warnings.
- Fixed Logic for resource pack status handling.

## v1.1.0

### Features
- Added ProtocolLib dependency.
- Implemented basic logout location logic.
- Converted all text to Small Caps.
