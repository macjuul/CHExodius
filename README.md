# CHExodius
Adds a bunch of random handy functions & events to CommandHelper. Some of them require NMS code so this Extensions will probably break quite often. If that happens check here for an updated version or create an issue ticket.

Latest release: [download v2.2.3](https://github.com/macjuul/CHExodius/releases/tag/v2.2.3)

## Functions:
+ **set_tab_msg(Player, Header, Footer)** - Set a TAB list header & footer message
+ **send_action_msg(Player, String)** - Sends a message in the action bar
+ **send_title(Player, Title, Subtitle, FadeIn, Stay, FadeOut)** - Send a title message to a player
+ **send_chest_packet(Player, Location, State)** - Sends a fake chest open packet. State determines if the chest opens (true) or closes (false)
+ **set_hotbar_slot(Player, Slot)** - Set the players hotbar slot. Slot is a number from 0 to 8
+ **send_block_cracks(Player, Location, CrackSize, id)** - Makes a fake block crack effect at the location. crackSize changes the size of the cracks. Supplying the function with an ID allows you to 'remove' the crack later by making a level 0 crack with the same ID
+ **user_input(Player, Callback, [Item])** - Open an Anvil GUI input for player, calling the callback closure when the player submits the input. The text the player typed in gets returned to the closure. Item can be an item array, already only the keys 'type', 'data' and 'display' are used.
+ **json_msg(Player, Msg)** - Send a raw JSON message to the player (Much like /tellraw)
+ **set_pspectating(Player, EntityID | null)** - Makes the player spectate the given entity. If entity is null the player will revert to his own vision.
+ **set_entity_target(Entity, TargetEntity)** - make an entity target another mob. This function is very likeley to do nothing at all.
+ **get_tps() [SPIGOT ONLY]** - Gets an array containing the TPS of the last 1, 5 and 15 minutes.
+ **launch_instant_firework(locationArray, [color], [type], [flicker], [trail])** - Launch an instantly detonating firework. Color accepts an array containing 3 ints, using RGB
+  **set_entity_advanced_spec(Entity UUID, Spec array)** - Set entity options that Command Helper does not support directly. Current specs are Ignited (For creepers) and NoAI
+  **popen_villager_trade([player], Villager UUID, force)** - Opens a villager trading GUI. If force is true the current player trading will be kicked out of the trade.
+  **center_chat_message(String message)** - Centers a string for a nice chat message effect

## Events:
###projectile_hit_block
*Event data:*
* id - The entity ID of the projectile
* type - returns the entity type of the entity that hit a block
* location - returns the location of _the block the arrow hit_


###entity_combust
*Event data:*
* id - The entity ID of the projectile
* type - returns the entity type of the entity that hit a block
* duration - The duration the fire will last for

*Mutable fields:*
* duration
