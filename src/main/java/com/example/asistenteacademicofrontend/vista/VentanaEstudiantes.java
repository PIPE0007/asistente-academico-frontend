package com.example.asistenteacademicofrontend.vista;

import com.example.asistenteacademicofrontend.cliente.EstudianteCliente;
import com.example.asistenteacademicofrontend.cliente.PerfilCliente;
import com.example.asistenteacademicofrontend.dto.EstudianteDTO;
import com.example.asistenteacademicofrontend.dto.PerfilDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

//CLASE: VentanaEstudiantes
@Component
public class VentanaEstudiantes extends JFrame {

    private final EstudianteCliente estudianteCliente;
    private final PerfilCliente perfilCliente;

    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Correo"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final JTextField txtNombre = new JTextField(12);
    private final JTextField txtCorreo = new JTextField(12);
    private final JTextField txtCarrera = new JTextField(12);
    private final JTextField txtSemestre = new JTextField(4);

    public VentanaEstudiantes(EstudianteCliente estudianteCliente, PerfilCliente perfilCliente) {
        this.estudianteCliente = estudianteCliente;
        this.perfilCliente = perfilCliente;

        setTitle("Asistente Académico - Estudiantes");
        setSize(850, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabla.setDefaultEditor(Object.class, null);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
                txtCorreo.setText(String.valueOf(modelo.getValueAt(fila, 2)));
            }
        });

        JPanel formulario = new JPanel();
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Carrera:"));
        formulario.add(txtCarrera);
        formulario.add(new JLabel("Semestre:"));
        formulario.add(txtSemestre);

        JPanel botones = new JPanel(new GridLayout(2, 4, 5, 5));
        botones.add(boton("Listar", () -> listar()));
        botones.add(boton("Crear", () -> crear()));
        botones.add(boton("Actualizar", () -> actualizar()));
        botones.add(boton("Eliminar", () -> eliminar()));
        botones.add(boton("Ver perfil", () -> verPerfil()));
        botones.add(boton("Guardar perfil", () -> guardarPerfil()));
        botones.add(boton("Eliminar perfil", () -> eliminarPerfil()));

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla");
            return null;
        }
        return (Integer) modelo.getValueAt(fila, 0);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void listar() {
        try {
            modelo.setRowCount(0);
            for (EstudianteDTO e : estudianteCliente.listar()) {
                modelo.addRow(new Object[]{e.getIdEstudiante(), e.getNombre(), e.getCorreo()});
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void crear() {
        try {
            EstudianteDTO e = new EstudianteDTO();
            e.setNombre(txtNombre.getText());
            e.setCorreo(txtCorreo.getText());
            estudianteCliente.crear(e);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            EstudianteDTO e = new EstudianteDTO();
            e.setNombre(txtNombre.getText());
            e.setCorreo(txtCorreo.getText());
            estudianteCliente.actualizar(id, e);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            estudianteCliente.eliminar(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void verPerfil() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            PerfilDTO p = perfilCliente.obtener(id);
            txtCarrera.setText(p.getCarrera());
            txtSemestre.setText(String.valueOf(p.getSemestre()));
        } catch (HttpClientErrorException.NotFound nf) {
            txtCarrera.setText("");
            txtSemestre.setText("");
            JOptionPane.showMessageDialog(this, "Este estudiante no tiene perfil");
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void guardarPerfil() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            PerfilDTO p = new PerfilDTO();
            p.setCarrera(txtCarrera.getText());
            p.setSemestre(Integer.parseInt(txtSemestre.getText().trim()));
            try {
                perfilCliente.obtener(id);
                perfilCliente.actualizar(id, p);
            } catch (HttpClientErrorException.NotFound nf) {
                perfilCliente.crear(id, p);
            }
            JOptionPane.showMessageDialog(this, "Perfil guardado");
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void eliminarPerfil() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            perfilCliente.eliminar(id);
            txtCarrera.setText("");
            txtSemestre.setText("");
        } catch (Exception ex) {
            error(ex);
        }
    }
}