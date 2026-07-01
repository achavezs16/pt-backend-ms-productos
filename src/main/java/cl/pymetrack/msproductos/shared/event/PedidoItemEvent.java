package cl.pymetrack.msproductos.shared.event;

public class PedidoItemEvent {

    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;

    public PedidoItemEvent() {}

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}