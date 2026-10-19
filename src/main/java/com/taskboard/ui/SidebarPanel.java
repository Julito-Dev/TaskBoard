package com.taskboard.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SidebarPanel extends JPanel {

    // Este panel representa la parte izquierda de la ventana principal.
    // Aqui se encuentra el nombre de la aplicacion, los grupos a los que el usuario pertenece y el acceso al perfil.


    // Colores utiles 
    private static final Color BACKGROUND = new Color(23,28,40);
    private static final Color HOVER = new Color(42,52,74);
    private static final Color SELECTED = new Color(48,59,83);
    private static final Color TEXT = new Color(231, 235, 244);
    private static final Color MUTED = new Color(151, 163, 185);
    private static final Color ACCENT = new Color(126, 151, 255);

    //
    private final JPanel teamsContainer;
    private final Consumer<String> onTeamSelected;  // Esta funcion recibe un String y no retorna nada, muy util para conectarlo con el
                                                    // Frame principal y avisarle sobre que equipo selecciono el usuario.

    private String selectedTeam = "TaskBoard";

    //constructor
    public SidebarPanel(Consumer<String> onTeamSelected){
        this.onTeamSelected = onTeamSelected;
        
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(22, 14, 14, 14));
        setPreferredSize(new Dimension(255, 600));

        // Parte superior, aqui se muestra el titulo y el "logo"
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 4, 28, 4));


        JLabel logo = new JLabel("✓");
        logo.setOpaque(true);
        logo.setBackground(ACCENT);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(42, 42));

        // Panel que contiene el titulo y subtitulo
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TaskBoard");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD,19));

        JLabel subtitle = new JLabel("WORKSPACE");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN,10));

        brand.add(title);
        brand.add(Box.createVerticalStrut(4));
        brand.add(subtitle);

        header.add(logo, BorderLayout.WEST);
        header.add(brand, BorderLayout.CENTER);

        // Parte media: Navegacion general, Aqui se muestran los equipos y su info

        //Contenedor principal
        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        

        // Cabecera para la seccion de equipos
        JPanel teamsHeader = new JPanel();
        teamsHeader.setOpaque(false);
        teamsHeader.setBorder(new EmptyBorder(26,5,12,3));

        JLabel teamsLabel = new JLabel("Tus Equipos");
        teamsLabel.setForeground(MUTED);
        teamsLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

        JButton addButton = new JButton("+"); 
        addButton.setForeground(ACCENT);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        addButton.setToolTipText("Añadir Equipo");
        // Boton sin funcion. Falta agregar el actionListener

        teamsHeader.add(teamsLabel, BorderLayout.WEST);
        teamsHeader.add(addButton, BorderLayout.EAST);



        // Panel para la lista de equipos
        teamsContainer = new JPanel();
        teamsContainer.setOpaque(false);
        teamsContainer.setLayout(new BoxLayout(teamsContainer, BoxLayout.Y_AXIS));

        // Equipos. Lo ideal seria que el constructor recibiera los equipos List<Equipo> equipos y los agregara con el metodo addTeamButton()
        // Sin embargo por ahora (No hay persistencia), los equipos se muestran HardCodeados.
        
        addTeamButton("Universidad", "U", "6 Tareas");
        addTeamButton("Proyecto Messi", "MS10", "1 tarea");

        
        // Agregar elementos al subpanel
        navigation.add(teamsHeader);
        navigation.add(teamsContainer);







        // Agregando los subpaneles al SidebarPanel principal
        add(header, BorderLayout.NORTH);
        add(navigation, BorderLayout.CENTER);
    }


    private void addTeamButton(String name, String icon, String taskCount){
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10,0));
        button.setBorder(new EmptyBorder(9,10,9,8));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel teamIcon = new JLabel(icon);
        teamIcon.setForeground(ACCENT);
        teamIcon.setFont(new Font("SansSerif", Font.BOLD,19));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel teamName = new JLabel(name);
        teamName.setOpaque(false);
        teamName.setForeground(TEXT);
        teamName.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel count = new JLabel(taskCount);
        count.setForeground(MUTED);
        count.setFont(new Font("SansSerif", Font.BOLD, 10));

        details.add(teamName);
        details.add(Box.createVerticalStrut(4));
        details.add(count);

        JLabel arrow = new JLabel(">");
        arrow.setForeground(MUTED);
        arrow.setFont(new Font("SansSerif", Font.PLAIN, 22));

        button.add(teamIcon, BorderLayout.WEST);
        button.add(details, BorderLayout.CENTER);
        button.add(arrow, BorderLayout.EAST);

        teamsContainer.add(button);
        teamsContainer.add(Box.createVerticalStrut(4));
    }



}
