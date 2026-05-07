package cl.pymetrack.msproductos.producto.repository;

import cl.pymetrack.msproductos.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByIdPymeAndActivoOrderByNombreProducto(Long idPyme, Boolean activo);
    
    List<Producto> findByIdPymeAndActivoTrue(Long idPyme);
    
    Optional<Producto> findByIdPymeAndCodigoSKU(Long idPyme, String codigoSKU);
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.idPyme = :pymeId AND p.activo = true")
    Integer countByPymeIdAndActivo(@Param("pymeId") Long pymeId);
    
    boolean existsByIdPymeAndCodigoSKU(Long idPyme, String codigoSKU);
    
    @Query("SELECT p FROM Producto p WHERE p.idPyme = :pymeId AND p.activo = true AND (LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.codigoSKU) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Producto> buscarPorPymeYQuery(@Param("pymeId") Long pymeId, @Param("query") String query);
}
