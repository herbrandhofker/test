package academy.kafka;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import org.xnio.ChannelListener;

import academy.kafka.utils.KafkaUtils;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class WebsocketService04 {
    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
    private static final JsonSchemaGenerator generator = new JsonSchemaGenerator(JACKSON_MAPPER);

    private static final String WEBPAGE = "index.html";
    private static final String ApplicationMame = "myChatApp";

    private static final List<WebSocketChannel> channels = Collections
            .synchronizedList(new ArrayList<WebSocketChannel>());

    public static void main(final String[] args) {
        ArrayNode schemas = JACKSON_MAPPER.createArrayNode();

        TreeSet<String> lst = KafkaUtils.getTopicNames();
        lst.forEach(topic -> {
            Class<?> cls = getEntityClass(topic);
            if (cls != null) {

                JsonNode schema = academy.kafka.utils.SchemaUtils.getSchema(cls);
                // JsonNode schema = generator.generateJsonSchema(getEntityClass(topic));
                ObjectNode schemaObject = JACKSON_MAPPER.createObjectNode();
                schemaObject.put("key", topic);
                schemaObject.set("value", schema);
                schemas.add(schemaObject);
            }
        });

        startServer(schemas);
    }

    public static void startServer(ArrayNode schemas) {

        final String host = "localhost";
        final int port = 8081;
        URI uri = URI.create("http://" + host + ":" + port);
        System.out.println("Go to " + uri.toString());

        Undertow server = Undertow.builder().addHttpListener(port, "localhost")
                .setHandler(path().addPrefixPath(ApplicationMame, websocket(new WebSocketConnectionCallback() {
                    @Override
                    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                        System.out.println("onConnect");
                        final String chatboxName = getChatboxFromUrl(channel, ApplicationMame);
                        setChatboxTag(channel, chatboxName);
                        synchronized (channels) {
                            setChatboxTag(channel, chatboxName);
                            channels.add(channel);
                            switch (chatboxName) {
                                case "meta": {
                                    channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                        @Override
                                        protected void onFullTextMessage(WebSocketChannel channel,
                                                BufferedTextMessage message) {
                                            channels.forEach((ch) -> {
                                                if (getChatboxTag(ch).equals("meta")) {

                                                    WebSockets.sendText(schemas.toString(), ch, null);
                                                }
                                            });
                                        }
                                    });
                                    break;
                                }
                                case "control": {
                                    channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                        @Override
                                        protected void onFullTextMessage(WebSocketChannel channel,
                                                BufferedTextMessage message) {
                                            String msg = message.getData();
                                            try {
                                                channels.forEach((ch) -> {
                                                    if (getChatboxTag(ch).equals("control")) {
                                                        WebSockets.sendText(msg, ch, null);
                                                    }

                                                });
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                                    break;
                                }

                                default: {
                                    final String topic = chatboxName;
                                    Class<?> clazz = getEntityClass(topic);
                                    if (clazz == null)
                                        System.err.println("topic:>>" + topic + "<< not found");
                                    else {
                                        channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                            @Override
                                            protected void onFullTextMessage(WebSocketChannel channel,
                                                    BufferedTextMessage message) {
                                                Thread thread = new ConsumeThread(channels, topic, clazz);
                                                thread.start();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        channel.addCloseTask(new ChannelListener<WebSocketChannel>() {
                            @Override
                            public void handleEvent(WebSocketChannel chatbox) {
                                synchronized (channels) {
                                    System.out.println(String.format("chatbox %s disconnects", getChatboxTag(chatbox)));
                                    channels.remove(chatbox);
                                }
                            }
                        });
                        channel.resumeReceives();
                    }

                })).addPrefixPath("/", resource(new ClassPathResourceManager(WebsocketService04.class.getClassLoader()))
                        .addWelcomeFiles(WEBPAGE)))
                .build();

        server.start();

        try {

            java.awt.Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getChatboxFromUrl(WebSocketChannel chatbox, String prefix) {
        String url = chatbox.getUrl();
        System.out.println("url=" + url.toString() + "prefix=" + prefix);
        return chatbox.getUrl().substring(url.indexOf(prefix) + prefix.length() + 1);
    }

    private static void setChatboxTag(WebSocketChannel chatbox, String chatboxName) {
        chatbox.setAttribute("chat", chatboxName);
    }

    public static String getChatboxTag(WebSocketChannel chatbox) {
        return (String) chatbox.getAttribute("chat");
    }

    public static Class<?> getEntityClass(String topic) {
        try {
            return Class.forName("academy.kafka.entities." + topic);
        } catch (ClassNotFoundException e) {
         }
        return null;
    }

}
