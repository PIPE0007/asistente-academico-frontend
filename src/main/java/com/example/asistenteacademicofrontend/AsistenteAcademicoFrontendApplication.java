package com.example.asistenteacademicofrontend;

import com.example.asistenteacademicofrontend.vista.VentanaEstudiantes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import javax.swing.SwingUtilities;

//CLASE: AsistenteAcademicoFrontendApplication
@SpringBootApplication
public class AsistenteAcademicoFrontendApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AsistenteAcademicoFrontendApplication.class)
                .headless(false)
                .run(args);
    }

    @Bean
    public CommandLineRunner mostrarVentana(VentanaEstudiantes ventana) {
        return args -> SwingUtilities.invokeLater(() -> ventana.setVisible(true));
    }
}