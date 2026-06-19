package cl.pymetrack.msproductos.shared.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoEstadoEvent {

    private Long pedidoId;
    private Long idPyme;
    private String nombreCliente;
    private String emailCliente;
    private String estadoAnterior;
    private String estadoNuevo;
    private Long repartidorId;
    private String observacion;
    private LocalDateTime fechaEvento;
    private List<PedidoItemEvent> items;

    public PedidoEstadoEvent() {}

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Long getIdPyme() { return idPyme; }
    public void setIdPyme(Long idPyme) { this.idPyme = idPyme; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public Long getRepartidorId() { return repartidorId; }
    public void setRepartidorId(Long repartidorId) { this.repartidorId = repartidorId; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDateTime fechaEvento) { this.fechaEvento = fechaEvento; }

    public List<PedidoItemEvent> getItems() { return items; }
    public void setItems(List<PedidoItemEvent> items) { this.items = items; }
}