# Dream-Command
An advanced framework for simple command creation.

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
  <version>2.0-beta.3</version>
</dependency>
```
```groovy
implementation "cc.dreamcode.command:core:2.0-beta.3"
```

### Supported platforms:
- [Bukkit](https://github.com/DreamPoland/dream-command/tree/2.x/bukkit)
- [Bungee](https://github.com/DreamPoland/dream-command/tree/2.x/bungee)

```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>{platform}</artifactId>
  <version>2.0-beta.3</version>
</dependency>
```
```groovy
implementation "cc.dreamcode.command:{platform}:2.0-beta.3"
```

### Example command class
```java
@Permission(name = "simple.command")
@Command(label = "simple", description = "Simple command.")
public class SimpleCommand extends DreamCommandExecutor {
    @Path
    void simpleHelp(TestSender sender) {
        sender.sendMessage("Brak argumentow!");
    }

    @Path(name = "dirt")
    void simpleBlock(TestSender sender) {
        sender.sendMessage("Dirt!");
    }

    @Path(name = "player")
    void simplePlayerInput(TestSender sender, @Arg(name = "name") String playerName) {
        sender.sendMessage("[" + sender.getName() + "] Player name: " + playerName);
    }

    @Path(name = "broadcast")
    void simpleBroadcast(TestSender sender, @Args(name = "message") String message) {
        sender.sendMessage("[BROADCAST] " + message);
    }

    @Permission(name = "simple.answer")
    @Path(name = "answer")
    void simpleAnswer(TestSender sender, @Arg(name = "answer") Answer answer) {
        sender.sendMessage("You answered: " + answer);
    }

    @RequireSender(type = SenderType.PLAYER)
    @Path(name = "words")
    void simpleWords(TestSender sender, @Args(name = "words") String[] words) {
        for (int index = 0; index < words.length; index++) {
            final String word = words[index];
            
            sender.sendMessage("Word number " + index + ": " + word);
        }
    }
}
```

### Other projects:
- [Dream-Platform](https://github.com/DreamPoland/dream-platform)
- [Dream-Menu](https://github.com/DreamPoland/dream-menu)
- [Dream-Notice](https://github.com/DreamPoland/dream-notice)
- [Dream-Utilities](https://github.com/DreamPoland/dream-utilities)