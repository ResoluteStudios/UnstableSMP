# Commands Reference

All UnstableSMP commands and their usage.

## Disguise Command

**Permission**: `unstablesmp.admin`

### Syntax
```
/disguise <skinName> [targetPlayer]
```

### Description
Change a player's skin and display name to that of another player.

### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `skinName` | String | Yes | The Minecraft username whose skin to apply |
| `targetPlayer` | String | No | The player to disguise (default: yourself) |

### Examples
```
# Disguise yourself as Notch
/disguise Notch

# Disguise another player
/disguise Steve player123

# Disguise a player as someone else
/disguise Herobrine targetPlayer
```

### What It Changes
- **Skin Texture**: Applies the selected player's skin texture
- **Player List Name**: Changes how the player appears in the player list (Tab)
- **Display Name**: Changes how the player's name appears in chat
- **Nametag**: Changes the name displayed above the player's head
- **Scoreboard**: Uses scoreboard teams to ensure proper display

### Notes
- The actual UUID and login name do NOT change
- Other players will see the new skin and name immediately
- The disguise persists until changed again or the server restarts
- Skin data is fetched from Mojang's official API

---

## SkipRP Command

**Permission**: `unstablesmp.skiprp`

### Syntax
```
/skiprp
```

### Description
Skip the resource pack requirement and join the server immediately.

### Examples
```
/skiprp
```

### What It Does
1. Exits the waiting room
2. Shows you to other players
3. Teleports you to your last logout location
4. Sets you to survival mode

### Notes
- Only works while in the waiting room (loading resource pack)
- Resource pack will not be applied if skipped
- Can only be used once per join

---

## Unstable Command

**Permission**: `unstablesmp.admin`

### Syntax
```
/unstable <subcommand> [args]
```

### Subcommands

#### version
Display the current plugin version.

**Syntax**: `/unstable version`

**Example**:
```
/unstable version
```

**Output**:
```
UnstableSMP v1.2.1
```

---

#### notifications
Manage admin notifications.

**Syntax**: `/unstable notifications autoupdate [true/false]`

**Description**: Toggle automatic update notifications.

**Examples**:
```
# Enable update notifications
/unstable notifications autoupdate true

# Disable update notifications
/unstable notifications autoupdate false

# Toggle current state
/unstable notifications autoupdate
```

**Default**: `true` (enabled)

---

## Permission Nodes

All plugin features are controlled by permission nodes:

| Permission | Command(s) | Description |
|-----------|-----------|-------------|
| `unstablesmp.admin` | `/disguise`, `/unstable` | Admin access to all features |
| `unstablesmp.disguise` | `/disguise` | Permission to disguise players |
| `unstablesmp.skiprp` | `/skiprp` | Permission to skip resource pack |

### Setting Permissions

#### In LuckPerms
```
/lp user <player> permission set unstablesmp.admin true
/lp group <group> permission set unstablesmp.admin true
```

#### In PermissionsEx
```
/pex user <player> add unstablesmp.admin
/pex group <group> add unstablesmp.admin
```

#### In bukkit.yml
```yaml
permissions:
  unstablesmp.admin:
    description: "UnstableSMP Admin Permission"
    default: op
  unstablesmp.disguise:
    description: "Disguise Players"
    default: false
  unstablesmp.skiprp:
    description: "Skip Resource Pack"
    default: false
```

---

## Command Help in-Game

Use `/help <command>` to get in-game help:

```
/help disguise
/help skiprp
/help unstable
```

---

## Errors and Messages

### Disguise Command Errors
```
"You do not have permission." - Missing unstablesmp.admin permission
"Usage: /disguise <skinName> [targetPlayer]" - Invalid arguments
"Player not found." - Target player is not online
"Failed to fetch skin (Invalid name?)." - Player name doesn't exist or API error
"Disguised [player] as [skinName]" - Success message
```

### SkipRP Command Errors
```
"You are not in the waiting room." - Command used outside waiting room
"You have been released from the waiting room!" - Success message
```

### Unstable Command Errors
```
"You do not have permission." - Missing unstablesmp.admin permission
"Usage: /unstable version|notifications" - Invalid subcommand
```

---

## Command Tips

- **Admin commands** can be executed from console or in-game
- **Commands are case-insensitive**: `/DISGUISE`, `/disguise`, `/Disguise` all work
- **Tab completion** is available for command suggestions
- **Aliases**: Some commands may have shortcuts (check your server implementation)

For more help, see [FAQ](FAQ) or [Troubleshooting](Troubleshooting).
