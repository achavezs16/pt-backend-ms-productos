package cl.pymetrack.msproductos.inventario.service;

import cl.pymetrack.msproductos.inventario.entity.Inventario;
import cl.pymetrack.msproductos.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(10L);
        inventario.setStockDisponible(100);
        inventario.setStockReservado(20);
    }

    // ==========================================
    // TESTS MÉTODOS BÁSICOS (CRUD)
    // ==========================================

    @Test
    void testFindAll() {
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario));
        List<Inventario> resultado = inventarioService.findAll();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testFindById() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        Optional<Inventario> resultado = inventarioService.findById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testFindByProductoId() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        Optional<Inventario> resultado = inventarioService.findByProductoId(10L);
        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getProductoId());
    }

    @Test
    void testSave() {
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
        Inventario resultado = inventarioService.save(inventario);
        assertNotNull(resultado);
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testDeleteById() {
        doNothing().when(inventarioRepository).deleteById(1L);
        inventarioService.deleteById(1L);
        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsByProductoId() {
        when(inventarioRepository.existsByProductoId(10L)).thenReturn(true);
        boolean resultado = inventarioService.existsByProductoId(10L);
        assertTrue(resultado);
    }

    // ==========================================
    // TESTS ACTUALIZAR STOCK
    // ==========================================

    @Test
    void testActualizarStock_Exitoso() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario resultado = inventarioService.actualizarStock(10L, 150, 30);

        assertEquals(150, resultado.getStockDisponible());
        assertEquals(30, resultado.getStockReservado());
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testActualizarStock_NoEncontrado() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.actualizarStock(99L, 100, 10);
        });

        assertEquals("No existe inventario para el producto ID: 99", exception.getMessage());
    }

    // ==========================================
    // TESTS RESERVAR STOCK
    // ==========================================

    @Test
    void testReservarStock_Exitoso() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        
        boolean resultado = inventarioService.reservarStock(10L, 50);

        assertTrue(resultado);
        assertEquals(50, inventario.getStockDisponible()); // 100 - 50
        assertEquals(70, inventario.getStockReservado());  // 20 + 50
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testReservarStock_NoEncontrado() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> inventarioService.reservarStock(99L, 10));
    }

    @Test
    void testReservarStock_CantidadInvalida() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.reservarStock(10L, 0));
    }

    @Test
    void testReservarStock_StockInsuficiente() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        
        boolean resultado = inventarioService.reservarStock(10L, 150); // Pide más del stock disponible (100)

        assertFalse(resultado);
        verify(inventarioRepository, never()).save(any());
    }

    // ==========================================
    // TESTS LIBERAR STOCK
    // ==========================================

    @Test
    void testLiberarStock_Exitoso() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.liberarStock(10L, 10);

        assertTrue(resultado);
        assertEquals(110, inventario.getStockDisponible()); // 100 + 10
        assertEquals(10, inventario.getStockReservado());   // 20 - 10
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testLiberarStock_NoEncontrado() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> inventarioService.liberarStock(99L, 10));
    }

    @Test
    void testLiberarStock_CantidadInvalida() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.liberarStock(10L, -5));
    }

    @Test
    void testLiberarStock_StockReservadoInsuficiente() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.liberarStock(10L, 30); // Intenta liberar 30, pero solo hay 20 reservados

        assertFalse(resultado);
        verify(inventarioRepository, never()).save(any());
    }

    // ==========================================
    // TESTS CONFIRMAR DESPACHO
    // ==========================================

    @Test
    void testConfirmarDespacho_DesdeReservado_Exitoso() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.confirmarDespacho(10L, 15); // Hay 20 reservados, descuenta de ahí

        assertTrue(resultado);
        assertEquals(100, inventario.getStockDisponible()); // Intacto
        assertEquals(5, inventario.getStockReservado());    // 20 - 15
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testConfirmarDespacho_DesdeDisponible_Exitoso() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.confirmarDespacho(10L, 50); // Pide 50, excede reservados (20), cobra de disponible (100)

        assertTrue(resultado);
        assertEquals(50, inventario.getStockDisponible()); // 100 - 50
        assertEquals(20, inventario.getStockReservado());  // Intacto
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testConfirmarDespacho_SinStockEnAmbos() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.confirmarDespacho(10L, 150); // Excede ambos (100 y 20)

        assertFalse(resultado);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testConfirmarDespacho_NoEncontrado() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> inventarioService.confirmarDespacho(99L, 10));
    }

    @Test
    void testConfirmarDespacho_CantidadInvalida() {
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        assertThrows(IllegalArgumentException.class, () -> inventarioService.confirmarDespacho(10L, 0));
    }
}