# dream-command
Bukkit/Bungee command library for easy command creations with tab-complete support.

```xml
<repository>
  <id>dreamcode-repository-releases</id>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```

```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

### Core:
```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>core</artifactId>
  <version>1.4.17</version>
</dependency>
```

```groovy
implementation "cc.dreamcode.command:core:1.4.17"
```

### Supported platforms:
- [Bukkit](https://github.com/DreamPoland/dream-command/tree/master/bukkit)
- [Bungee](https://github.com/DreamPoland/dream-command/tree/master/bungee)

```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>{platform}</artifactId>
  <version>1.4.17</version>
</dependency>
```
```groovy
implementation "cc.dreamcode.command:{platform}:1.4.17"
```

For project content, open project modules and see the contents of the classes. (todo)
