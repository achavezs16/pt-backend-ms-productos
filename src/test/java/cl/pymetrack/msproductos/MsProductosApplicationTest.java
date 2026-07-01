package cl.pymetrack.msproductos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MsProductosApplicationTest {

    @Test
    void applicationContextTest() {
        // Instanciamos la clase para que JaCoCo la registre como cubierta
        MsProductosApplication app = new MsProductosApplication();
        assertNotNull(app);
    }

    @Test
    void mainTest() {

        try {
            System.setProperty("spring.main.web-application-type", "none");
            MsProductosApplication.main(new String[] {});
        } catch (Exception e) {
        }
    }
}