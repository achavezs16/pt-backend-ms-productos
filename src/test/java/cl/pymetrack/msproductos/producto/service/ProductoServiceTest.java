package cl.pymetrack.msproductos.producto.service;

import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.entity.CategoriaProducto;
import cl.pymetrack.msproductos.producto.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void getProductosByPyme_Success() {
        when(productoRepository.findByIdPymeAndActivoTrue(1L)).thenReturn(List.of(new Producto()));
        List<Producto> result = productoService.getProductosByPyme(1L);
        assertFalse(result.isEmpty());
    }

    @Test
    void getProductoById_Success() {
        Producto p = new Producto();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        Optional<Producto> result = productoService.getProductoById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void createProducto_Success() {
        Producto p = new Producto();
        when(productoRepository.save(any(Producto.class))).thenReturn(p);
        
        Producto result = productoService.createProducto(p);
        
        assertTrue(result.getActivo()); // Verifica la lógica de negocio
        verify(productoRepository).save(p);
    }

    @Test
    void updateProducto_Success() {
        Long id = 1L;
        Producto existing = new Producto();
        Producto details = new Producto();
        details.setNombreProducto("Nuevo Nombre");
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productoRepository.save(any(Producto.class))).thenReturn(existing);
        
        Optional<Producto> result = productoService.updateProducto(id, details);
        
        assertTrue(result.isPresent());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void deleteProducto_Success() {
        Long id = 1L;
        Producto p = new Producto();
        p.setActivo(true);
        when(productoRepository.findById(id)).thenReturn(Optional.of(p));
        
        boolean result = productoService.deleteProducto(id);
        
        assertTrue(result);
        assertFalse(p.getActivo()); // Verifica que se desactivó
        verify(productoRepository).save(p);
    }

    @Test
    void buscarProductos_Success() {
        when(productoRepository.buscarPorPymeYQuery(1L, "test")).thenReturn(List.of(new Producto()));
        List<Producto> result = productoService.buscarProductos(1L, "test");
        assertFalse(result.isEmpty());
    }

    @Test
    void getProductosByPymeAsDTO_Success() {
        Producto p = new Producto();
        p.setId(1L);
        // FIX: Cambia 'CategoriaProducto.OTROS' por CUALQUIERA que exista en tu Enum
        // Si no sabes cuál existe, usa p.setCategoriaProducto(CategoriaProducto.values()[0]);
        p.setCategoriaProducto(CategoriaProducto.values()[0]); 
        
        when(productoRepository.findByIdPymeAndActivoTrue(1L)).thenReturn(List.of(p));
        
        List<ProductoDTO> result = productoService.getProductosByPymeAsDTO(1L);
        
        assertFalse(result.isEmpty());
        assertEquals(p.getId(), result.get(0).getId());
    }
}