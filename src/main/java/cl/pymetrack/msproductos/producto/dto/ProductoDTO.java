package cl.pymetrack.msproductos.producto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoDTO {
    private Long id;
    private Long pymeId;
    private String codigoSKU;
    private String nombreProducto;
    private String descripcionProducto;
    private BigDecimal precioVentaChile;
    private BigDecimal pesoProductoKg;
    private String dimensionesProducto;
    private String imagenUrl;
    private String categoriaProducto;
    private Boolean activo;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    // Constructors
    public ProductoDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPymeId() { return pymeId; }
    public void setPymeId(Long pymeId) { this.pymeId = pymeId; }

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

    public String getCategoriaProducto() { return categoriaProducto; }
    public void setCategoriaProducto(String categoriaProducto) { this.categoriaProducto = categoriaProducto; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
