# Configuration Guide

Complete guide to configuring UnstableSMP via `config.yml`.

## Config File Location

```
plugins/UnstableSMP/config.yml
```

## Default Configuration

```yaml
# UnstableSMP Configuration

# Plugin Prefix
prefix: "§6[§eUnstableSMP§6] "

# Resource Pack Settings
resource-pack:
  url: "https://example.com/resourcepack.zip"
  sha1: "0000000000000000000000000000000000000000"
  prompt: "Download our resource pack for the best experience!"

# Auto-Update Settings
auto-update: true

# Notifications
notifications:
  autoupdate: true
```

## Configuration Options

### Plugin Prefix

**Key**: `prefix`

**Type**: String

**Default**: `"§6[§eUnstableSMP§6] "`

**Description**: The prefix displayed in all plugin messages. Supports Minecraft color codes.

**Color Code Reference**:
- `§0` - Black
- `§1` - Dark Blue
- `§2` - Dark Green
- `§3` - Dark Cyan
- `§4` - Dark Red
- `§5` - Purple
- `§6` - Gold
- `§7` - Gray
- `§8` - Dark Gray
- `§9` - Blue
- `§a` - Green
- `§b` - Cyan
- `§c` - Red
- `§d` - Magenta
- `§e` - Yellow
- `§f` - White
- `§l` - Bold
- `§o` - Italic
- `§r` - Reset

**Example**:
```yaml
prefix: "§b[§3UnstableSMP§b] "  # Cyan prefix
prefix: "§c[UnstableSMP] "      # Red prefix
```

---

### Resource Pack Settings

#### URL

**Key**: `resource-pack.url`

**Type**: String (URL)

**Default**: `"https://example.com/resourcepack.zip"`

**Description**: The URL where your resource pack is hosted. This must be a direct link to a `.zip` file.

**Example**:
```yaml
resource-pack:
  url: "https://cdn.example.com/packs/server-pack.zip"
```

#### SHA1 Hash

**Key**: `resource-pack.sha1`

**Type**: String (SHA1 hex)

**Default**: `"0000000000000000000000000000000000000000"`

**Description**: The SHA1 hash of your resource pack ZIP file. Used to verify the pack's integrity. Generate using:

**On Windows**:
```powershell
certUtil -hashfile "resourcepack.zip" SHA1
```

**On Linux/Mac**:
```bash
sha1sum resourcepack.zip
```

**Example**:
```yaml
resource-pack:
  sha1: "d41d8cd98f00b204e9800998ecf8427e00000000"
```

#### Prompt Message

**Key**: `resource-pack.prompt`

**Type**: String

**Default**: `"Download our resource pack for the best experience!"`

**Description**: The message shown to players when prompting them to download the resource pack. Supports color codes.

**Example**:
```yaml
resource-pack:
  prompt: "§6Please download our §eCustom Resource Pack§6!"
```

---

### Auto-Update Settings

#### Enable Auto-Updates

**Key**: `auto-update`

**Type**: Boolean

**Default**: `true`

**Description**: Enable automatic checking and installation of plugin updates. When enabled, the plugin checks for updates every 10 minutes.

**Example**:
```yaml
auto-update: true   # Enable auto-updates
auto-update: false  # Disable auto-updates
```

---

### Notifications

#### Auto-Update Notifications

**Key**: `notifications.autoupdate`

**Type**: Boolean

**Default**: `true`

**Description**: Notify server admins (with `unstablesmp.admin` permission) when updates are available. Notifications appear on join and in real-time.

**Example**:
```yaml
notifications:
  autoupdate: true   # Enable update notifications
  autoupdate: false  # Disable update notifications
```

---

## Complete Example Configuration

```yaml
# UnstableSMP Configuration File

# Main plugin prefix for messages
prefix: "§6[§eUnstableSMP§6] "

# Resource Pack Configuration
resource-pack:
  # URL to your resource pack ZIP file
  url: "https://cdn.myserver.com/resource-packs/main.zip"
  
  # SHA1 hash for integrity verification
  # Generate with: sha1sum <filename> (Linux/Mac) or certUtil -hashfile <filename> SHA1 (Windows)
  sha1: "d41d8cd98f00b204e9800998ecf8427e"
  
  # Message shown when prompting players to download
  prompt: "§cDownload our §eCustom Resource Pack§c for the full experience!"

# Automatic Plugin Updates
auto-update: true

# Admin Notifications
notifications:
  autoupdate: true
```

---

## Reloading Configuration

After editing `config.yml`, reload the configuration:

1. **Stop the server** (recommended for full reload)
2. **Edit** `plugins/UnstableSMP/config.yml`
3. **Start the server**

Or use a reload command if your server supports it:
```
/reload
```

---

## Troubleshooting Config Issues

### Resource Pack Not Loading
- Verify the URL is accessible and returns a valid ZIP file
- Check the SHA1 hash is correct
- Ensure the ZIP file is not corrupted

### Plugin Not Starting
- Check for syntax errors in YAML (indentation matters!)
- Verify all required fields are present
- Check console logs for error messages

### Messages Not Displaying Correctly
- Verify color codes are valid (`§` + letter/number)
- Ensure the string is properly quoted if it contains special characters

---

## Performance Tips

1. **Set reasonable resource pack sizes** - Large packs take longer to download
2. **Use a CDN** - Host your resource pack on a fast, reliable server
3. **Monitor auto-updates** - Disable if updates cause issues
4. **Backup regularly** - Keep backups of your `config.yml` and player data

For more help, see [Troubleshooting](Troubleshooting).
