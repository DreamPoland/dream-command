# Dream-Command (2.0)

Simple command library for easy command creations.


## Platforms

- Bukkit/Spigot/Paper - [bukkit](https://github.com/DreamPoland/dream-command/tree/master/bukkit)
- Bungee/Waterfall - [bungee](https://github.com/DreamPoland/dream-command/tree/master/bungee)

### Warning
Bukkit/Bungee module require that method: (on-enable)

`BukkitCommandProvider.create(this)` or `BungeeCommandProvider.create(this)`

For better support, add `-parameters` flag to your compiler.

## Maven/Gradle

### Maven
```xml
<repository>
  <id>dreamcode-repository-releases</id>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```

```xml
<dependency>
  <groupId>cc.dreamcode.command</groupId>
  <artifactId>{platform}</artifactId>
  <version>2.0-beta.8</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.command:{platform}:2.0-beta.8"
```

## Example

```java
@Permission(name = "example.permission.base")
@Command(name = "example", description = "Example command.")
public class ExampleCommand implements CommandBase {

    @Executor(path = "param", description = "Example method usage.")
    @Sender(type = DreamSender.Type.CLIENT)
    @Permission(name = "example.permission.param")
    @Completion(arg = "arg1", value = {"key1", "value2", "magic"})
    @Completion(arg = "arg2", value = "@all-players", filter = @CompletionFilter(name = "limit", value = "5"))
    void example(
            @Arg String arg1,
            @Arg String arg2,
            @OptArg(name = "optional-arg") String optionalArg,
            @Args(min = 1, max = 5) String[] args
    ) {
        // default arg
        System.out.println(arg);

        // optional arg
        System.out.println("OPTIONAL - " + optionalArg);

        // many args
        System.out.println(Arrays.toString(args));
    }
}
```