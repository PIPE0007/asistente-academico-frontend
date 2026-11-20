package com.example.asistenteacademicofrontend.vista;

import com.example.asistenteacademicofrontend.cliente.CompromisoCliente;
import com.example.asistenteacademicofrontend.dto.CompromisoDTO;
import com.example.asistenteacademicofrontend.dto.EstudianteDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//CLASE: VentanaCompromisos
@Component
public class VentanaCompromisos extends JFrame {

    private final CompromisoCliente compromisoCliente;
    private List<CompromisoDTO> lista = new ArrayList<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Título", "Fecha límite", "Prioridad", "Estado", "Estudiante", "Materia"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final JTextField txtTitulo = new JTextField(10);
    private final JTextField txtDescripcion = new JTextField(10);
    private final JTextField txtFecha = new JTextField(8);
    private final JTextField txtPrioridad = new JTextField(6);
    private final JTextField txtIdEstudiante = new JTextField(4);
    private final JTextField txtIdMateria = new JTextField(4);
    private final JTextField txtEstadoFiltro = new JTextField("pendiente", 8);

    public VentanaCompromisos(CompromisoCliente compromisoCliente) {
        this.compromisoCliente = compromisoCliente;

        setTitle("Asistente Académico - Compromisos");
        setSize(1000, 480);
        setLocation(140, 140);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tabla.setDefaultEditor(Object.class, null);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0 && fila < lista.size()) {
                CompromisoDTO c = lista.get(fila);
                txtTitulo.setText(c.getTitulo());
                txtDescripcion.setText(c.getDescripcion());
                txtFecha.setText(String.valueOf(c.getFechaLimite()));
                txtPrioridad.setText(c.getPrioridad());
                txtIdEstudiante.setText(c.getEstudiante() == null ? "" : String.valueOf(c.getEstudiante().getIdEstudiante()));
                txtIdMateria.setText(c.getMateria() == null ? "" : String.valueOf(c.getMateria().getIdMateria()));
            }
        });

        JPanel formulario = new JPanel();
        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);
        formulario.add(new JLabel("Descripción:"));
        formulario.add(txtDescripcion);
        formulario.add(new JLabel("Fecha (aaaa-mm-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Prioridad:"));
        formulario.add(txtPrioridad);
        formulario.add(new JLabel("ID estudiante:"));
        formulario.add(txtIdEstudiante);
        formulario.add(new JLabel("ID materia:"));
        formulario.add(txtIdMateria);
        formulario.add(new JLabel("Estado a filtrar:"));
        formulario.add(txtEstadoFiltro);

        JPanel botones = new JPanel(new GridLayout(1, 6, 5, 5));
        botones.add(boton("Listar", () -> listar()));
        botones.add(boton("Crear", () -> crear()));
        botones.add(boton("Actualizar", () -> actualizar()));
        botones.add(boton("Eliminar", () -> eliminar()));
        botones.add(boton("Completar", () -> completar()));
        botones.add(boton("Filtrar por estado", () -> filtrarPorEstado()));

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    private JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un compromiso de la tabla");
            return null;
        }
        return (Integer) modelo.getValueAt(fila, 0);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void llenarTabla(List<CompromisoDTO> datos) {
        lista = datos;
        modelo.setRowCount(0);
        for (CompromisoDTO c : datos) {
            modelo.addRow(new Object[]{
                    c.getIdCompromiso(), c.getTitulo(), c.getFechaLimite(), c.getPrioridad(), c.getEstado(),
                    c.getEstudiante() == null ? "" : c.getEstudiante().getNombre(),
                    c.getMateria() == null ? "" : c.getMateria().getNombre()});
        }
    }

    private CompromisoDTO leerFormulario() {
        CompromisoDTO c = new CompromisoDTO();
        c.setTitulo(txtTitulo.getText());
        c.setDescripcion(txtDescripcion.getText());
        c.setFechaLimite(LocalDate.parse(txtFecha.getText().trim()));
        c.setPrioridad(txtPrioridad.getText());
        EstudianteDTO e = new EstudianteDTO();
        e.setIdEstudiante(Integer.parseInt(txtIdEstudiante.getText().trim()));
        c.setEstudiante(e);
        MateriaDTO m = new MateriaDTO();
        m.setIdMateria(Integer.parseInt(txtIdMateria.getText().trim()));
        c.setMateria(m);
        return c;
    }

    private void listar() {
        try {
            llenarTabla(compromisoCliente.listar());
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void filtrarPorEstado() {
        try {
            llenarTabla(compromisoCliente.porEstado(txtEstadoFiltro.getText().trim()));
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void crear() {
        try {
            compromisoCliente.crear(leerFormulario());
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            CompromisoDTO c = leerFormulario();
            c.setEstado(lista.get(tabla.getSelectedRow()).getEstado());
            compromisoCliente.actualizar(id, c);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void completar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            compromisoCliente.completar(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            compromisoCliente.eliminar(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }
}