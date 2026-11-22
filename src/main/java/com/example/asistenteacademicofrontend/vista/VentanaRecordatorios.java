package com.example.asistenteacademicofrontend.vista;

import com.example.asistenteacademicofrontend.cliente.RecordatorioCliente;
import com.example.asistenteacademicofrontend.dto.RecordatorioDTO;
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

//CLASE: VentanaRecordatorios
@Component
public class VentanaRecordatorios extends JFrame {

    private final RecordatorioCliente recordatorioCliente;
    private List<RecordatorioDTO> lista = new ArrayList<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Mensaje", "Fecha", "Enviado", "ID compromiso"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final JTextField txtMensaje = new JTextField(18);
    private final JTextField txtFecha = new JTextField(8);
    private final JTextField txtIdCompromiso = new JTextField(4);

    public VentanaRecordatorios(RecordatorioCliente recordatorioCliente) {
        this.recordatorioCliente = recordatorioCliente;

        setTitle("Asistente Académico - Recordatorios");
        setSize(900, 420);
        setLocation(160, 160);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tabla.setDefaultEditor(Object.class, null);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0 && fila < lista.size()) {
                RecordatorioDTO r = lista.get(fila);
                txtMensaje.setText(r.getMensaje());
                txtFecha.setText(String.valueOf(r.getFechaRecordatorio()));
                txtIdCompromiso.setText(String.valueOf(r.getIdCompromiso()));
            }
        });

        JPanel formulario = new JPanel();
        formulario.add(new JLabel("Mensaje:"));
        formulario.add(txtMensaje);
        formulario.add(new JLabel("Fecha (aaaa-mm-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("ID compromiso:"));
        formulario.add(txtIdCompromiso);

        JPanel botones = new JPanel(new GridLayout(1, 6, 5, 5));
        botones.add(boton("Listar", () -> listar()));
        botones.add(boton("Crear", () -> crear()));
        botones.add(boton("Actualizar", () -> actualizar()));
        botones.add(boton("Eliminar", () -> eliminar()));
        botones.add(boton("Marcar enviado", () -> marcarEnviado()));
        botones.add(boton("Filtrar por compromiso", () -> filtrarPorCompromiso()));

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
            JOptionPane.showMessageDialog(this, "Selecciona un recordatorio de la tabla");
            return null;
        }
        return (Integer) modelo.getValueAt(fila, 0);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void llenarTabla(List<RecordatorioDTO> datos) {
        lista = datos;
        modelo.setRowCount(0);
        for (RecordatorioDTO r : datos) {
            modelo.addRow(new Object[]{r.getIdRecordatorio(), r.getMensaje(), r.getFechaRecordatorio(),
                    r.isEnviado() ? "Sí" : "No", r.getIdCompromiso()});
        }
    }

    private RecordatorioDTO leerFormulario() {
        RecordatorioDTO r = new RecordatorioDTO();
        r.setMensaje(txtMensaje.getText());
        r.setFechaRecordatorio(LocalDate.parse(txtFecha.getText().trim()));
        r.setIdCompromiso(Integer.parseInt(txtIdCompromiso.getText().trim()));
        return r;
    }

    private void listar() {
        try {
            llenarTabla(recordatorioCliente.listar());
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void filtrarPorCompromiso() {
        try {
            Integer idCompromiso = Integer.parseInt(txtIdCompromiso.getText().trim());
            llenarTabla(recordatorioCliente.porCompromiso(idCompromiso));
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void crear() {
        try {
            recordatorioCliente.crear(leerFormulario());
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            RecordatorioDTO r = leerFormulario();
            r.setEnviado(lista.get(tabla.getSelectedRow()).isEnviado());
            recordatorioCliente.actualizar(id, r);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void marcarEnviado() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            recordatorioCliente.marcarEnviado(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        try {
            recordatorioCliente.eliminar(id);
            listar();
        } catch (Exception ex) {
            error(ex);
        }
    }
}
