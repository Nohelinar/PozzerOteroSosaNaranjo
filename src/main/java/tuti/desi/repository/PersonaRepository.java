package tuti.desi.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tuti.desi.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p WHERE p.eliminado = false")
    List<Persona> findAllActivas();

    @Query("""
        SELECT p FROM Persona p
        WHERE p.eliminado = false
          AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:apellido IS NULL OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%')))
          AND (:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%')))
    """)
    List<Persona> filter(
        @Param("nombre") String nombre,
        @Param("apellido") String apellido,
        @Param("email") String email
    );
}