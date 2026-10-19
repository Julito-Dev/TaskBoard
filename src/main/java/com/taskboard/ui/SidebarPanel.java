package com.taskboard.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
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


        // Parte media: Aqui se muestran los equipos
        teamsContainer = new JPanel();
        teamsContainer.setOpaque(false);
        teamsContainer.setLayout(new BoxLayout(teamsContainer, BoxLayout.Y_AXIS));

        // Agregar botones luego...
    }




}
