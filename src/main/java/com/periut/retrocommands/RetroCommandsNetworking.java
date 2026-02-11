package com.periut.retrocommands;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;

public class RetroCommandsNetworking {
	public static final NamespacedIdentifier OP_CHANNEL =
		ChannelRegistry.register(ChannelIdentifiers.from("retrocommands", "op"), true, false);
	public static final NamespacedIdentifier PLAYERS_CHANNEL =
		ChannelRegistry.register(ChannelIdentifiers.from("retrocommands", "players"), true, false);
	public static final NamespacedIdentifier DISABLED_CHANNEL =
		ChannelRegistry.register(ChannelIdentifiers.from("retrocommands", "disabled"), true, false);
}
