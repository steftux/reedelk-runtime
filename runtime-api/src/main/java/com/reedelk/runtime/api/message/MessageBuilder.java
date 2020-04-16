package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.message.content.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class MessageBuilder {

    private MessageAttributes attributes;
    private TypedContent<?,?> typedContent;

    private MessageBuilder() {
    }

    public static MessageBuilder get() {
        return new MessageBuilder();
    }

    // XML

    public MessageBuilder withXml(String xml) {
        this.typedContent = new StringContent(xml, MimeType.TEXT_XML);
        return this;
    }

    public MessageBuilder withXml(Publisher<String> xmlStream) {
        this.typedContent = new StringContent(xmlStream, MimeType.TEXT_XML);
        return this;
    }

    // HTML

    public MessageBuilder withHtml(String html) {
        this.typedContent = new StringContent(html, MimeType.TEXT_HTML);
        return this;
    }

    public MessageBuilder withHtml(Publisher<String> htmlStream) {
        this.typedContent = new StringContent(htmlStream, MimeType.TEXT_HTML);
        return this;
    }

    // TEXT

    public MessageBuilder withText(String text) {
        this.typedContent = new StringContent(text, MimeType.TEXT_PLAIN);
        return this;
    }

    public MessageBuilder withText(Publisher<String> textStream) {
        this.typedContent = new StringContent(textStream, MimeType.TEXT_PLAIN);
        return this;
    }

    // JSON

    public MessageBuilder withJson(String json) {
        this.typedContent = new StringContent(json, MimeType.APPLICATION_JSON);
        return this;
    }

    public MessageBuilder withJson(Publisher<String> jsonStream) {
        this.typedContent = new StringContent(jsonStream, MimeType.APPLICATION_JSON);
        return this;
    }

    // STRING

    public MessageBuilder withString(String value, MimeType mimeType) {
        this.typedContent = new StringContent(value, mimeType);
        return this;
    }

    public MessageBuilder withString(Publisher<String> valueStream, MimeType mimeType) {
        this.typedContent = new StringContent(valueStream, mimeType);
        return this;
    }

    // BINARY

    public MessageBuilder withBinary(byte[] bytes) {
        this.typedContent = new ByteArrayContent(bytes, MimeType.APPLICATION_BINARY);
        return this;
    }

    public MessageBuilder withBinary(byte[] bytes, MimeType mimeType) {
        this.typedContent = new ByteArrayContent(bytes, mimeType);
        return this;
    }

    public MessageBuilder withBinary(Publisher<byte[]> bytesStream) {
        this.typedContent = new ByteArrayContent(bytesStream, MimeType.APPLICATION_BINARY);
        return this;
    }

    public MessageBuilder withBinary(Publisher<byte[]> bytesStream, MimeType mimeType) {
        this.typedContent = new ByteArrayContent(bytesStream, mimeType);
        return this;
    }

    // TYPED STREAM

    @SuppressWarnings("unchecked")
    public <ItemType> MessageBuilder withStream(Publisher<ItemType> typedStream, Class<ItemType> clazz, MimeType mimeType) {
        if (String.class.equals(clazz)) {
            Publisher<String> stringStream = (Publisher<String>) typedStream;
            this.typedContent = new StringContent(stringStream, mimeType);
        } else if (byte[].class.equals(clazz)) {
            Publisher<byte[]> byteArrayStream = (Publisher<byte[]>) typedStream;
            this.typedContent = new ByteArrayContent(byteArrayStream, mimeType);
        } else {
            this.typedContent = new ListContent<>(typedStream, clazz);
        }
        return this;
    }

    public <ItemType> MessageBuilder withStream(Publisher<ItemType> typedStream, Class<ItemType> clazz) {
        withStream(typedStream, clazz, MimeType.APPLICATION_JAVA);
        return this;
    }

    // TYPED PUBLISHER

    public <ItemType> MessageBuilder withTypedPublisher(TypedPublisher<ItemType> typedPublisher) {
        withStream(typedPublisher, typedPublisher.getType());
        return this;
    }

    public <ItemType> MessageBuilder withTypedPublisher(TypedPublisher<ItemType> typedPublisher, MimeType mimeType) {
        withStream(typedPublisher, typedPublisher.getType(), mimeType);
        return this;
    }

    // JAVA OBJECT

    public MessageBuilder withJavaObject(Object object) {
        withJavaObject(object, MimeType.APPLICATION_JAVA);
        return this;
    }

    public <ItemType> MessageBuilder withJavaObject(Mono<ItemType> monoStream, Class<ItemType> type) {
        this.typedContent = new ObjectContent<>(monoStream, type);
        return this;
    }

    /**
     * Note that the mime type is only used for non Java only types, e.g byte array and String.
     */
    @SuppressWarnings("unchecked")
    public MessageBuilder withJavaObject(Object object, MimeType mimeType) {
        if (object == null) {
            empty();
        } else if (object instanceof Flux) {
            Flux<Object> objectStream = (Flux<Object>) object;
            this.typedContent = new ListContent<>(objectStream, Object.class);
        } else if (object instanceof Mono) {
            // A mono is considered a single object content. This is needed for instance when
            // we have Attachments map from REST Listener. The REST Listener mapper forces us
            // to have a map of Attachments inside a Mono.
            Mono<Object> objectStream = (Mono<Object>) object;
            this.typedContent = new ObjectContent<>(objectStream, Object.class);
        } else if (object instanceof String) {
            this.typedContent = new StringContent((String) object, mimeType);
        } else if (object instanceof byte[]) {
            this.typedContent = new ByteArrayContent((byte[]) object, mimeType);
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            this.typedContent = new ListContent<>(list, Object.class);
        } else {
            this.typedContent = new ObjectContent<>(object);
        }
        return this;
    }

    // TYPED CONTENT

    public MessageBuilder withTypedContent(TypedContent<?,?> typedContent) {
        this.typedContent = typedContent;
        return this;
    }

    // LIST

    public <ItemType> MessageBuilder withList(List<ItemType> list, Class<ItemType> listItemType) {
        this.typedContent = new ListContent<>(list, listItemType);
        return this;
    }

    public MessageBuilder empty() {
        this.typedContent = new EmptyContent();
        return this;
    }

    public MessageBuilder empty(MimeType mimeType) {
        this.typedContent = new EmptyContent(mimeType);
        return this;
    }

    public MessageBuilder attributes(MessageAttributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Message build() {
        if (attributes == null) {
            attributes = DefaultMessageAttributes.empty();
        }
        if (typedContent == null) {
            throw new IllegalStateException("Typed content missing");
        }
        return new MessageDefault(typedContent, attributes);
    }
}
