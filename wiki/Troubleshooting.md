# Troubleshooting Guide

Common issues and solutions for UnstableSMP.

## Installation Issues

### Plugin Won't Load

**Symptoms**: Console shows error, plugin doesn't enable

**Solutions**:
1. **Check server version**: Must be Spigot/Paper 1.21 or higher
   ```
   /version
   ```
   If version is less than 1.21, upgrade your server.

2. **Check Java version**: Must be Java 21+
   ```
   java -version
   ```
   If Java is older, install Java 21 JDK.

3. **Verify JAR file**: Ensure the file is not corrupted
   - Re-download from GitHub releases
   - Check file size (should be > 1MB)

4. **Check console output**: Look for specific error messages
   - `ClassNotFoundException` - Missing dependency
   - `NoClassDefFoundError` - Incompatible server version
   - `IOException` - File permission issues

5. **Try clean install**:
   - Stop server
   - Delete `plugins/UnstableSMP/` folder
   - Delete the JAR file
   - Re-download and install fresh
   - Start server

---

### Config File Not Generating

**Symptoms**: `config.yml` doesn't appear after plugin loads

**Solutions**:
1. Stop the server
2. Delete the entire `plugins/UnstableSMP/` folder
3. Delete the plugin JAR file
4. Restart the server
5. Wait for the plugin to fully load
6. Stop the server again
7. Replace the JAR file
8. Start the server once more

---

### File Permission Errors

**Symptoms**: Console shows `Permission denied` or `Cannot create file`

**Solutions**:
1. **Check folder permissions**:
   ```bash
   # Linux/Mac: Make folder writable
   chmod 755 plugins/UnstableSMP/
   chmod 755 plugins/UnstableSMP/userdata.db
   ```

2. **Verify ownership**:
   ```bash
   ls -l plugins/UnstableSMP/
   ```
   Should show your user as owner

3. **Check disk space**:
   ```bash
   df -h
   ```
   Ensure you have at least 100MB free

---

## Resource Pack Issues

### Players Can't Download Resource Pack

**Symptoms**: Players stuck in waiting room, pack doesn't download

**Solutions**:
1. **Verify URL is correct**:
   - Test in browser: copy `config.yml` URL and open in browser
   - Should download the ZIP file directly
   - File should be > 1MB

2. **Check SHA1 hash**:
   - Regenerate hash and update config:
     ```powershell
     # Windows
     certUtil -hashfile "resourcepack.zip" SHA1
     ```
     ```bash
     # Linux/Mac
     sha1sum resourcepack.zip
     ```
   - Copy exact output to `config.yml`

3. **Verify file is valid ZIP**:
   - Try opening locally
   - Should not be corrupted
   - Should contain valid Minecraft assets

4. **Check hosting**:
   - Ensure URL is publicly accessible
   - No authentication required
   - Firewall not blocking access
   - File size < 100MB (recommended)

5. **Test with simple pack**:
   - Create minimal test pack
   - Host it temporarily
   - Verify it works
   - Then use your main pack

---

### Players Stuck in Waiting Room

**Symptoms**: Players see resource pack loading screen forever

**Solutions**:
1. **Player can skip**:
   - Tell player to run `/skiprp`
   - Requires `unstablesmp.skiprp` permission

2. **Check resource pack URL**:
   - Verify URL is correct and accessible
   - Test download manually
   - Check file isn't corrupted

3. **Check server logs**:
   - Look for ResourcePack listener errors
   - May indicate network issues

4. **Restart server**:
   - Stop and start server
   - Relogin to test

5. **Reset player data** (last resort):
   - Locate player UUID in database
   - Remove their entry
   - Player will respawn at spawn point

---

### Resource Pack Applied Incorrectly

**Symptoms**: Textures look wrong, items don't match

**Solutions**:
1. **Verify pack format**:
   - Check pack.mcmeta file
   - Ensure format version matches your MC version
   - For 1.21: use `pack_format: 34`

2. **Check file structure**:
   - Assets should be in `assets/minecraft/...`
   - Not in root directory

3. **Regenerate pack**:
   - Recreate using proper tools
   - Test locally first
   - Then upload

---

## Disguise Issues

### Disguise Command Not Working

**Symptoms**: Command fails, skin doesn't change

**Solutions**:
1. **Check permission**:
   - Verify you have `unstablesmp.admin`
   - Use permission plugin to grant it

2. **Check player name**:
   - Name must be exact spelling
   - Case-sensitive in some cases
   - Must be valid Minecraft username

3. **Check Mojang API**:
   - May be temporarily down
   - Wait a moment and try again
   - Check server logs for API errors

4. **Try different player**:
   - Test with known player (e.g., Notch, Steve)
   - If that works, original name is invalid

---

### Disguised Players Not Visible

