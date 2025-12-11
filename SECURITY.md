# Security Policy

## Reporting a Vulnerability

We take security seriously. If you discover a security vulnerability in UnstableSMP, please report it responsibly by emailing us directly instead of using the public issue tracker.

### How to Report

Email your security report to: **resolutestudios@proton.me**

**Please include the following information:**

1. **Description of the vulnerability** - Clear explanation of the security issue
2. **Affected versions** - Which plugin versions are affected
3. **Steps to reproduce** - Detailed steps to reproduce the vulnerability
4. **Impact assessment** - What could an attacker do with this vulnerability?
5. **Suggested fix** (optional) - If you have a solution, share it

### Example Report Template

```
Subject: [SECURITY] UnstableSMP Vulnerability Report

Plugin Version: v1.2.1
Vulnerability Type: [e.g., SQL Injection, Command Injection, etc.]

Description:
[Detailed description of the vulnerability]

Steps to Reproduce:
1. [Step 1]
2. [Step 2]
3. [Step 3]

Impact:
[Explain the potential impact]

Suggested Fix (Optional):
[If you have a solution]
```

## Security Response Process

1. **Initial Response** - We will acknowledge your report within 48 hours
2. **Investigation** - Our team will investigate and assess the severity
3. **Fix Development** - We will develop and test a fix
4. **Release** - A patched version will be released as soon as possible
5. **Credit** - We will credit you in the release notes (unless you prefer anonymity)

## Vulnerability Timeline

- **Critical (CVSS 9-10)** - Patched within 24-48 hours
- **High (CVSS 7-8.9)** - Patched within 1 week
- **Medium (CVSS 4-6.9)** - Patched within 2 weeks
- **Low (CVSS 0-3.9)** - Fixed in next regular release

## Security Best Practices for Users

### For Server Administrators

1. **Keep the plugin updated** - Always use the latest version
2. **Restrict permissions** - Only give `unstablesmp.admin` to trusted players
3. **Use strong server authentication** - Implement Mojang authentication
4. **Monitor logs** - Regularly check server logs for suspicious activity
5. **Backup data** - Maintain regular backups of `plugins/UnstableSMP/` directory

### Recommended Permission Configuration

```yaml
# Only trusted admins should have these permissions
unstablesmp.admin: false (default)
unstablesmp.disguise: false (default)
unstablesmp.skiprp: false (by request)
```

### Resource Pack Security

- **HTTPS Only** - Always host resource packs over HTTPS
- **Verify SHA1** - Keep SHA1 hash updated in config
- **File Permissions** - Restrict access to resource pack files
- **Scan Files** - Scan packs for malicious content before hosting

## Known Security Considerations

### Player Disguise Feature

- **UUID Preservation** - The `/disguise` command changes appearance only, not identity
- **Admin-Only Command** - Requires `unstablesmp.admin` permission
- **Skin Data Validation** - All skins validated through official Mojang API

### Database Security

- **Local Storage** - Player data stored locally in SQLite (not cloud)
- **File Permissions** - Database file should have restricted permissions
- **Backup Regularly** - Store backups in secure location

### Resource Pack Delivery

- **SHA1 Verification** - Integrity check prevents tampering
- **HTTPS Required** - Use encrypted connections for downloads
- **CDN Recommended** - Use reputable CDN for distribution

## Dependencies Security

UnstableSMP uses the following key dependencies:

- **Paper API** - Latest stable version
- **Adventure API** - Latest stable version
- **SQLite JDBC** - Latest stable version

We regularly check for CVE vulnerabilities in all dependencies using:
- Maven dependency checks
- GitHub Dependabot
- Manual security audits

## Responsible Disclosure

We practice responsible vulnerability disclosure:

1. **No public disclosure** until fix is released
2. **Coordinated disclosure** with security researchers
3. **Credit given** to responsible reporters
4. **Embargo period** to allow time for patching

## Security Updates

### Auto-Update Feature

- Updates checked every 10 minutes
- Automatic download and installation enabled
- Admins notified of available updates
- Can be disabled in config

### Staying Updated

Always use the latest version to ensure you have the latest security patches:

```
/unstable version
```

Check the [GitHub Releases](https://github.com/ResoluteStudios/UnstableSMP/releases) page for security advisories.

## Security Audit

UnstableSMP is:

- Open-source and auditable
- Built on trusted frameworks (Paper, Adventure)
- Using industry-standard libraries
- Regularly tested for vulnerabilities

## Contact & Questions

For security questions or concerns, please email: **resolutestudios@proton.me**

**Do not:**
- Open public GitHub issues for security vulnerabilities
- Share vulnerability details publicly
- Contact via other channels for security issues

## Credits

We appreciate the security community's efforts to keep UnstableSMP safe. Security researchers who responsibly disclose vulnerabilities may be credited in release notes.

## Legal

By reporting a security vulnerability, you agree to:

1. Not publicly disclose the vulnerability until a fix is released
2. Not attempt to access data beyond the scope of the reported vulnerability
3. Not perform any testing on production systems without permission

## Changelog & Security Updates

All security-related updates are documented in [CHANGELOG](patchnotes.md).

---

**Last Updated:** December 2025

**Current Stable Version:** v1.2.1

For the latest information, visit [GitHub](https://github.com/ResoluteStudios/UnstableSMP)
