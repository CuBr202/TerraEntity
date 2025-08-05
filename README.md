# Terra Entity -- submodule of Confluence: OtherWorld

lang: [中文](./README_zh.md) | [English](./README.md)
___ 
## Mod Contents

- **[Creatures](#-creatures)**
    - [Monsters](#monsters-)
    - [Animals](#animals-)
    - [Bosses](#bosses-)
    - [NPCs](#npcs-)
    - [Summonings](#summonings-)

- **Items**
    - **[Weapons](#-weapons)**
        - [Yoyos](#yoyos-)
        - [Boomerangs](#boomerangs-)
        - [Summoning staffs](#summoning-staffs-)
        - [Whips](#whips-)

    - **[Enchant Books](#-enchant-books)**
        - [Whip Sweep](#whip-sweep)
        - [Multi Boomerang](#multi-boomerang)

    - **[Tools](#-tools)**
        - [House Detector](#house-detector-)
        - [Pets](#pets-)
        - [Rideable](#rideable-)

    - **[Spawn Eggs](#-spawn-eggs)**

- **[Gameplay](#-gameplay)**
  - [NPC System](#npc-system-)
  - [Summoning System](#summoning-system-)
  - [Configuration Files](#configuration-files-)
___

## 🥚 Creatures

### Monsters 😈

<div class="box">
  <img src="images/monster.png" alt="chester" style="max-width: 100%;">
</div>

- Slime 14 variants
- Demon Eye 14 variants
- Bat 5 kinds
- Goblin 7 kinds
- Skeleton 7 kinds
- Worm 3 kinds
- Hornet 
- Fly Fish、Crimson Kemera... 5 kinds
- Giant Shelly 2 variants
- Variant Zombies 4 kinds
- Decayeder
- Antlion Swarmer 2 kinds
- Nymph
- Harpy
- Demon 2 kinds
- Ghost
- Snow Flinx
- Fire Imp
- Bloody Spore
- Blood Crawler
- Cursed Skeleton
- Man Easter、Snatcher
- piranha

### Animals 🐰

<div class="box">
  <img src="images/animal.png" alt="chester" style="max-width: 100%;">
</div>

- Dusk
- Squirrel 2 variants
- Jewel Squirrel 8 variants
- Bunny
- Jewel Bunny 8 variants
- Bird 3 kinds

### Bosses 🤡

<div class="box">
  <img src="images/boss.png" alt="chester" style="max-width: 100%;">
</div>

- King Slime
- Eye of Cthulhu
- Eater of Worlds
- Queen Bee
- Skeletron
- Wall of Flesh

### NPCs 😆

<div class="box">
  <img src="images/npc.png" alt="chester" style="max-width: 100%;">
</div>

- Guide、Merchant、Nurse、Goblin Thinkerer、Demolitionist、Arms Dealer、Fish Man... 17 kinds

### Summonings 🐣

<div class="box">
  <img src="images/summoning.png" alt="chester" style="max-width: 50%;">
</div>

- Finch Staff
- Iron Golem Staff
- Slime_ Staff
- Hornet Staff
- Sculk Wisp Staff
- Imp Staff
- Snow Flinx Staff
- Summon Sword 6 kinds
- Terraprism 

---
## ⚔️ Weapons

<div class="box">
  <img src="images/weapon.png" alt="chester" style="max-width: 100%;">
</div>

### Yoyos 🪀

Long press the mouse to shoot, and scroll the mouse wheel to change the range. You can lock onto the target pointed by the nearest pointer.

### Boomerangs 🪃

Right-click to fire, and it will fly for a period of time before returning to the player's hand.

### Summoning staffs 🪄

Right-click to fire, and it will fly for a while before returning to the player's hand.

### Whips 🪢

Right-click to whip all targets within range, causing the summonings to deal additional damage to the targets.

---
## 📕 Enchant Books

### Whip Sweep

The whip can sweep all targets within its range.

### Multi Boomerang

You can fire an additional boomerang.

---
## 🔧 Tools

### House Detector 🏠

It has three modes: detecting houses, adding houses (requires prior detection), and deleting houses. Right-click on NPCs to add houses, allowing NPCs to move in.

### Pets 🐕

- Chester, Wallet: They allow remote connection to containers and ender chests.  

<div class="box">
  <img src="images/pet.png" alt="chester" style="max-width: 50%;">
</div>

### Rideable 🐎

Right-click to summon a pet mount, and press shift to recall the pet.
- Slime
- Bee

<div class="box">
  <img src="images/rideable.png" alt="chester" style="max-width: 50%;">
</div>



--- 
## 🥚 Spawn Eggs

<div class="box">
    <img src="images/eggs_1.png" alt="chester" style="max-width: 45%;">
    <img src="images/eggs_2.png" alt="chester" style="max-width: 45%;">
</div>

---
## 📖 Gameplay
### NPC System 🙋‍♂️
- Trade (data/terra_entity/npc/shop/)
- Mood (data/terra_entity/npc/moods.json)
- Dialog (data/terra_entity/npc/dialogs.json)
- Chat (data/terra_entity/npc/chat/)
- Name (data/terra_entity/npc/names.json)

### Summoning System 🧙🏻
- Summon Damage: Whips and Summonings cause base damage.  
- Mark Damage: Summonings cause additional damage when handholding a whip.  
- Minion Capacity: Maximum number of minions that can be summoned. 


### Configuration Files ⚙️
- Common Configuration (config/terra_entity-common.toml)
- Client Configuration (config/terra_entity-client.toml)
- Attribute Configuration (config/terra_entity/attribute_config.json)


<style>

.box {
    text-align: center;
}

img {
    border-radius: 5px;
    box-shadow: 10px 10px 5px rgba(0,0,0,.5);
    -moz-box-shadow: 10px 10px 10px rgba(0,0,0,.5);
    -webkit-box-shadow: 10px 10px 10px rgba(0,0,0,.5);
}

h1 {
    background: linear-gradient(135deg, #0eaf6d, #ff6ac6, #147b96);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

</style>




