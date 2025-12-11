# Installation Guide

## Requirements

- **Minecraft Server**: Spigot 1.21+ or Paper 1.21+
- **Java**: Java 21 or higher
- **Disk Space**: ~5MB for the plugin and database

## Installation Steps

### 1. Download the Plugin

Download the latest release (`.jar` file) from the [GitHub Releases](https://github.com/ResoluteStudios/UnstableSMP/releases) page.

### 2. Place in Plugins Folder

1. Navigate to your server directory
2. Create a `plugins` folder if it doesn't exist
3. Place the `UnstableSMP-X.X.X.jar` file in the `plugins` folder

```
server-root/
├── plugins/
│   └── UnstableSMP-1.2.1.jar
├── server.properties
└── ...
```

### 3. Configure the Plugin

1. Start your server once (the plugin will generate `config.yml`)
2. Stop the server
3. Edit `plugins/UnstableSMP/config.yml` with your settings
4. Start the server again

## Configuration

See the [Configuration](Configuration) page for detailed config options.

### Quick Setup

At minimum, set your resource pack URL in `config.yml`:

```yaml
resource-pack:
  url: "https://your-domain.com/resourcepack.zip"
  sha1: "your-sha1-hash"
```

## Verification

After installation, verify the plugin is running:

1. Start your server
2. Check the console for: `[UnstableSMP] UnstableSMP enabled!`
3. Run `/unstable version` in-game (requires admin permission)

## Building from Source

If you want to build the plugin yourself:

### Prerequisites
- **Maven** 3.8.0+
- **Java 21** JDK

### Build Steps

```bash
git clone https://github.com/ResoluteStudios/UnstableSMP.git
cd UnstableSMP
mvn clean package
```

The compiled JAR will be in `target/UnstableSMP-X.X.X.jar`

## Troubleshooting Installation

### Plugin doesn't load
- Check that the JAR file is in the correct `plugins` folder
- Verify your server version is 1.21+
- Check console for error messages

### Config file not generating
- Delete the `UnstableSMP` folder in `plugins/`
- Restart the server
- The config will regenerate with default values

### Database errors
- Ensure `plugins/UnstableSMP/` folder has write permissions
- Check console logs for SQLite errors

For more help, see [Troubleshooting](Troubleshooting).
