# Features

UnstableSMP provides a comprehensive set of features designed to enhance server gameplay and performance. Below is a detailed overview of each feature.

## Disguise System

Change player skins and display names to impersonate other players or customize appearance.

### Features
- **Skin Changing**: Apply any Minecraft skin to a player using Mojang's official API
- **Name Display**: Changes player list name, display name, and nametag
- **Scoreboard Integration**: Uses scoreboard teams to ensure names display correctly to all players
- **Admin Only**: Requires `unstablesmp.admin` permission

### Command
```
/disguise <skinName> [targetPlayer]
```

### Example
```
/disguise Notch
/disguise Steve player123
```

### How It Works
1. Fetches the skin data from Mojang's API using the player name
2. Applies the texture to the target player's profile
3. Updates the player's displayed name across chat, tab list, and above their head
4. Refreshes visibility to other players to apply changes immediately

---

## Resource Pack Management

Automatic resource pack handling with a waiting room system.

### Features
- **Waiting Room**: Players join in a hidden spectator void until resource pack loads
- **Progress Bar**: Animated loading bar while waiting for the resource pack
- **Auto-Restore**: Automatically restores players to their last logout location
- **Flexible Status**: Handles accepted, declined, and failed downloads
- **SkipRP Command**: Players can skip the resource pack requirement

### How It Works
1. Player joins the server
2. Player is teleported to void and hidden from others (spectator mode)
3. Resource pack is prompted to the player
4. Loading bar animates while waiting for response
5. Once status is received (loaded, declined, or failed), player is restored to their logout location
6. Player is shown to other players and can rejoin the game

### Commands
```
/skiprp - Skip the resource pack requirement and join immediately
```

---

## Player Location Persistence

Automatically save and restore player locations across server restarts.

### Features
- **Automatic Saving**: Saves player location when they log off (if not in waiting room)
- **Automatic Restoration**: Restores player to last logout location on join
- **Database Storage**: Uses SQLite for reliable data persistence
- **No Data Loss**: Database file is never replaced—only created once
- **Spawn Fallback**: If no location is saved, player spawns at world spawn

### How It Works
1. When a player logs off, their location (world, x, y, z, yaw, pitch) is saved to the database
2. On disable, all online players' locations are saved
3. When a player joins, their saved location is retrieved
4. After resource pack handling, they are teleported to their saved location
5. If no location exists, they spawn at the world spawn point

### Database
- **File**: `plugins/UnstableSMP/userdata.db`
- **Type**: SQLite 3
- **Table**: `player_data` (uuid, world, x, y, z, yaw, pitch)

---

## Adaptive Render Distance

Automatically optimize server performance by adjusting view distance based on player count and TPS.

### Features
- **Automatic Adjustment**: Adjusts view distance every 30 seconds
- **Performance Based**: Responds to server TPS (ticks per second)
- **Player Count Aware**: Reduces distance with more players
- **Configurable**: Set minimum and maximum render distance

### How It Works
1. Monitors server TPS and player count every 30 seconds
2. If TPS drops below threshold, reduces render distance
3. If TPS recovers, gradually increases render distance
4. Ensures optimal performance without manual intervention

---

## Auto-Updating

Automatically check for and install plugin updates.

### Features
- **Background Checking**: Checks for updates every 10 minutes
- **Auto-Installation**: Automatically downloads and installs updates
- **Admin Notifications**: Notifies admins when updates are available
- **Configurable**: Toggle notifications in config

### Commands
```
/unstable version - Display current plugin version
/unstable notifications autoupdate [true/false] - Toggle update notifications
```

### How It Works
1. Plugin queries GitHub releases periodically
2. If a newer version is found, downloads the JAR
3. Installs the update on next server restart
4. Admins are notified of available updates on join or in real-time

---

## Combat Balancing

Balanced weapon mechanics and enchantment management.

### Features
- **Mace Nerf**: Reduced damage from the Mace weapon to balance combat
- **Wind Burst Control**: Temporary disable of Wind Burst enchantment
- **Exploit Prevention**: Prevents mace swapping exploits using ProtocolLib

### Configuration
Adjust combat values in `config.yml` to match your server's balance.

---

## Visual Improvements

Enhanced user interface and visual feedback.

### Features
- **Progress Bars**: Animated progress bars with color gradients (Red→Yellow→Green)
- **Configurable Prefix**: Customize the plugin message prefix with hex colors
- **Action Bar Messages**: Real-time feedback using action bars
- **Full Color Support**: Complete RGBA color support in console and chat

### Example Progress Bar
```
[████████░░░░░░░░░░░░] 40% Loading Resource Pack...
```

---

## Data Persistence

All player data is stored locally on your server.

### What's Saved
- Player locations (world, coordinates, rotation)
- Custom player states

### Backup Recommendation
Regularly backup the `plugins/UnstableSMP/` folder to ensure data safety.

---

## Permissions

All UnstableSMP features are controlled by permissions:

| Permission | Description |
|-----------|-------------|
| `unstablesmp.admin` | Access to admin commands and features |
| `unstablesmp.disguise` | Permission to use `/disguise` command |
| `unstablesmp.skiprp` | Permission to use `/skiprp` command |

For more details on permissions, see [Commands](Commands).
