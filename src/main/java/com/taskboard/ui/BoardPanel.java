package src.main.java.com.taskboard.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class BoardPanel extends JPanel {
    private static final Color BACKGROUND = new Color(35, 41, 58);
    private static final Color COLUMN = new Color(23, 28, 40);
    private static final Color CARD = new Color(48, 59, 83);
    private static final Color TEXT = new Color(231, 235, 244);
    private static final Color MUTED = new Color(151, 163, 185);

    public BoardPanel() {
        setLayout(new GridLayout(1, 3, 16, 0));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(0, 24, 24, 24));

        // Tareas de prueba. Esto no deberia ir aca (Solo demo)
        add(createColumn("Por hacer", List.of("Crear base de datos", "Definir roles del equipo")));
        add(createColumn("En progreso", List.of("Vista del tablero")));
        add(createColumn("Hecho", List.of("Sidebar de equipos", "Ventana principal")));
    }

    private JPanel createColumn(String title, List<String> tasks) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(COLUMN);
        column.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel header = new JLabel(title + "  " + tasks.size());
        header.setForeground(MUTED);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setAlignmentX(LEFT_ALIGNMENT);
        column.add(header);
        column.add(Box.createVerticalStrut(12));

        for (String task : tasks) {
            column.add(createCard(task));
            column.add(Box.createVerticalStrut(8));
        }

        column.add(Box.createVerticalGlue());
        return column;
    }

    private JPanel createCard(String text) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(label, BorderLayout.CENTER);

        // Evita que la tarjeta se estire en vertical dentro del BoxLayout
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        return card;
    }
}