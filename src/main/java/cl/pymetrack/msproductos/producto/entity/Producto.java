package cl.pymetrack.msproductos.producto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_pyme", nullable = false)
    private Long idPyme;
    
    @Column(name = "codigo_sku", nullable = false, length = 50)
    private String codigoSKU;
    
    @Column(name = "nombre_producto", nullable = false, length = 200)
    private String nombreProducto;
    
    @Column(name = "descripcion_producto", columnDefinition = "TEXT")
    private String descripcionProducto;
    
    @Column(name = "precio_venta_chile", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVentaChile;
    
    @Column(name = "peso_producto_kg", precision = 8, scale = 3)
    private BigDecimal pesoProductoKg;
    
    @Column(name = "dimensiones_producto", length = 50)
    private String dimensionesProducto;
    
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;
    
    @Column(name = "categoria_producto", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaProducto categoriaProducto;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
    
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn = LocalDateTime.now();

    @Transient
    private Integer stockInicial;
    
    // Constructors
    public Producto() {}
    
    public Producto(Long idPyme, String codigoSKU, String nombreProducto, BigDecimal precioVentaChile) {
        this.idPyme = idPyme;
        this.codigoSKU = codigoSKU;
        this.nombreProducto = nombreProducto;
        this.precioVentaChile = precioVentaChile;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdPyme() { return idPyme; }
    public void setIdPyme(Long idPyme) { this.idPyme = idPyme; }
    
    public String getCodigoSKU() { return codigoSKU; }
    public void setCodigoSKU(String codigoSKU) { this.codigoSKU = codigoSKU; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
    
    public BigDecimal getPrecioVentaChile() { return precioVentaChile; }
    public void setPrecioVentaChile(BigDecimal precioVentaChile) { this.precioVentaChile = precioVentaChile; }
    
    public BigDecimal getPesoProductoKg() { return pesoProductoKg; }
    public void setPesoProductoKg(BigDecimal pesoProductoKg) { this.pesoProductoKg = pesoProductoKg; }
    
    public String getDimensionesProducto() { return dimensionesProducto; }
    public void setDimensionesProducto(String dimensionesProducto) { this.dimensionesProducto = dimensionesProducto; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public CategoriaProducto getCategoriaProducto() { return categoriaProducto; }
    public void setCategoriaProducto(CategoriaProducto categoriaProducto) { this.categoriaProducto = categoriaProducto; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public Integer getStockInicial() { return stockInicial;}

    public void setStockInicial(Integer stockInicial) { this.stockInicial = stockInicial;}
    
    @PreUpdate
    public void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
