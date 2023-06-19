import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class Program {
    
    public static void main(String[] args) throws Exception {
        var token = Optional.ofNullable(System.getenv("TOKEN"))
                .orElseThrow(() -> new Exception("Bot token not found."));
        // You shouldn't need to change anything above this
        
        var slashCommandHandler = new SlashCommandHandler();

        // You shouldn't need to change anything below this
        DiscordClientBuilder.create(token)
                .build()
                .withGateway(gateway -> {
                    var printOnLogin = gateway.on(ReadyEvent.class, event -> Mono.fromRunnable(() -> {
                                var self = event.getSelf();
                                System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                            }))
                            .then();

                    var handleSlash = gateway.on(ChatInputInteractionEvent.class, slashCommandHandler::handle)
                            .then();
                    
                    registerCommands(gateway.getRestClient());

                    return printOnLogin.and(handleSlash);
                })
                .block();
    }

    private static void registerCommands(RestClient client) {
        final var appId = client.getApplicationId()
                .block();
        if (appId == null) return;

        var commands = List.of(
                createCommandRequest("qr", "encode some text into a QR image",
                        createOptionData(COMMAND_TEXT_OPTION_NAME, "text to encode", true)),
                createCommandRequest("qrsave", "save a QR with a name",
                        createOptionData(COMMAND_TEXT_OPTION_NAME, "text to encode", true),
                        createOptionData(COMMAND_NAME_OPTION_NAME, "a unique name to save your QR", true)),
                createCommandRequest("qrload", "load a saved QR",
                        createOptionData(COMMAND_NAME_OPTION_NAME, "the name of your saved QR", true))
        );

        client.getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(appId, commands)
                .subscribe();
    }

    private static ApplicationCommandRequest createCommandRequest(String name, String description, ApplicationCommandOptionData... options) {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description(description)
                .addOptions(options)
                .build();
    }

    private static ApplicationCommandOptionData createOptionData(String name, String description, boolean required) {
        return ApplicationCommandOptionData.builder()
                .name(name)
                .description(description)
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(required)
                .build();
    }
}