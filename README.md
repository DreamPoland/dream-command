# Dream-Command (2.x)

[![Build](https://github.com/DreamPoland/dream-command/actions/workflows/gradle.yml/badge.svg?branch=2.x)](https://github.com/DreamPoland/dream-command/actions/workflows/gradle.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 21](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://adoptium.net/)

**Dream-Command** to zaawansowana, lekka i bezpieczna wątkowo biblioteka ułatwiająca tworzenie komend w oparciu o adnotacje dla platform **Bukkit/Spigot/Paper** oraz **BungeeCord/Waterfall**.

---

## 🚀 Główne możliwości

- **Adnotacje zamiast boilerplate'u:** Twórz komendy za pomocą `@Command`, `@Executor`, `@Arg`, `@OptArg`, `@Args`, `@Permission`, `@Sender`, `@Async`, `@Completion`.
- **Wieloplatformowość:** Gotowe moduły dla platform **Bukkit** (Spigot, Paper, Purpur) oraz **BungeeCord** (Waterfall).
- **Automatyczna konwersja typów (Transformers):**
  - Typy proste oraz ich obiektowe odpowiedniki (`int`, `double`, `boolean`, `long`, `float`, `byte`, `short`, `char`).
  - Tablice jedno- i wieloargumentowe (np. `@Args int[]`, `@Args String[]`).
  - Dowolne Enumy (automatyczne dopasowywanie i tab-complete bez dodatkowego kodu).
  - Obiekty platformowe: `Player`, `OfflinePlayer`, `World`, `ProxiedPlayer`, `Server`.
- **Wstrzykiwanie kontekstu (Binds):** Bezpośredni dostęp w sygnaturze metody do nadawcy: `Player`, `CommandSender`, `ConsoleCommandSender`, `Server` itp.
- **Inteligentne auto-uzupełnianie (Tab Completion):** Statyczne listy podpowiedzi, słowa kluczowe (`@all-players`, `@offline-players`, `@enum`) oraz filtry (np. limitowanie wyników).
- **Bezpieczeństwo wielowątkowe (Thread-Safety):** Zoptymalizowane kolekcje współbieżne (`ConcurrentHashMap`, `CopyOnWriteArrayList`) z pełnym wsparciem komend asynchronicznych (`@Async`).
- **Gotowa obsługa błędów:** Domyślne, czytelne dla graczy komunikaty przy braku uprawnień, błędnych argumentach czy niepoprawnym typie nadawcy – z możliwością pełnej personalizacji.

---

## 📦 Instalacja

Wymagane środowisko: **Java 21** lub nowsza.

> [!IMPORTANT]
> Aby zachować nazwy parametrów metod w podpowiedziach komend, dodaj flagę kompilatora `-parameters` do konfiguracji `compileJava` w swoim projekcie.

### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
    maven("https://repo.dreamcode.cc/releases")
}

dependencies {
    // Wybierz odpowiednią platformę: bukkit lub bungee
    implementation("cc.dreamcode.command:bukkit:2.3.0")
    // implementation("cc.dreamcode.command:bungee:2.3.0")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
```

### Gradle (Groovy)
```groovy
repositories {
    mavenCentral()
    maven { url "https://repo.dreamcode.cc/releases" }
}

dependencies {
    implementation "cc.dreamcode.command:bukkit:2.3.0"
    // implementation "cc.dreamcode.command:bungee:2.3.0"
}

compileJava {
    options.compilerArgs << "-parameters"
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>dreamcode-repository-releases</id>
        <url>https://repo.dreamcode.cc/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>cc.dreamcode.command</groupId>
        <artifactId>bukkit</artifactId> <!-- lub bungee -->
        <version>2.3.0</version>
    </dependency>
</dependencies>

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

## ⚙️ Inicjalizacja

Zainicjalizuj provider w metodzie `onEnable()` swojego pluginu:

### Bukkit / Paper
```java
public class MyPlugin extends JavaPlugin {

    private BukkitCommandProvider commandProvider;

    @Override
    public void onEnable() {
        this.commandProvider = BukkitCommandProvider.create(this);

        // Rejestracja komendy
        this.commandProvider.register(new ExampleCommand());
    }
}
```

### BungeeCord
```java
public class MyProxyPlugin extends Plugin {

    private BungeeCommandProvider commandProvider;

    @Override
    public void onEnable() {
        this.commandProvider = BungeeCommandProvider.create(this);

        // Rejestracja komendy
        this.commandProvider.register(new ExampleCommand());
    }
}
```

---

## 💡 Przykłady użycia

### 1. Podstawowa komenda z podkomendami i uprawnieniami

```java
@Command(name = "teleport", aliases = {"tp"}, description = "System teleportacji")
@Permission("plugin.teleport")
public class TeleportCommand implements CommandBase {

    // Wywołanie: /tp <gracz> (tylko dla gracza, nie dla konsoli)
    @Sender(DreamSender.Type.CLIENT)
    @Executor(description = "Teleportuje do wskazanego gracza")
    void tpToPlayer(Player player, @Arg(value = "cel") Player target) {
        player.teleport(target.getLocation());
        player.sendMessage("§aPrzeteleportowano do " + target.getName() + "!");
    }

    // Wywołanie: /tp pos <x> <y> <z>
    @Sender(DreamSender.Type.CLIENT)
    @Executor(path = "pos", description = "Teleportuje na podane koordynaty")
    void tpToCoords(Player player, @Arg double x, @Arg double y, @Arg double z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
        player.sendMessage("§aPrzeteleportowano na koordynaty!");
    }
}
```

---

### 2. Opcjonalne argumenty (`@OptArg`) oraz tablice (`@Args`)

```java
@Command(name = "money", description = "Zarządzanie stanem konta")
public class MoneyCommand implements CommandBase {

    // Wywołanie: /money pay <gracz> <kwota> [powód]
    @Executor(path = "pay", description = "Przelewa środki innemu graczowi")
    void pay(
        Player sender,
        @Arg(value = "odbiorca") Player target,
        @Arg(value = "kwota") double amount,
        @OptArg(value = "powód") Optional<String> reason
    ) {
        sender.sendMessage("Wysłano " + amount + "$ do " + target.getName() +
            reason.map(r -> " z powodem: " + r).orElse(""));
    }

    // Wywołanie: /money addmany <kwoty...> (np. /money addmany 10 20 30 50)
    @Executor(path = "addmany", description = "Sumuje i dodaje tablicę kwot")
    void addMany(CommandSender sender, @Args int[] amounts) {
        int sum = Arrays.stream(amounts).sum();
        sender.sendMessage("Suma wprowadzonych kwot: " + sum);
    }
}
```

---

### 3. Komendy asynchroniczne (`@Async`) i automatyczny Tab-Complete

```java
@Async
@Command(name = "user", description = "Informacje o użytkowniku")
public class UserCommand implements CommandBase {

    // Wywołanie: /user lookup <nick>
    // Tab-complete automatycznie podpowiada listę graczy online
    @Completion(arg = "target", value = "@all-players")
    @Executor(path = "lookup", description = "Sprawdza dane gracza w bazie danych")
    void lookup(CommandSender sender, @Arg(value = "target") String target) {
        // Ta metoda wykonuje się asynchronicznie poza głównym wątkiem serwera!
        String status = Database.queryPlayerStatus(target);
        sender.sendMessage("Status gracza " + target + ": " + status);
    }
}
```

---

### 4. Enumy i filtry podpowiedzi (`@CompletionFilter`)

```java
public enum Rank {
    VIP, SVIP, ELITE, MODERATOR, ADMIN
}

@Command(name = "rank", description = "Zarządzanie rangami")
public class RankCommand implements CommandBase {

    // Słowo kluczowe @enum automatycznie podpowiada wszystkie wartości enuma Rank
    @Completion(arg = "rank", value = "@enum", filter = @CompletionFilter(name = "limit", value = "3"))
    @Executor(path = "set", description = "Nadaje rangę graczowi")
    void setRank(CommandSender sender, @Arg Player target, @Arg Rank rank) {
        sender.sendMessage("Nadano rangę " + rank.name() + " dla " + target.getName());
    }
}
```

---

### 5. Zwracanie wartości i Result Resolvers

Metody wykonawcze mogą zwracać obiekty, które są automatycznie przetwarzane przez zarejestrowany `ResultResolver`:

```java
@Command(name = "ping", description = "Sprawdza status serwera")
public class PingCommand implements CommandBase {

    @Executor
    String ping() {
        return "§aPong!"; // String jest automatycznie wysyłany do nadawcy komendy
    }
}
```

---

## 🛠️ Własne konwertery i handlery błędów

### Rejestracja własnego typu (ObjectTransformer)
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

### Konfiguracja własnych komunikatów błędów
```java
// Własny komunikat braku uprawnień
commandProvider.setInvalidPermissionHandler((sender, permission) -> {
    sender.sendMessage("§cOdmowa dostępu! Wymagane uprawnienie: " + permission);
});

// Własna pomoc przy błędnym użyciu
commandProvider.setInvalidUsageHandler((sender, commandMeta, commandInput) -> {
    sender.sendMessage("§eNieprawidłowe użycie. Wpisz §f/" + commandInput.getLabel() + " help §eaby uzyskać pomoc.");
});
```

---

## 📄 Licencja

Projekt jest udostępniany na licencji [MIT](LICENSE).
