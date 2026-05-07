package cl.pymetrack.msproductos.producto.service;

import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProductosByPyme(Long pymeId) {
        return productoRepository.findByIdPymeAndActivoTrue(pymeId);
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public Optional<Producto> updateProducto(Long id, Producto productoDetails) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setNombreProducto(productoDetails.getNombreProducto());
                producto.setDescripcionProducto(productoDetails.getDescripcionProducto());
                producto.setPrecioVentaChile(productoDetails.getPrecioVentaChile());
                producto.setPesoProductoKg(productoDetails.getPesoProductoKg());
                producto.setDimensionesProducto(productoDetails.getDimensionesProducto());
                producto.setImagenUrl(productoDetails.getImagenUrl());
                return productoRepository.save(producto);
            });
    }

    public boolean deleteProducto(Long id) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setActivo(false);
                productoRepository.save(producto);
                return true;
            })
            .orElse(false);
    }

    public List<Producto> buscarProductos(Long pymeId, String query) {
        return productoRepository.buscarPorPymeYQuery(pymeId, query);
    }

    // Método para convertir Producto a ProductoDTO
    private ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setPymeId(producto.getIdPyme());
        dto.setCodigoSKU(producto.getCodigoSKU());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        dto.setPrecioVentaChile(producto.getPrecioVentaChile());
        dto.setPesoProductoKg(producto.getPesoProductoKg());
        dto.setDimensionesProducto(producto.getDimensionesProducto());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setCategoriaProducto(producto.getCategoriaProducto().toString());
        dto.setActivo(producto.getActivo());
        dto.setCreadoEn(producto.getCreadoEn());
        dto.setActualizadoEn(producto.getActualizadoEn());
        return dto;
    }

    // Método para obtener productos como DTOs
    public List<ProductoDTO> getProductosByPymeAsDTO(Long pymeId) {
        List<Producto> productos = productoRepository.findByIdPymeAndActivoTrue(pymeId);
        return productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
