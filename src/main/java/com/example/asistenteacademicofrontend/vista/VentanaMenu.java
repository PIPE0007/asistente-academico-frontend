package com.example.asistenteacademicofrontend.vista;

import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;

//CLASE: VentanaMenu
@Component
public class VentanaMenu extends JFrame {

    public VentanaMenu(VentanaEstudiantes estudiantes, VentanaMaterias materias,
                       VentanaCompromisos compromisos, VentanaRecordatorios recordatorios) {
        setTitle("Asistente Académico Virtual");
        setSize(380, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("Menú principal", SwingConstants.CENTER);

        JPanel botones = new JPanel(new GridLayout(4, 1, 8, 8));
        botones.add(boton("Estudiantes", estudiantes));
        botones.add(boton("Materias", materias));
        botones.add(boton("Compromisos", compromisos));
        botones.add(boton("Recordatorios", recordatorios));

        add(titulo, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
    }

    private JButton boton(String texto, JFrame ventana) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> {
            ventana.setVisible(true);
            ventana.toFront();
        });
        return boton;
    }
}