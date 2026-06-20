package cl.pymetrack.msproductos.producto.controller;

import cl.pymetrack.msproductos.producto.entity.Producto;
import cl.pymetrack.msproductos.producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // <--- IMPORT NUEVO
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
        objectMapper = new ObjectMapper();
        // ¡LA LÍNEA MÁGICA QUE SOLUCIONA EL ERROR DE LAS FECHAS!
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getProductosByPyme_Success() throws Exception {
        when(productoService.getProductosByPymeAsDTO(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/productos/pyme/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductosByPyme_Error() throws Exception {
        doThrow(new RuntimeException("Error")).when(productoService).getProductosByPymeAsDTO(anyLong());
        mockMvc.perform(get("/productos/pyme/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getProductoById_Found() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.of(new Producto()));
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductoById_NotFound() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProducto_Success() throws Exception {
        Producto p = new Producto();
        when(productoService.createProducto(any(Producto.class))).thenReturn(p);
        
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProducto_Success() throws Exception {
        Producto p = new Producto();
        when(productoService.updateProducto(eq(1L), any(Producto.class))).thenReturn(Optional.of(p));
        
        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProducto_Success() throws Exception {
        when(productoService.deleteProducto(1L)).thenReturn(true);
        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarProductos_Success() throws Exception {
        when(productoService.buscarProductos(eq(1L), anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/productos/pyme/1/buscar?query=test"))
                .andExpect(status().isOk());
    }
}