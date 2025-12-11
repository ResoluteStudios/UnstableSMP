# FAQ - Frequently Asked Questions

## General Questions

### What is UnstableSMP?

UnstableSMP is a comprehensive Spigot/Paper plugin that adds advanced features to multiplayer survival servers, including player disguises, resource pack management, location persistence, adaptive performance optimization, and auto-updating capabilities.

### What Minecraft versions does it support?

UnstableSMP supports **Minecraft 1.21+** on Spigot or Paper servers. Paper is recommended for better performance and stability.

### Is it free?

Yes! UnstableSMP is open-source and free to use. You can find the source code on [GitHub](https://github.com/ResoluteStudios/UnstableSMP).

### Can I use this on a public server?

Absolutely! The plugin is designed for both small and large servers. Just ensure your server has adequate resources and proper backups.

---

## Installation & Setup

### Do I need any special permissions to install it?

You need write access to the `plugins` folder on your server. Most server hosts provide this by default.

### Will it work on a Spigot server?

Yes, UnstableSMP works on Spigot 1.21+ and Paper 1.21+. Paper is recommended for better compatibility and performance.

### How do I uninstall the plugin?

1. Stop the server
2. Delete `UnstableSMP-X.X.X.jar` from the `plugins` folder
3. (Optional) Delete `plugins/UnstableSMP/` folder to remove config and data
4. Restart the server

**Note**: Deleting the plugin folder will also remove all saved player locations!

### What if the config doesn't generate?

Delete the `plugins/UnstableSMP/` folder and restart the server. The plugin will regenerate the config with default values.

---

## Features

### Disguise System

**Q: Can players disguise themselves without admin permission?**

A: No, the `/disguise` command requires `unstablesmp.admin` permission. Only server admins can use it.

**Q: Does disguise change the actual UUID?**

A: No, disguise only changes the displayed name and skin texture. The actual UUID remains unchanged, so inventory and stats stay with the original player.

**Q: How long does a disguise last?**

A: Indefinitely, until the player logs off or is disguised as someone else. It persists through server restarts.

**Q: What if the skin doesn't exist?**

A: The plugin will fetch from Mojang's API and return an error if the player name is invalid or doesn't have a skin.

---

### Resource Pack

**Q: Why are players put in a waiting room?**

A: The waiting room ensures the resource pack loads before players join the main world. This prevents visual glitches and ensures all players see the same textures.

**Q: Can players skip the resource pack?**

A: Yes, with the `/skiprp` command (requires `unstablesmp.skiprp` permission). They'll join without the resource pack applied.

**Q: Where does my resource pack URL go?**

A: In the `config.yml` file under `resource-pack.url`. It must be a direct link to a `.zip` file.

**Q: How do I generate the SHA1 hash?**

A: 
- **Windows**: `certUtil -hashfile "resourcepack.zip" SHA1`
- **Linux/Mac**: `sha1sum resourcepack.zip`

**Q: What if players can't download the pack?**

A: Check that:
- The URL is correct and publicly accessible
- The ZIP file is not corrupted
- The file size isn't too large
- The SHA1 hash is correct

**Q: Do I need a resource pack for the plugin to work?**

A: No, but the plugin will prompt players for one. You can set a dummy URL or leave it as-is.

---

### Player Locations

**Q: Where are player locations stored?**

A: In `plugins/UnstableSMP/userdata.db`, a SQLite database file.

**Q: Will player locations be restored after a crash?**

A: Yes! The plugin auto-saves locations on quit, so they persist even after crashes.

**Q: What if a player's location no longer exists?**

A: If the world doesn't exist, the player spawns at the world spawn point instead.

**Q: Can I backup player data?**

A: Yes! Regularly backup the `plugins/UnstableSMP/` folder to preserve player data.

**Q: Can I migrate player data to another server?**

A: Yes, copy the `userdata.db` file from one server's `plugins/UnstableSMP/` to another. The database structure is identical.

---

### Adaptive Render Distance

**Q: How does it work?**

A: The plugin monitors server TPS and player count, adjusting render distance every 30 seconds to maintain optimal performance.

**Q: Can I disable it?**

A: Yes, comment out or remove the render distance system initialization in the code, or disable it in a future config option.

**Q: Will it affect my players' gameplay?**

A: Minimal impact. The changes happen gradually and help prevent lag spikes.

---

### Auto-Updates

**Q: Will updates install automatically?**

A: Yes, the plugin checks GitHub every 10 minutes. If an update is found, it downloads and installs on next server restart.

**Q: Can I disable auto-updates?**

A: Yes, set `auto-update: false` in `config.yml`.

**Q: How do I get update notifications?**

A: Enable `notifications.autoupdate: true` in `config.yml`. Admins with `unstablesmp.admin` permission will be notified.

**Q: What if an update breaks my server?**

A: Test updates on a test server first. You can manually update the JAR file instead of using auto-update.

---

## Permissions

**Q: How do I give a player admin permission?**

A: Use your permission plugin:
- **LuckPerms**: `/lp user <player> permission set unstablesmp.admin true`
- **PermissionsEx**: `/pex user <player> add unstablesmp.admin`
- **Default (OP)**: Give them OP status with `/op <player>`

**Q: What's the difference between permissions?**

A:
- `unstablesmp.admin` - Access to all admin commands
- `unstablesmp.disguise` - Permission to disguise players
- `unstablesmp.skiprp` - Permission to skip resource pack

---

## Troubleshooting

**Q: The plugin won't start!**

A: Check the console for errors. Common issues:
- Server version < 1.21
- Java version < 21
- Plugin JAR is corrupted

**Q: Players can't see disguised players!**

A: The disguise should work automatically. If it doesn't:
- Check if `unstablesmp.admin` permission is set correctly
- Verify the skin name is valid
- Check console logs for errors

**Q: Auto-updates aren't working!**

A: Check that:
- `auto-update: true` in config
- Server can access the internet/GitHub
- Firewall isn't blocking downloads

**Q: Database errors in console!**

A: Ensure `plugins/UnstableSMP/` has write permissions. Check logs for specific errors.

---

## Performance

**Q: Will this plugin cause lag?**

A: No, UnstableSMP is optimized for minimal performance impact. The adaptive render distance feature actually *reduces* lag.

**Q: How much disk space does it use?**

A: About 5MB for the plugin JAR. The player database grows slowly (~1KB per player).

**Q: Is it safe for large servers?**

A: Yes, UnstableSMP is designed to scale with server size.

---

## Support & Contributions

**Q: Where do I report bugs?**

A: Open an issue on [GitHub Issues](https://github.com/ResoluteStudios/UnstableSMP/issues).

**Q: Can I contribute code?**

A: Yes! Submit a pull request on [GitHub](https://github.com/ResoluteStudios/UnstableSMP). We welcome contributions.

**Q: Who maintains this plugin?**

A: The plugin is maintained by ResoluteStudios. See the README for more info.

---

## Still Have Questions?

- Check [Troubleshooting](Troubleshooting) for common issues
- Read the [Configuration](Configuration) guide for setup help
- See [Commands](Commands) for command syntax
- View [Features](Features) for detailed feature explanations

If you can't find the answer, [open an issue on GitHub](https://github.com/ResoluteStudios/UnstableSMP/issues).
