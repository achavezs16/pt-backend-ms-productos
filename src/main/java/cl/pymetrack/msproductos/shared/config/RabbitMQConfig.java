package cl.pymetrack.msproductos.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange compartido entre microservicios
    public static final String PEDIDOS_EXCHANGE = "pedidos.exchange";

    // Cola que escucha los cambios de estado emitidos por ms-pedidos
    public static final String PEDIDO_ESTADO_QUEUE = "pedido.estado.actualizado";

    // Routing key utilizada por ms-pedidos
    public static final String PEDIDO_ESTADO_ROUTING_KEY = "pedido.estado.actualizado";

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE, true, false);
    }

    @Bean
    public Queue pedidoEstadoQueue() {
        return QueueBuilder
                .durable(PEDIDO_ESTADO_QUEUE)
                .build();
    }

    @Bean
    public Binding pedidoEstadoBinding() {
        return BindingBuilder
                .bind(pedidoEstadoQueue())
                .to(pedidosExchange())
                .with(PEDIDO_ESTADO_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory) {

        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter());

        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory
    rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setMessageConverter(messageConverter());

        return factory;
    }
}