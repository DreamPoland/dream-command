# Dream-Command

[![Build](https://github.com/DreamPoland/dream-command/actions/workflows/gradle.yml/badge.svg?branch=2.x)](https://github.com/DreamPoland/dream-command/actions/workflows/gradle.yml)
[![Version](https://img.shields.io/badge/version-2.4.1-blue.svg)](https://repo.dreamcode.cc/#/releases/cc/dreamcode/command)
[![Java](https://img.shields.io/badge/java-21%2B-orange.svg)](https://www.oracle.com/java/)

Modern, lightweight, and thread-safe annotation-based Minecraft command framework for **Bukkit / Spigot / Paper** and **BungeeCord / Waterfall**.

---

## Features

- **Annotation-Driven Architecture**: Eliminate boilerplate with `@Command`, `@Executor`, `@Arg`, `@OptArg`, `@Args`, `@Permission`, `@Sender`, `@Async`, and `@Completion`.
- **Multi-Platform Support**:
  - **Paper / Spigot / Bukkit**: Native integration with Paper/Spigot command map and scheduler.
  - **BungeeCord / Waterfall**: Native proxy tab-executor and async execution support.
- **Automatic Type Resolution (Transformers)**:
  - Primitive and boxed types (`int`, `double`, `boolean`, `long`, `float`, `byte`, `short`, `char`).
  - Arrays and variadic arguments (e.g. `@Args int[]`, `@Args String[]`).
  - Enums (automatic reflection resolution and case-insensitive matching).
  - Platform objects: `Player`, `OfflinePlayer`, `World`, `ProxiedPlayer`, `Server`.
- **Contextual Dependency Injection (Binds)**: Direct parameter access to `Player`, `CommandSender`, `ConsoleCommandSender`, `Server`, etc.
- **Smart Tab Completion**:
  - Static lists, keywords (`@all-players`, `@offline-players`, `@enum`), and stream-like filters (`@CompletionFilter`).
- **Thread-Safety & Async Execution**:
  - Safe concurrent structures (`ConcurrentHashMap`, `CopyOnWriteArrayList`) with native `@Async` scheduler offloading.
- **Built-in Error Handlers**:
  - Out-of-the-box user-friendly error messages for missing permissions, invalid senders, malformed arguments, and usage help. Fully customizable via handler interfaces.

---

## Requirements

- **Java 21** or newer.
- **Paper / Spigot / Bukkit** 1.20+ or **BungeeCord** 1.20+.

> [!IMPORTANT]
> To preserve method parameter names for command suggestions and usage messages, make sure to add the `-parameters` compiler flag to your build configuration.

---

## Installation

### Maven

Add the repository:
```xml
<repositories>
  <repository>
    <id>dreamcode-repository-releases</id>
    <url>https://repo.dreamcode.cc/releases</url>
  </repository>
</repositories>
```

Add the dependency for your platform:

**Bukkit / Spigot / Paper:**
```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>bukkit</artifactId>
  <version>2.4.1</version>
</dependency>
```

**BungeeCord:**
```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>bungee</artifactId>
  <version>2.4.1</version>
</dependency>
```

Compiler configuration:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.11.0</version>
      <configuration>
        <parameters>true</parameters>
      </configuration>
    </plugin>
  </plugins>
</build>
```

---

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.dreamcode.cc/releases")
}

dependencies {
    // Bukkit / Spigot / Paper
    implementation("cc.dreamcode.command:bukkit:2.4.1")

    // BungeeCord
    // implementation("cc.dreamcode.command:bungee:2.4.1")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
```

---

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url "https://repo.dreamcode.cc/releases" }
}

dependencies {
    // Bukkit / Spigot / Paper
    implementation "cc.dreamcode.command:bukkit:2.4.1"

    // BungeeCord
    // implementation "cc.dreamcode.command:bungee:2.4.1"
}

compileJava {
    options.compilerArgs << "-parameters"
}
```

---

## Usage

### 1. Initializing the Provider

#### On Bukkit / Spigot / Paper:
```java
public class MyPlugin extends JavaPlugin {

    private BukkitCommandProvider commandProvider;

    @Override
    public void onEnable() {
        this.commandProvider = BukkitCommandProvider.create(this);

        // Register commands
        this.commandProvider.register(new TeleportCommand());
    }
}
```

#### On BungeeCord:
```java
public class MyProxyPlugin extends Plugin {

    private BungeeCommandProvider commandProvider;

    @Override
    public void onEnable() {
        this.commandProvider = BungeeCommandProvider.create(this);

        // Register commands
        this.commandProvider.register(new TeleportCommand());
    }
}
```

---

### 2. Basic Command & Subcommands

Create a command class by implementing `CommandBase` and annotating it with `@Command`:

```java
@Command(name = "teleport", aliases = {"tp"}, description = "Teleportation system")
@Permission("plugin.teleport")
public class TeleportCommand implements CommandBase {

    // Matches: /tp <target> (player only)
    @Sender(DreamSender.Type.CLIENT)
    @Executor(description = "Teleport to another player")
    void tpToPlayer(Player player, @Arg(value = "target") Player target) {
        player.teleport(target.getLocation());
        player.sendMessage("Teleported to " + target.getName() + "!");
    }

    // Matches: /tp pos <x> <y> <z>
    @Sender(DreamSender.Type.CLIENT)
    @Executor(path = "pos", description = "Teleport to coordinates")
    void tpToCoords(Player player, @Arg double x, @Arg double y, @Arg double z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
        player.sendMessage("Teleported to coordinates!");
    }
}
```

---

### 3. Argument Handling (`@Arg`, `@OptArg`, `@Args`)

```java
@Command(name = "money", description = "Economy management")
public class MoneyCommand implements CommandBase {

    // Matches: /money pay <target> <amount> [reason]
    @Executor(path = "pay", description = "Send money to a player")
    void pay(
        Player sender,
        @Arg(value = "target") Player target,
        @Arg(value = "amount") double amount,
        @OptArg(value = "reason") Optional<String> reason
    ) {
        sender.sendMessage("Sent " + amount + "$ to " + target.getName() +
            reason.map(r -> " for: " + r).orElse(""));
    }

    // Matches: /money addmany <amounts...> (e.g. /money addmany 10 20 30 50)
    @Executor(path = "addmany", description = "Sum and deposit multiple values")
    void addMany(CommandSender sender, @Args int[] amounts) {
        int total = Arrays.stream(amounts).sum();
        sender.sendMessage("Total amount: " + total);
    }
}
```

---

### 4. Tab Completion & Suggestions (`@Completion`)

```java
public enum Rank {
    VIP, SVIP, ELITE, MODERATOR, ADMIN
}

@Command(name = "rank", description = "Manage player ranks")
public class RankCommand implements CommandBase {

    // Uses the built-in @enum keyword to suggest enum constants with a limit filter
    @Completion(arg = "rank", value = "@enum", filter = @CompletionFilter(name = "limit", value = "3"))
    @Executor(path = "set", description = "Set a rank for a player")
    void setRank(CommandSender sender, @Arg Player target, @Arg Rank rank) {
        sender.sendMessage("Rank " + rank.name() + " assigned to " + target.getName());
    }

    // Uses built-in @all-players keyword to suggest online players
    @Completion(arg = "playerName", value = "@all-players")
    @Executor(path = "check", description = "Check rank of a player")
    void checkRank(CommandSender sender, @Arg String playerName) {
        sender.sendMessage("Checking rank for: " + playerName);
    }
}
```

---

### 5. Permissions, Senders & Async Execution

```java
// Execute all executors in this class asynchronously
@Async
@Command(name = "user", description = "User information lookup")
public class UserCommand implements CommandBase {

    // Enforces permission check and console-only sender type
    @Permission("admin.audit")
    @Sender(DreamSender.Type.CONSOLE)
    @Executor(path = "audit", description = "Audit user database")
    void audit(ConsoleCommandSender sender, @Arg String username) {
        // Runs on an async thread safely
        Database.audit(username);
        sender.sendMessage("Audit complete for: " + username);
    }
}
```

---

### 6. Custom Transformers & Error Handlers

#### Custom Object Transformer:
```java
commandProvider.registerTransformer(new ObjectTransformer<Guild>() {
    @Override
    public Class<?> getGeneric() {
        return Guild.class;
    }

    @Override
    public Optional<Guild> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.ofNullable(GuildManager.findGuild(input));
    }
});
```

#### Custom Error Messages:
```java
// Custom permission denied message
commandProvider.setInvalidPermissionHandler((sender, permission) -> {
    sender.sendMessage("Access denied! Required permission: " + permission);
});

// Custom invalid usage message
commandProvider.setInvalidUsageHandler((sender, commandMeta, commandInput) -> {
    sender.sendMessage("Invalid usage. Type /" + commandInput.getLabel() + " help for details.");
});
```

---

## License

This project is licensed under the [MIT License](LICENSE).
