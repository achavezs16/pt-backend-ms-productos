package cl.pymetrack.msproductos.producto.service;

import cl.pymetrack.msproductos.inventario.entity.Inventario;
import cl.pymetrack.msproductos.inventario.repository.InventarioRepository;
import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.CategoriaProducto;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setIdPyme(10L);
        producto.setCodigoSKU("SKU-123");
        producto.setNombreProducto("Producto Test");
        producto.setPrecioVentaChile(BigDecimal.valueOf(15000)); 
        producto.setStockInicial(10);
        producto.setActivo(true);
        // Truco Senior: Obtenemos dinámicamente el primer valor de tu Enum para evitar nulos
        producto.setCategoriaProducto(CategoriaProducto.values()[0]); 
    }

    @Test
    void testGetProductosByPyme() {
        when(productoRepository.findByIdPymeAndActivoTrue(10L)).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.getProductosByPyme(10L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Producto Test", resultado.get(0).getNombreProducto());
        verify(productoRepository, times(1)).findByIdPymeAndActivoTrue(10L);
    }

    @Test
    void testGetProductoById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.getProductoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    // ==========================================
    // TESTS DE CREACIÓN
    // ==========================================

    @Test
    void testCreateProducto_InventarioNoExiste_ConStockInicial() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(inventarioRepository.existsByProductoId(1L)).thenReturn(false);

        Producto resultado = productoService.createProducto(producto);

        assertTrue(resultado.getActivo());
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void testCreateProducto_InventarioNoExiste_StockInicialNull() {
        producto.setStockInicial(null);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(inventarioRepository.existsByProductoId(1L)).thenReturn(false);

        Producto resultado = productoService.createProducto(producto);

        assertNotNull(resultado);
        // CORRECCIÓN EXACTA: Usamos getStockDisponible() en base a tu clase Inventario
        verify(inventarioRepository, times(1)).save(argThat(inv -> inv.getStockDisponible() == 0));
    }

    @Test
    void testCreateProducto_InventarioYaExiste() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(inventarioRepository.existsByProductoId(1L)).thenReturn(true);

        Producto resultado = productoService.createProducto(producto);

        assertNotNull(resultado);
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN Y ELIMINACIÓN
    // ==========================================

    @Test
    void testUpdateProducto_Exitoso() {
        Producto productoActualizado = new Producto();
        productoActualizado.setNombreProducto("Nuevo Nombre");
        productoActualizado.setPrecioVentaChile(BigDecimal.valueOf(20000));
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Optional<Producto> resultado = productoService.updateProducto(1L, productoActualizado);

        assertTrue(resultado.isPresent());
        assertEquals("Nuevo Nombre", producto.getNombreProducto());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testUpdateProducto_NoEncontrado() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.updateProducto(1L, new Producto());

        assertFalse(resultado.isPresent());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testDeleteProducto_Exitoso() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        boolean resultado = productoService.deleteProducto(1L);

        assertTrue(resultado);
        assertFalse(producto.getActivo());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testDeleteProducto_NoEncontrado() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        boolean resultado = productoService.deleteProducto(1L);

        assertFalse(resultado);
        verify(productoRepository, never()).save(any());
    }

    // ==========================================
    // TESTS DE BÚSQUEDA Y DTOs
    // ==========================================

    @Test
    void testBuscarProductos() {
        when(productoRepository.buscarPorPymeYQuery(10L, "Test")).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.buscarProductos(10L, "Test");

        assertFalse(resultado.isEmpty());
        verify(productoRepository, times(1)).buscarPorPymeYQuery(10L, "Test");
    }

    @Test
    void testGetProductosByPymeAsDTO() {
        when(productoRepository.findByIdPymeAndActivoTrue(10L)).thenReturn(Arrays.asList(producto));

        List<ProductoDTO> resultado = productoService.getProductosByPymeAsDTO(10L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Producto Test", resultado.get(0).getNombreProducto());
        assertEquals(10L, resultado.get(0).getPymeId());
    }
}