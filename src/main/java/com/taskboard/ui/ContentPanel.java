package com.taskboard.ui;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import com.taskboard.model.Team;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
public class ContentPanel extends JPanel{

    // Colores utiles
     private static final Color BACKGROUND = new Color(23,28,40);
    private static final Color HOVER = new Color(42,52,74);
    private static final Color SELECTED = new Color(48,59,83);
    private static final Color TEXT = new Color(231, 235, 244);
    private static final Color MUTED = new Color(151, 163, 185);
    private static final Color ACCENT = new Color(126, 151, 255);


    private final JLabel title;

    // This fields are used to show the team info
    private final JLabel nameLabel = new JLabel();
    private final JLabel idLabel = new JLabel();
    private final JLabel taskcountLabel = new JLabel();


    private final JPanel board;
    private final BoardColumn[] columns;
    private final Consumer<TaskStatus> onAddTask;
    private final BiConsumer<Task, TaskStatus> onMoveTask;


    public ContentPanel(){
        this(status -> { }, (task, status) -> { });
    }


    public ContentPanel(Consumer<TaskStatus> onAddTask, BiConsumer<Task, TaskStatus> onMoveTask){
        this.onAddTask = onAddTask;
        this.onMoveTask = onMoveTask;

        setLayout(new BorderLayout());
        setBackground(new Color(35, 41, 58));

        title = new JLabel("Selecciona un equipo");
        title.setForeground(new Color(231,235,244));
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(24, 24, 16, 24));

        
        // Team Info Panel

        JPanel teamInfoPanel = new JPanel();
        teamInfoPanel.setLayout(new BoxLayout(teamInfoPanel, BoxLayout.Y_AXIS));
        teamInfoPanel.setOpaque(false);
        teamInfoPanel.setBorder(new EmptyBorder(0,24,24,24));

        teamInfoPanel.add(nameLabel);
        teamInfoPanel.add(Box.createVerticalStrut(8));
        teamInfoPanel.add(idLabel);
        teamInfoPanel.add(Box.createVerticalStrut(8));
        teamInfoPanel.add(taskcountLabel);

        
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        top.add(title);
        top.add(teamInfoPanel);
        add(top, BorderLayout.NORTH);

        columns = new BoardColumn[] {
            new BoardColumn("Por Hacer", TaskStatus.TODO, onAddTask, onMoveTask),
            new BoardColumn("En Progreso", TaskStatus.IN_PROGRESS, onAddTask, onMoveTask),
            new BoardColumn("Hecha", TaskStatus.DONE, onAddTask, onMoveTask)
        };
            

        board = new JPanel(new GridLayout(1,3,12,0));
        board.setOpaque(false);
        board.setBorder(new EmptyBorder(12,24,24,24));
        for (BoardColumn column : columns){
            board.add(column);
        }
        board.setVisible(false);
        add(board, BorderLayout.CENTER);
    }
    public void showTeam(Team team){
        // Muestra el equipo y su informacion en el Sidebar
        showTeam(team, List.of());
    }

    public void showTeam(Team team, List<Task> tasks){
        title.setText(team.getName());
        nameLabel.setText("Nombre: " + team.getName());
        nameLabel.setForeground(TEXT);
        idLabel.setText("ID: " + team.getId());
        idLabel.setForeground(TEXT);
        taskcountLabel.setText("Tareas: " + tasks.size());
        taskcountLabel.setForeground(TEXT);

        for (BoardColumn column : columns){
            TaskStatus status = column.getStatus();
            column.setTasks(tasks.stream().filter(t -> t.getStatus() == status).toList());
        }

        board.setVisible(true);
        revalidate();
        repaint();
    }

    private static void styleButton(JButton button, int size){
        button.setForeground(ACCENT);
        button.setFont(new Font("SansSerif", Font.BOLD, size));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
    }

    private static JPanel createCard(Task task, BiConsumer<Task, TaskStatus> onMoveTask){
        JPanel card = new JPanel(new BorderLayout(0,4));
        card.setBackground(HOVER);
        card.setBorder(new EmptyBorder(10,12,10,12));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel cardTitle = new JLabel(task.getTitle());
        cardTitle.setForeground(TEXT);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
            
        card.add(cardTitle, BorderLayout.NORTH);

        if (task.getDescription() != null && !task.getDescription().isBlank()) {
        JLabel desc = new JLabel(task.getDescription());
        desc.setForeground(MUTED);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        card.add(desc, BorderLayout.CENTER);
        }
            
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,4,0));
        actions.setOpaque(false);
        JButton prev = new JButton("<<");
        JButton next = new JButton(">>");
        styleButton(prev, 14);
        styleButton(next, 14);

        TaskStatus[] values = TaskStatus.values();
        int index = task.getStatus().ordinal();
        prev.setEnabled(index > 0);
        next.setEnabled(index < values.length -1);

        prev.addActionListener(e -> {
            if (index > 0 ){
                onMoveTask.accept(task, values[index-1]);
            }
        });
        next.addActionListener(e -> {
            if (index < values.length -1 ){
                onMoveTask.accept(task, values[index +1]);
            }
        });

        actions.add(prev);
        actions.add(next);
        card.add(actions, BorderLayout.SOUTH);
        

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        return card;
    }

    private static class BoardColumn extends JPanel {
        private final JPanel cards = new JPanel();
        private final TaskStatus status;
        private final BiConsumer<Task, TaskStatus> onMoveTask;

        BoardColumn(String label, TaskStatus status, Consumer<TaskStatus> onAddTask, BiConsumer<Task, TaskStatus> onMoveTask) {
            this.status = status;
            this.onMoveTask = onMoveTask;
            setLayout(new BorderLayout(0, 8));
            setOpaque(false);

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);

            JLabel header = new JLabel(label);
            header.setForeground(TEXT);
            header.setFont(new Font("SansSerif", Font.BOLD, 13));

            JButton addButton = new JButton("+");
            styleButton(addButton, 18);
            addButton.setToolTipText("Añadir tarea");
            addButton.addActionListener(e -> onAddTask.accept(status));

            headerPanel.add(header, BorderLayout.WEST);
            headerPanel.add(addButton, BorderLayout.EAST);
            add(headerPanel, BorderLayout.NORTH);

            cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
            cards.setOpaque(false);

            JScrollPane scroll = new JScrollPane(cards);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            add(scroll, BorderLayout.CENTER);
        }

        TaskStatus getStatus() {
            return status;
        }

        void setTasks(List<Task> tasks) {
            cards.removeAll();

            if (tasks.isEmpty()) {
                JLabel empty = new JLabel("Sin tareas");
                empty.setForeground(MUTED);
                empty.setFont(new Font("SansSerif", Font.ITALIC, 12));
                empty.setAlignmentX(LEFT_ALIGNMENT);
                cards.add(empty);
            } else {
                for (Task task : tasks) {
                    cards.add(createCard(task, onMoveTask));
                    cards.add(Box.createVerticalStrut(8));
                }
            }

            cards.add(Box.createVerticalGlue());
            cards.revalidate();
            cards.repaint();
        }
    }
}
