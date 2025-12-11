# UnstableSMP Patch Notes

## v1.2.0

### Features
- **Auto-Pilot Updating**: The plugin now automatically checks for updates every 10 minutes and will download/install them immediately if found.
    - Added config `notifications.autoupdate` to toggle admin notifications (Default: true).
    - Admins are notified on join and in real-time if an update is found.
- **Commands**:
    - `/unstable version`: Displays the current plugin version.
    - `/unstable notifications autoupdate [true/false]`: Toggles update notifications.
- **Console**: Improved console output with full color support.

## v1.1.6

### Features
- **Adaptive Render Distance**: Automatically adjusts server view distance based on player count and TPS to maintain performance.
- **Disguise System**: Added /disguise <skinName> [player] to change player skins using Mojang's API.
- **Visuals**:
    - **Gradual Progress Bars**: New UI for Updater and Resource Pack loading with [|||  ] style and Red-Green text gradients.
    - **Configurable Prefix**: Added a fancy hex-color prefix to all plugin messages (configurable).
    - **Resource Pack Loading**: Added an animated loading bar while the resource pack is downloading.

### Fixes
- **Mace Nerf**: Verified and polished combat nerf logic.

## v1.1.4

### Fixes
- **Critical Persistence Fix**: Preserves player logout location if disconnected in waiting room.

## v1.1.3

### Features
- Save-on-disable functionality.
- Mace swap exploit prevention (ProtocolLib).
- Wind Burst enchantment temporary disable.

## v1.1.2

### Features
- Resource pack waiting room.
- SkipRP command.
- Mace damage nerf.

## v1.1.0

### Features
- Initial release with Death/RP logic.
