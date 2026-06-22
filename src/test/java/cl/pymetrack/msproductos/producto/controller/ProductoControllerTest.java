package cl.pymetrack.msproductos.producto.controller;

import cl.pymetrack.msproductos.producto.dto.ProductoDTO;
import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;
    private ProductoDTO productoDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        
        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();

        // Configuramos ObjectMapper con soporte para fechas (LocalDateTime)
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        producto = new Producto();
        producto.setId(1L);
        producto.setIdPyme(10L);
        producto.setNombreProducto("Producto Controller Test");
        producto.setPrecioVentaChile(BigDecimal.valueOf(10000));

        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setPymeId(10L);
        productoDTO.setNombreProducto("Producto Controller Test");
    }

    // ==========================================
    // TESTS GET: Obtener todos los productos de una PYME
    // ==========================================

    @Test
    void testGetProductosByPyme_Exitoso() throws Exception {
        when(productoService.getProductosByPymeAsDTO(10L)).thenReturn(Arrays.asList(productoDTO));

        mockMvc.perform(get("/productos/pyme/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Controller Test"));

        verify(productoService, times(1)).getProductosByPymeAsDTO(10L);
    }

    @Test
    void testGetProductosByPyme_Excepcion() throws Exception {
        // Simulamos un error interno en el servicio para cubrir el bloque catch (Exception e)
        when(productoService.getProductosByPymeAsDTO(10L)).thenThrow(new RuntimeException("Error de Base de Datos"));

        mockMvc.perform(get("/productos/pyme/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error al obtener productos: Error de Base de Datos")));

        verify(productoService, times(1)).getProductosByPymeAsDTO(10L);
    }

    // ==========================================
    // TESTS GET: Obtener por ID
    // ==========================================

    @Test
    void testGetProductoById_Encontrado() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Controller Test"));
    }

    @Test
    void testGetProductoById_NoEncontrado() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS POST: Crear
    // ==========================================

    @Test
    void testCreateProducto() throws Exception {
        when(productoService.createProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Controller Test"));
    }

    // ==========================================
    // TESTS PUT: Actualizar
    // ==========================================

    @Test
    void testUpdateProducto_Encontrado() throws Exception {
        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(Optional.of(producto));

        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Controller Test"));
    }

    @Test
    void testUpdateProducto_NoEncontrado() throws Exception {
        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS DELETE: Eliminar
    // ==========================================

    @Test
    void testDeleteProducto_Exitoso() throws Exception {
        when(productoService.deleteProducto(1L)).thenReturn(true);

        mockMvc.perform(delete("/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProducto_NoEncontrado() throws Exception {
        when(productoService.deleteProducto(1L)).thenReturn(false);

        mockMvc.perform(delete("/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS GET: Buscar
    // ==========================================

    @Test
    void testBuscarProductos() throws Exception {
        when(productoService.buscarProductos(10L, "Test")).thenReturn(Collections.singletonList(producto));

        mockMvc.perform(get("/productos/pyme/10/buscar")
                .param("query", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Controller Test"));
    }
}