package cl.pymetrack.msproductos.inventario.messaging;

import cl.pymetrack.msproductos.inventario.service.InventarioService;
import cl.pymetrack.msproductos.shared.config.RabbitMQConfig;
import cl.pymetrack.msproductos.shared.event.PedidoEstadoEvent;
import cl.pymetrack.msproductos.shared.event.PedidoItemEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoEstadoListener {

    private final InventarioService inventarioService;

    public PedidoEstadoListener(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @RabbitListener(queues = RabbitMQConfig.PEDIDO_ESTADO_QUEUE)
    public void procesarCambioEstado(PedidoEstadoEvent event) {
        System.out.println("📦 Evento recibido en ms-productos. Pedido ID: "
                + event.getPedidoId()
                + " | Estado: "
                + event.getEstadoNuevo());

        if (event.getItems() == null || event.getItems().isEmpty()) {
            System.out.println("⚠️ Pedido sin items. No se actualiza inventario.");
            return;
        }

        for (PedidoItemEvent item : event.getItems()) {
            Long productoId = item.getProductoId();
            Integer cantidad = item.getCantidad();

            if (productoId == null || cantidad == null || cantidad <= 0) {
                System.out.println("⚠️ Item inválido en pedido " + event.getPedidoId());
                continue;
            }

            switch (event.getEstadoNuevo()) {
                case "ASIGNADO":
                case "ACEPTADO": {
                    boolean reservado = inventarioService.reservarStock(productoId, cantidad);
                    System.out.println("🟡 Stock reservado producto "
                            + productoId
                            + " cantidad "
                            + cantidad
                            + " resultado="
                            + reservado);
                    break;
                }

                case "RECHAZADO":
                case "CANCELADO": {
                    boolean liberado = inventarioService.liberarStock(productoId, cantidad);
                    System.out.println("🔵 Stock liberado producto "
                            + productoId
                            + " cantidad "
                            + cantidad
                            + " resultado="
                            + liberado);
                    break;
                }

                case "ENTREGADO": {
                    boolean confirmado = inventarioService.confirmarDespacho(productoId, cantidad);
                    System.out.println("🟢 Despacho confirmado producto "
                            + productoId
                            + " cantidad "
                            + cantidad
                            + " resultado="
                            + confirmado);
                    break;
                }

                default:
                    System.out.println("ℹ️ Estado sin impacto en inventario: " + event.getEstadoNuevo());
            }
        }
    }
}