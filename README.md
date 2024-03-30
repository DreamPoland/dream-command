# Dream-Command (2.0)

Simple command library for easy command creations.


## Platforms

- Bukkit/Spigot/Paper - [bukkit](https://github.com/DreamPoland/dream-command/tree/master/bukkit)

### Warning
Bukkit module require that method: (on-enable)

`BukkitCommandProvider.create(this)`

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
  <version>2.0-beta.2</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.command:{platform}:2.0-beta.2"
```

## Example

```java
@Permission(name = "example.permission.base")
@Command(name = "example", description = "Example command.")
public class ExampleCommand implements CommandBase {

    @Executor(path = "param", description = "Example method usage.")
    @Sender(type = DreamSender.Type.CLIENT)
    @Permission(name = "example.permission.param")
    @Completion(arg = "test", value = {"kolo", "kofo", "nanana"})
    @Completion(arg = "test2", value = "@all-players", filter = @CompletionFilter(name = "limit", value = "5"))
    public void optionalMethod(
            @Arg(name = "test") String test,
            @Arg(name = "test2") String test2,
            @OptArg(name = "optional-test") String optionalTest,
            @Args(name = "argsmen", min = 1, max = 5) String[] args
    ) {
        // default arg
        System.out.println(test);

        // optional arg
        System.out.println("OPTIONAL - " + optionalTest);

        // many args
        System.out.println(Arrays.toString(args));
    }
}
```