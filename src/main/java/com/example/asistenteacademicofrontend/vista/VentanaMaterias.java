package com.example.asistenteacademicofrontend.vista;

import com.example.asistenteacademicofrontend.cliente.MateriaCliente;
import com.example.asistenteacademicofrontend.dto.CompromisoDTO;
import com.example.asistenteacademicofrontend.dto.MateriaDTO;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

//CLASE: VentanaMaterias
@Component
public class VentanaMaterias extends JFrame {

    private final MateriaCliente materiaCliente;

    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final JTextField txtNombre = new JTextField(20);

    public VentanaMaterias(MateriaCliente materiaCliente) {
        this.materiaCliente = materiaCliente;

        setTitle("Asistente Académico - Materias");
        setSize(650, 400);
        setLocation(120, 120);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tabla.setDefaultEditor(Object.class, null);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
            }
        });

        JPanel formulario = new JPanel();
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        JPanel botones = new JPanel(new GridLayout(1, 5, 5, 5));
        botones.add(boton("Listar", () -> listar()));
        botones.add(boton("Crear", () -> crear()));
        botones.add(boton("Actualizar", () -> actualizar()));
        botones.add(boton("Eliminar", () -> eliminar()));
        botones.add(boton("Ver compromisos", () -> verCompromisos()));

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
            JOptionPane.showMessageDialog(this, "Selecciona una materia de la tabla");
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
            for (MateriaDTO m : materiaCliente.listar()) {
                modelo.addRow(new Object[]{m.getIdMateria(), m.getNombre()});
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void crear() {
        try {
            MateriaDTO m = new MateriaDTO();
            m.setNombre(txtNombre.getText());
            materiaCliente.crear(m);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            MateriaDTO m = new MateriaDTO();
            m.setNombre(txtNombre.getText());
            materiaCliente.actualizar(id, m);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            materiaCliente.eliminar(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void verCompromisos() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            StringBuilder texto = new StringBuilder();
            for (CompromisoDTO c : materiaCliente.compromisos(id)) {
                texto.append("- ").append(c.getTitulo()).append(" (").append(c.getEstado()).append(")\n");
            }
            JOptionPane.showMessageDialog(this, texto.length() == 0 ? "Sin compromisos" : texto.toString());
        } catch (Exception ex) {
            error(ex);
        }
    }
}