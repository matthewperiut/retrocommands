# Retro Commands for b1.7.3
 + it works on servers
 + it has an extensible API.
 + No dependencies required

## Optional Dependencies
 + To access /tp with dimensions, install STAPI
 + To access /gamemode, install BHCreative

## Help
+ Use `/help` in-game

## API
Note: Please make SPC optional!!

build.gradle
```gradle
repositories {
    maven {
        name = "Jitpack"
        url "https://jitpack.io/"
    }
}

dependencies {
    modImplementation('com.github.matthewperiut:retro-commands:0.5.1') {
        transitive false
    }
}
```

fabric.mod.json
```json
  "suggests": {
    "spc": "*"
  },
```

in your mods initialization
```java
public static void init_of_some_sort()
{
    if (FabricLoader.getInstance().isModLoaded("spc")){
        MyModsCommands.add();
    }
}
```

implement `com.matthewperiut.api.Command`  

register with `com.matthewperiut.api.CommandRegistry`  
`CommandRegistry.add(new Command())`  

new Command() replaced with your custom command.  

### Add your own summon command for your entities

Use `com.matthewperiut.api.SummonRegistry`  
SummonRegistry.add(...)  


Examples from `com.matthewperiut.spc.util.VanillaMobs`
```java
SummonRegistry.add(Creeper.class, (level, pos, param) -> {
    Creeper creeper = new Creeper(level);

    if (param.length > 5)
        if (!param[5].isEmpty())
            if (param[5].charAt(0) != '0')
                ((EntityAccessor) creeper).getDataTracker().setInt(17, (byte) 1);

    return creeper;
}, "{charged (0 or 1)}");

SummonRegistry.add(Sheep.class, (level, pos, param) -> {
    int color = Integer.parseInt(param[5]);
    int has_wool = 1;
    if (param.length > 6)
        has_wool = Integer.parseInt(param[6]);
    Sheep sheep = new Sheep(level);
    sheep.setSheared(has_wool == 0);
    sheep.setColour(color);
    return sheep;
}, "{wool color meta} {has wool (0/1)} ");
```