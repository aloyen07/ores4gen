# Ores4Gen

Ores4Gen is a Minecraft mod that adds ores in water-lava generators (also known as cobblestone generators). 
This mod is designed to enhance the gameplay experience by providing players with additional resources that 
can be mined directly from these generators

## Supported generator types

1. Water + Lava = Cobblestone Generator
2. Still Water + Lava = Stone Generator
3. Water + Lava + Blue Ice + Soul Soil = Basalt Generator
4. Lava source + Water = Obsidean Generator

## Features

- Lightweight and easy to install.
- Configurable ore generation: Users can customize which ores spawn in the generators.
- Very configurable: Adjust spawn rates, types of ores, and other parameters via a config file.
- Compatible with popular modpacks.

## Configuration

Config file can be found in the `config/ores4gen.json` file.

By default, this config contains this:
```json
{
  "enabled": true,
  "cobblestone_generator": {
    "enabled": true,
    "default_block": "minecraft:cobblestone",
    "chances": [
      {
        "block": "minecraft:cobblestone",
        "chance": 98.0
      },
      {
        "block": "minecraft:coal_ore",
        "chance": 1.0
      },
      {
        "block": "minecraft:iron_ore",
        "chance": 0.5
      },
      {
        "block": "minecraft:gold_ore",
        "chance": 0.3
      },
      {
        "block": "minecraft:diamond_ore",
        "chance": 0.1
      },
      {
        "block": "minecraft:emerald_ore",
        "chance": 0.1
      }
    ]
  },
  "stone_generator": {
    "enabled": true,
    "default_block": "minecraft:stone",
    "chances": [
      {
        "block": "minecraft:stone",
        "chance": 90.0
      },
      {
        "block": "minecraft:coal_ore",
        "chance": 2.0
      },
      {
        "block": "minecraft:iron_ore",
        "chance": 1.5
      },
      {
        "block": "minecraft:gold_ore",
        "chance": 1.3
      },
      {
        "block": "minecraft:lapis_ore",
        "chance": 1.2
      },
      {
        "block": "minecraft:redstone_ore",
        "chance": 2.0
      },
      {
        "block": "minecraft:diamond_ore",
        "chance": 1.0
      },
      {
        "block": "minecraft:emerald_ore",
        "chance": 1.0
      }
    ]
  },
  "basalt_generator": {
    "enabled": true,
    "default_block": "minecraft:basalt",
    "chances": [
      {
        "block": "minecraft:basalt",
        "chance": 96.5
      },
      {
        "block": "minecraft:blackstone",
        "chance": 2.0
      },
      {
        "block": "minecraft:gilded_blackstone",
        "chance": 1.0
      },
      {
        "block": "minecraft:ancient_debris",
        "chance": 0.5
      }
    ]
  },
  "obsidian_generator": {
    "enabled": true,
    "default_block": "minecraft:obsidian",
    "chances": [
      {
        "block": "minecraft:obsidian",
        "chance": 99.8
      },
      {
        "block": "minecraft:crying_obsidian",
        "chance": 0.2
      }
    ]
  }
}
```

Values:
 - `enabled`: Enable or disable the mod or specific generator types. You can also disable all generators at once.
 - `..._generator`: Configuration for each generator type. Required all generators to be present.
 - `default_block`: The block that will be generated if enabled = false. Required to be a valid block ID.
 - `chances`: An array of objects defining which blocks can spawn and their respective chances. **NOT null and will NON-empty**
 - `block`: The block ID to spawn. Required to be a valid block ID.
 - `chance`: The chance of the block to spawn. Required to be a positive number. Chance **IS NOT A PERCENTAGE**, it is a relative weight compared to other blocks. But for simplicity, we recommend using values that sum equals 100 or 1.

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details.