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
Note: Please make Retro Commands optional!!

build.gradle
```gradle
repositories {
    maven {
        name = "Jitpack"
        url "https://jitpack.io/"
    }
}

dependencies {
    modImplementation('com.github.matthewperiut:retrocommands:0.5.3') {
        transitive false
    }
}
```

fabric.mod.json
```json
  "suggests": {
    "retrocommands": "*"
  },
```

in your mods initialization
```java
public static void init_of_some_sort()
{
    if (FabricLoader.getInstance().isModLoaded("retrocommands")){
        MyModsCommands.add();
    }
}
```

implement `api.com.periut.retrocommands.Command`  

register with `api.com.periut.retrocommands.CommandRegistry`  
`CommandRegistry.add(new Command())`  

new Command() replaced with your custom command.  

### Add your own summon command for your entities

Use `api.com.periut.retrocommands.SummonRegistry`  
SummonRegistry.add(...)  


Examples from `util.com.periut.retrocommands.VanillaMobs`
```java
SummonRegistry.add(Creeper.class, (world, pos, param) -> {
    Creeper creeper = new Creeper(world);

    if (param.length > 5)
        if (!param[5].isEmpty())
            if (param[5].charAt(0) != '0')
                ((EntityAccessor) creeper).getDataTracker().setInt(17, (byte) 1);

    return creeper;
}, "{charged (0 or 1)}");

SummonRegistry.add(Sheep.class, (world, pos, param) -> {
    int color = Integer.parseInt(param[5]);
    int has_wool = 1;
    if (param.length > 6)
        has_wool = Integer.parseInt(param[6]);
    Sheep sheep = new Sheep(world);
    sheep.setSheared(has_wool == 0);
    sheep.setColour(color);
    return sheep;
}, "{wool color meta} {has wool (0/1)} ");
```

# Configuration
**Cryonic Config** is required for this feature.  
__This file generates when using a command for the first time__  
To disable commands go to `{mc_dir}/config/retrocommands.json` and in the json under "disabledCommands", set the value to commands you want to disable separated by commas  
Examples:
```json
{
  "disabledCommands": "help,give,hat"
}
```
```json
{
  "disabledCommands": "hat"
}
```