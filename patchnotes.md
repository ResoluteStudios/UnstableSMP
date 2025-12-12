# UnstableSMP Patch Notes

## v1.2.5

### Features
- **Persistent Fishing**: Fishing rod casts now persist through teleports, portals, and logouts.
    - **Teleport Persistence**: Hooks remain when players teleport or use commands like `/tp`.
    - **Portal Persistence**: Hooks remain when going through Nether/End portals.
    - **Logout Persistence**: Hooks are saved to database and restored when players rejoin.
    - **Client Sync**: Uses ProtocolLib to force client synchronization after hook restoration.

### Technical Changes
- **Database Schema**: Added `fishing_state` table to store hook location and velocity.
- **FishingListener**: New listener that handles:
    - `PlayerFishEvent` - Track casting state and hook location
    - `PlayerTeleportEvent` - Save hook before teleport, restore after
    - `PlayerPortalEvent` - Save hook before portal, restore after
- **JoinListener**: Restores fishing hook if player was casting when they logged out.
- **QuitListener**: Saves fishing state when player logs out while casting.
- **ProtocolLib**: Sends `SPAWN_ENTITY` packet to sync hook visually with client.

## v1.2.4

### Features
- **Persistent Disguise System**: Completely overhauled the `/disguise` command with full persistence and comprehensive name changes.
    - **Database Storage**: Disguises are now saved to the database and persist across server restarts.
    - **Complete Identity Change**: Disguises now affect:
        - Player skin texture
        - Nameplate (above player's head)
        - Tab list name
        - Display name (in chat)
        - Join/quit messages
    - **Reset Command**: Added `/disguise reset` to remove disguises and restore original identity.
    - **Auto-Application**: Saved disguises are automatically reapplied when players join the server.
- **Deathkick Behavior**: Changed deathkick to only kick players from the server instead of banning them.
    - Players can rejoin immediately after being kicked for dying.
    - The configured death message is still shown when kicked.

### Breaking Changes
- **Removed Resource Pack System**: Completely removed all resource pack functionality.
    - Players now join normally without any waiting area or resource pack prompts.
    - Join messages appear immediately.
    - Players appear in tab list immediately.
    - Chat is enabled immediately upon join.
    - Removed `/unstable skiprp` command.
- **Simplified Player Flow**: Players join directly into the game at their last logout location (or world spawn for new players).

### Technical Changes
- **Database Schema**: Added `disguises` table to store persistent disguise data (name, skin value, skin signature).
- **DisguiseCommand**: Complete rewrite with database integration, reset functionality, and all name display changes.
- **JoinListener**: Auto-applies saved disguises on join and customizes join messages.
- **QuitListener**: Customizes quit messages for disguised players.
- Deleted `ResourcePackListener.java` - no longer needed.
- Deleted `ChatListener.java` - no longer needed.
- Deleted `TabListUtils.java` - no longer needed.
- Simplified `JoinListener.java` - removed waiting area logic.
- Simplified `QuitListener.java` - always saves player location.
- Updated `UnstableSMP.java` - removed pending player tracking and restore logic.
- Updated `UnstableCommand.java` - removed skiprp command.
- Updated `DeathListener.java` - removed ban functionality, now only kicks.

## v1.2.3

### Features
- **Silent Auto-Update System**: The plugin now checks for updates every 10 minutes silently without console spam.
    - Console is only notified when an update is actually found and downloaded.
    - Opped players see a progress bar during the download process.
    - Notifications appear only after successful download completion.
- **Enhanced Resource Pack Loading**:
    - Players are now hidden from the tab list while in the waiting area.
    - Chat is blocked for players during resource pack loading.
    - Added 10-second "Finalizing..." countdown after pack loads successfully.
    - Players who decline or fail to download the pack are immediately restored (no delay).
    - Full location persistence now includes pitch and yaw (already implemented in database).
- **Code Security**:
    - Added exception handler to filter stack traces and hide plugin code from crash reports.
    - Global exception handler prevents plugin internals from appearing in error messages.
    - Makes reverse-engineering more difficult.
- **ProtocolLib Integration**:
    - Tab list manipulation now uses ProtocolLib for reliable cross-version compatibility.
    - New `TabListUtils` utility for hiding/showing players in tab list.

### Technical Changes
- Created `ChatListener.java` to prevent pending players from sending chat messages.
- Created `TabListUtils.java` for ProtocolLib-based tab list manipulation.
- Created `ExceptionHandler.java` for stack trace filtering and obfuscation.
- Updated `Updater.java` with silent checking and progress bar notifications.
- Updated `ResourcePackListener.java` with 10-second post-load delay.
- Updated `JoinListener.java` to hide players from tab list during loading.
- Updated `UnstableSMP.java` to show players in tab list after restoration.

## v1.2.1

### Features
- **Enhanced Disguise System**: The `/disguise` command now applies both player skin texture and changes the player's displayed name.
    - Changes the display name shown in chat and player list.
    - Keeps the player's actual username as the nameplate (above their head).
- **Resource Pack Logic**: After resource pack loading (accepted, declined, or failed), players are now restored to their last logout location instead of being moved to a default spawn.
- **Database Improvements**: The plugin database file is no longer replaced on startup—it is only created once and new data is added to it, preserving all existing player location data.

### Fixes
- Improved database persistence to prevent data loss on plugin restarts.

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
