package cl.pymetrack.msproductos.shared.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String PEDIDOS_EXCHANGE = "pedidos.exchange";
    public static final String PEDIDO_CREADO_QUEUE = "pedido.creadado";
    public static final String PEDIDO_CONFIRMADO_QUEUE = "pedido.confirmado";
    public static final String INVENTARIO_ACTUALIZADO_QUEUE = "inventario.actualizado";
    
    public static final String PEDIDO_CREADO_ROUTING_KEY = "pedido.creadado";
    public static final String PEDIDO_CONFIRMADO_ROUTING_KEY = "pedido.confirmado";
    public static final String INVENTARIO_ACTUALIZADO_ROUTING_KEY = "inventario.actualizado";
    
    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE, true, false);
    }
    
    @Bean
    public Queue pedidoCreadoQueue() {
        return QueueBuilder.durable(PEDIDO_CREADO_QUEUE).build();
    }
    
    @Bean
    public Queue pedidoConfirmadoQueue() {
        return QueueBuilder.durable(PEDIDO_CONFIRMADO_QUEUE).build();
    }
    
    @Bean
    public Queue inventarioActualizadoQueue() {
        return QueueBuilder.durable(INVENTARIO_ACTUALIZADO_QUEUE).build();
    }
    
    @Bean
    public Binding pedidoCreadoBinding() {
        return BindingBuilder.bind(pedidoCreadoQueue())
            .to(pedidosExchange())
            .with(PEDIDO_CREADO_ROUTING_KEY);
    }
    
    @Bean
    public Binding pedidoConfirmadoBinding() {
        return BindingBuilder.bind(pedidoConfirmadoQueue())
            .to(pedidosExchange())
            .with(PEDIDO_CONFIRMADO_ROUTING_KEY);
    }
    
    @Bean
    public Binding inventarioActualizadoBinding() {
        return BindingBuilder.bind(inventarioActualizadoQueue())
            .to(pedidosExchange())
            .with(INVENTARIO_ACTUALIZADO_ROUTING_KEY);
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
