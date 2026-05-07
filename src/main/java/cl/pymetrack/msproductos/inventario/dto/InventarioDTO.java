package cl.pymetrack.msproductos.inventario.dto;

import java.time.LocalDateTime;

public class InventarioDTO {
    private Long id;
    private Long productoId;
    private Integer stockDisponible;
    private Integer stockReservado;
    private LocalDateTime ultimoActualizado;

    // Constructors
    public InventarioDTO() {}

    public InventarioDTO(Long productoId, Integer stockDisponible, Integer stockReservado) {
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
        this.stockReservado = stockReservado;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }

    public Integer getStockReservado() { return stockReservado; }
    public void setStockReservado(Integer stockReservado) { this.stockReservado = stockReservado; }

    public LocalDateTime getUltimoActualizado() { return ultimoActualizado; }
    public void setUltimoActualizado(LocalDateTime ultimoActualizado) { this.ultimoActualizado = ultimoActualizado; }
}
