package de.wzrd;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class FastReplyClient implements ClientModInitializer {
    private static final Responder responder = new Responder();
    private static final String SENDER_MSG_KEY = "commands.message.display.incoming";

    @Override
    public void onInitializeClient() {
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (params.type().value().chat().translationKey().equals(SENDER_MSG_KEY) && sender != null && sender.name() != null) {
                responder.setLastName(sender.name());
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal("r")
                        .then(argument("msg", greedyString())
                                .executes(context -> {
                                    final String message = context.getArgument("msg", String.class);
                                    final boolean status = responder.executeMessage(MinecraftClient.getInstance().player, message);
                                    if (!status) {
                                        context.getSource().sendFeedback(Text.literal("You haven't spoken to anyone yet"));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))));
    }


}
