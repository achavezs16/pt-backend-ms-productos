package cl.pymetrack.msproductos.producto.controller;

import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos de una PYME
    @GetMapping("/pyme/{pymeId}")
    public ResponseEntity<?> getProductosByPyme(@PathVariable Long pymeId) {
        try {
            List<ProductoDTO> productos = productoService.getProductosByPymeAsDTO(pymeId);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener productos: " + e.getMessage());
        }
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Optional<Producto> productoActualizado = productoService.updateProducto(id, producto);
        return productoActualizado.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un producto (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        boolean eliminado = productoService.deleteProducto(id);
        return eliminado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Buscar productos por nombre o SKU
    @GetMapping("/pyme/{pymeId}/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(
            @PathVariable Long pymeId,
            @RequestParam String query) {
        List<Producto> productos = productoService.buscarProductos(pymeId, query);
        return ResponseEntity.ok(productos);
    }
}
