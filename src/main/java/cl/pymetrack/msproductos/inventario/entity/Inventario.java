package cl.pymetrack.msproductos.inventario.entity;

import cl.pymetrack.msproductos.producto.entity.Producto;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "producto_id", nullable = false, unique = true)
    private Long productoId;
    
    @Column(name = "stock_disponible", nullable = false)
    private Integer stockDisponible = 0;
    
    @Column(name = "stock_reservado", nullable = false)
    private Integer stockReservado = 0;
    
    @Column(name = "ultimo_actualizado")
    private LocalDateTime ultimoActualizado = LocalDateTime.now();
    
    // Constructors
    public Inventario() {}
    
    public Inventario(Long productoId, Integer stockDisponible) {
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
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
    
    @PreUpdate
    public void preUpdate() { this.ultimoActualizado = LocalDateTime.now(); }
    
    public Integer getStockTotal() { return stockDisponible + stockReservado; }
}