**Symptoms**: Name/skin changed but other players can't see it

**Solutions**:
1. **Refresh visibility**:
   - Have player logout and login
   - Other players should see new appearance

2. **Check scoreboard**:
   - Ensure scoreboard team is created correctly
   - Check for scoreboard conflicts with other plugins

3. **Clear cache** (client-side):
   - Have players clear launcher cache
   - Close and reopen game
   - Rejoin server

---

## Database Issues

### Database Corrupted

**Symptoms**: "Database is locked" or "Corrupted database" errors

**Solutions**:
1. **Stop the server** immediately
2. **Backup the database**:
   ```bash
   cp plugins/UnstableSMP/userdata.db plugins/UnstableSMP/userdata.db.backup
   ```

3. **Delete corrupted database**:
   ```bash
   rm plugins/UnstableSMP/userdata.db
   ```

4. **Restart server**:
   - New database will be created
   - Player locations will be lost
   - You can restore from backup if available

---

### Player Locations Not Saving

**Symptoms**: Players respawn at spawn after restart

**Solutions**:
1. **Check database file exists**:
   ```bash
   ls -la plugins/UnstableSMP/userdata.db
   ```

2. **Verify write permissions**:
   ```bash
   ls -la plugins/UnstableSMP/
   ```
   Should show write permissions (w flag)

3. **Check server logs** for database errors
4. **Ensure quit event fires**:
   - Player must properly quit (not crash)
   - Check QuitListener in logs

5. **Test on clean server**:
   - Create test world
   - Test player location saving
   - Verify it works

---

### Database File Size Growing Too Large

**Symptoms**: `userdata.db` becomes > 100MB

**Solutions**:
1. **Monitor file size**:
   ```bash
   ls -lh plugins/UnstableSMP/userdata.db
   ```

2. **Vacuum database** (compress):
   - Stop server
   - Use SQLite command:
     ```bash
     sqlite3 plugins/UnstableSMP/userdata.db VACUUM;
     ```
   - Restart server

3. **Archive old data**:
   - Backup current database
   - Create new empty database
   - Keeps only recent data

---

## Performance Issues

### Server Lagging After Installing Plugin

**Symptoms**: TPS drops, players experience lag

**Solutions**:
1. **Check resource pack size**:
   - Keep under 100MB
   - Compress textures
   - Remove unused assets

2. **Monitor database**:
   - Database shouldn't cause lag
   - File should stay small
   - Use VACUUM if large

3. **Check tick time**:
   - Use `/mspt` or similar
   - Should be < 50ms per tick
   - If plugin > 5ms, may be issue

4. **Disable features**:
   - Temporarily disable auto-update: `auto-update: false`
   - Disable adaptive render distance
   - See if lag persists

5. **Check other plugins**:
   - Compatibility issue with another plugin?
   - Try with only UnstableSMP
   - Gradually add plugins back

---

## Update Issues

### Auto-Updates Not Working

**Symptoms**: Plugin doesn't update even with `auto-update: true`

**Solutions**:
1. **Check internet access**:
   - Ensure server can reach GitHub
   - Check firewall rules
   - Test with: `ping github.com`

2. **Check config**:
   ```yaml
   auto-update: true
   ```
   Ensure it's set to `true` (not `false` or commented out)

3. **Check server logs**:
   - Look for update check messages
   - May show network errors

4. **Manual update**:
   - Download latest JAR from GitHub
   - Replace old file
   - Restart server

---

## Advanced Troubleshooting

### Enable Debug Logging

Some issues require detailed logs:

1. Edit `config.yml`:
   ```yaml
   debug: true
   ```

2. Restart server
3. Reproduce the issue
4. Check logs in `plugins/UnstableSMP/logs/`
5. Share logs with support

### Collecting Debug Information

When reporting issues, provide:
1. **Server version**: `/version`
2. **Java version**: `java -version`
3. **Plugin version**: `/unstable version`
4. **Full error logs**: From console startup to error
5. **Config file**: (without sensitive URLs)
6. **Steps to reproduce**: Exact steps to cause the issue

---

## Reporting Bugs

If you can't fix the issue:

1. Check [FAQ](FAQ) for similar questions
2. Search [GitHub Issues](https://github.com/ResoluteStudios/UnstableSMP/issues)
3. [Create new issue](https://github.com/ResoluteStudios/UnstableSMP/issues/new) with:
   - Clear title
   - Detailed description
   - Debug information
   - Server logs
   - Steps to reproduce

---

## Still Need Help?

- **For general questions**: See [FAQ](FAQ)
- **For setup help**: See [Installation](Installation)
- **For feature info**: See [Features](Features)
- **For command help**: See [Commands](Commands)
- **For config help**: See [Configuration](Configuration)
