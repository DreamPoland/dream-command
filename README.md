# Dream-Command (2.0)

An advanced annotation-based command framework for simple command creation.


## Platforms

- Bukkit/Spigot/Paper - [bukkit](https://github.com/DreamPoland/dream-command/tree/master/bukkit)
- Bungee/Waterfall - [bungee](https://github.com/DreamPoland/dream-command/tree/master/bungee)

### Warning
Bukkit/Bungee modules require this method on startup:

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
  <version>2.0.15</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.command:{platform}:2.0.15"
```

## Example

```java
// makes all executors async, without annotation is sync
@Async
// adds permission requirement to all executors
@Permission("example.permission.base")
// adds console/client requirement to all executors
@Sender(DreamSender.Type.CLIENT)
// provide command context (label/name, aliases) using annotation @Command
// description is used by custom help/usage-builder
@Command(name = "example", description = "Example command.")
public class ExampleCommand implements CommandBase {

    // makes this executor async
    @Async
    // adds permission requirement to this executor
    @Permission("example.permission.executor")
    // adds console/client requirement to this executor
    @Sender(DreamSender.Type.CLIENT)
    // empty executor without arguments
    // input: /example
    //
    // input must be empty, in the otherwise
    // library will print usage list
    @Executor(description = "Empty executor.")
    void empty() {
        System.out.println("empty call");
    }

    // executor with provided path pattern
    // input: /example special
    //
    // path pattern is non-changeable by input-call
    // can only be executed if it starts with this 'special' argument
    @Executor(path = "special", description = "Executor with path pattern.")
    void special_empty() {
        System.out.println("empty call but called by pattern");
    }

    // executor with custom argument
    // input: /example <name>
    //
    // required argument can be provided by using @Arg annotation
    //
    // TIP: '/example special' has priority to exec,
    // when path 'special' is provided in another executor.
    @Executor(description = "Executor with argument.")
    void single_argument(@Arg String name) {
        System.out.println("Hello, " + name);
    }

    // executor with custom arguments
    // input: /example <name> <age>
    //
    // params with @Arg annotation can be transformed to every object
    // for example : input-arg -> string/int/long/etc
    // by registering object-transformer class in command-provider
    @Executor(description = "Executor with argument.")
    void double_argument(@Arg String name, @Arg int age) {
        System.out.println("Hello, " + name);

        if (age >= 18) {
            System.out.println("You're adult!");
        }
    }

    // executor with binds
    // input: /executor sender
    //
    // bind is a class resolved from command-sender instance
    // provided without any command annotation in method params
    //
    // executor can bind Server class instance from sender.getServer(), or Player from object-cast
    // (object-cast - for example: cow.class -> entity.class)
    // by registering bind-resolver class in command-provider
    @Executor(path = "sender", description = "Executor to send message to sender.")
    void bind(TestSender testSender, Server server) {
        testSender.sendMessage("You're on " + server.getName());
    }

    // executor with bind and argument with suggestion
    // input: /executor <name>
    //
    // tab-competition/suggestion can be provided by using @Completion annotation
    // value will suggest you options provided in annotation
    // or can be used by special keyword, for example: @all-players
    //
    // keyword @all-players is provided by suggestion-supplier class,
    // registered in command-provider instance
    @Completion(arg = "playerName", value = {"mia", "vans", "rookie"})
    @Completion(arg = "randomName", value = "@all-players")
    @Executor(description = "Executor to send warn-message to sender.")
    void bind_with_args(TestSender testSender, @Arg String playerName, @Arg String randomName) {
        testSender.sendMessage(playerName + " was looking at you.");
        testSender.sendMessage(randomName + " can help you from player-list.");
    }

    // executor with multi-arguments
    // input: /executor broadcast <message>
    //
    // long message can be stored in array by using @Args annotation
    // can be setup by min and max values.
    //
    // also @Args array-value can be transformed to every object
    // by registering array-transformer class in command-provider
    //
    // by default, @Args is storing every-arguments
    // for example: ["broadcast", "arg1", "arg2"...]
    // but it can be trimmed by min = 1 (or max): ["arg1", "arg2"...]
    @Executor(path = "broadcast", description = "Broadcast message")
    void broadcast(@Args(min = 1) String[] arguments) {
        final String message = StringUtil.join(arguments, " ");
        System.out.println("[BROADCAST] " + message);
    }

    // executor with optional-arguments
    // input: /executor opt <player> (gamemode)
    //
    // optional argument can be provided by using @OptArg annotation
    // it is recommended to put these annotation at the end of @Arg annotations
    //
    // it can be optional by setting Optional<Gamemode>
    // and adding generic: @OptArg(generic = Gamemode.class)
    @Executor(path = "opt", description = "Executor to registry optional value.")
    void optional_value(@Arg Player player, @OptArg Gamemode gamemode) {
        player.setGamemode(gamemode == null ? Gamemode.SURVIVAL : gamemode);
    }

    // executor with enum
    // input: /executor type <enum>
    //
    // enum arguments are not requiring special transformer
    // they're transforming by self to enum that you're requiring
    //
    // by adding @Completion(arg = "exampleEnum", value = "@enum")
    // library will suggest you all enum fields (collecting enum.name())
    //
    // and by @CompletionFilter you can filter values like streams
    // filter annotation requires SuggestionFilter registry in command-provider
    @Executor(path = "type")
    @Completion(arg = "exampleEnum", value = "@enum", filter = @CompletionFilter(name = "limit", value = "5"))
    void suggestEnum(@Arg ExampleEnum exampleEnum) {
        System.out.println(exampleEnum);
    }
}
```