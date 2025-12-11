# UnstableSMP

UnstableSMP Plugin is a plugin that introduces similar features to those that are on the youtuber UnstableSMP


# UnstableSMP Plugin

**UnstableSMP** is a plugin that introduces features that are akin to those on the famous YouTuber UnstableSMP


## Features

###  Death Mechanics
- **Deathkick**: Players are kicked and banned upon death (configurable).
- **Spectator Mode**: Alternatively, players can be put into spectator mode instead of being banned.
- **Custom Sounds**: Plays a global sound effect when a player dies (Beacon Deactivate @ 0.8 volume).
- **Logout Location**: Guarantees players respawn at their exact logout location (persistence/crash-proof).

###  Resource Pack Logic
- **Waiting Room**: Players join in a "void" state (Spectator/Hidden) until they interact with the Resource Pack prompt.
- **Strict Flow**:
  - **Accepted** -> Wait for load -> Restore to Survival.
  - **Declined/Failed** -> Restore to Survival (or kick, depending on config).
  - **Skip Command**: Admins can force-skip this check for stuck players.

###  Combat Balance (v1.1.3)
- **Mace Nerf**: Mace damage is reduced to **66%** (`x0.66`).
- **Anti-Swap Exploit**: Uses **ProtocolLib** to strictly inspect packets. Prevents players from hitting with a Mace and swapping to a Sword to bypass cooldowns or enchantments.
- **Wind Burst Nerf**: Temporarily disables the *Wind Burst* enchantment during Mace attacks to prevent the launch effect.

###  Item Restrictions
- **Anti-Netherite**: Prevents crafting or smithing Netherite gear.
- **Mace Ban**: Option to ban the crafting/usage of Maces entirely.

## Commands

| Command | Permission | Description |
|---|---|---|
| `/unstable <feature> <true/false>` | `unstablesmp.admin` | Toggle features (e.g., `deathkick`, `macenerf`). |
| `/unstable update` | `unstablesmp.admin` | Force check for updates via GitHub API. |
| `/unstable version` | `unstablesmp.admin` | Show plugin version and authors. |
| `/unstable skiprp <player>` | `unstablesmp.admin` | Skip the Resource Pack waiting room for a player. |
| `/disguise <skin> [player]` | `unstablesmp.admin` | Disguise as another player (Skin). |

## Permissions

- `unstablesmp.admin`: Access to all commands.
- `unstablesmp.bypass`: Bypasses the death kick/ban mechanic.

## Installation

1. Download the [Latest Version](https://github.com/ResoluteStudios/UnstableSMP/releases/latest)
2. Install [ProtocolLib](https://github.com/dmulloy2/ProtocolLib/releases/latest) **(Required)** and drop both plugins into your `plugins` folder.
3. Restart the server.

---
