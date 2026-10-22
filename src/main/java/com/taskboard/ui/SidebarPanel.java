package com.taskboard.ui;

import com.taskboard.model.Team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

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
    
    // Equipos que muestra el panel. Copia propia para no depender de la lista que le pasa el caller.
    private final List<Team> teams = new ArrayList<>();

    // Botones ya creados, uno por cada equipo de la lista
    private  final List<TeamButton> teamsButtons = new ArrayList<>();
    private String selectedTeam = "TaskBoard";

    //constructor
    public SidebarPanel(Consumer<String> onTeamSelected){    // Este constructor es temporal. No lo usara Main (Solo para debuggear)
        this(List.of(), onTeamSelected); 
    }
    
    public SidebarPanel(List<Team> teams, Consumer<String> onTeamSelected){
        this. onTeamSelected = onTeamSelected;
        this.teams.addAll(teams);

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
        styleButton(addButton);
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

        //Construyendo los equipos
        rebuildTeams();


        
        // Agregar elementos al subpanel
        navigation.add(teamsHeader);
        navigation.add(teamsContainer);

        
        // Panel Inferior. Aqui se muestra informacion muy basica sobre el perfil del usuario.

        //En este panel ira la info de perfil
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14,4,4,4));

        // Separador para el panel inferior y el medio.
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(51,59,75));
        separator.setBackground(new Color(51, 59, 75));

        JLabel avatar = new JLabel("U");
        avatar.setOpaque(true);
        avatar.setBackground(new Color(64,75,97));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD,15));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(36,36));

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));

        JLabel username = new JLabel("Usuario");
        username.setForeground(TEXT);
        username.setFont(new Font("SansSerif",Font.BOLD, 11));

        JLabel profile = new JLabel("Mi espacio");
        profile.setForeground(TEXT);
        profile.setFont(new Font("SansSerif", Font.PLAIN,11));

        userInfo.add(username);
        userInfo.add(Box.createVerticalStrut(4));
        userInfo.add(profile);

        footer.add(avatar, BorderLayout.WEST);
        footer.add(userInfo, BorderLayout.CENTER);

        //Subpanel Principal
        JPanel bottom = new JPanel(new BorderLayout(0,14));
        bottom.setOpaque(false);
        bottom.add(separator, BorderLayout.NORTH);
        bottom.add(footer, BorderLayout.CENTER);


        // Agregando los subpaneles al SidebarPanel principal
        add(header, BorderLayout.NORTH);
        add(navigation, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }


    private void addTeamButton(Team team){
        TeamButton button = new TeamButton(team);

        JLabel teamIcon = new JLabel(team.getIcon());
        teamIcon.setForeground(ACCENT);
        teamIcon.setFont(new Font("SansSerif", Font.BOLD,19));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel teamName = new JLabel(team.getName());
        teamName.setOpaque(false);
        teamName.setForeground(TEXT);
        teamName.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel count = new JLabel(taskCountText(team.getTaskCount()));
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

        teamsButtons.add(button);
        teamsContainer.add(button);
        teamsContainer.add(Box.createVerticalStrut(4));
    }
    public void setTeams(List<Team> teams){
        // Reemplaza la lista de equipos de todo el panel
        this.teams.clear();
        this.teams.addAll(teams);
        rebuildTeams();
    }

    public void addTeam(Team team){
        // Agrega un equipo nuevo al panel (Lo usara el boton +)
        teams.add(team);
        rebuildTeams();
    }

    private void rebuildTeams(){
        // Borra el contenedor y vuelve a pintar un boton por cada equipo
        teamsContainer.removeAll();
        teamsButtons.clear();

        for (Team team : teams){
            addTeamButton(team);
        }
        teamsContainer.revalidate();
        teamsContainer.repaint();
    }

    private static String taskCountText(int count){
        // Este metodo solo formatea el texto para mostrar tareas en singular o plural.
        return count + (count == 1 ? " tarea" : " tareas");
    }

    private void styleButton(JButton button){  
        // Este metodo es usado para darle estilo al boton de "+" en el panel del medio
        button.setBackground(BACKGROUND);
        button.setForeground(TEXT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11,10,11,10));
    }

    private static class TeamButton extends JButton {

        private final Team team;
        private boolean hovering;
        private boolean active;

        TeamButton(Team team){
            this.team=team;   // TeamName proviene del getter de model.Team

            // Dibujando el boton
            setLayout(new BorderLayout(10,0));
            setBorder(new EmptyBorder(9,10,9,8));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            setUI(new BasicButtonUI());

            addMouseListener(new MouseAdapter() {
                @Override 
                public void mouseEntered(MouseEvent e){
                    hovering = true;
                    repaint();
                }

                @Override 
                public void mouseExited(MouseEvent e){
                    hovering = false;
                    repaint();
                }
            });
        }

        Team getTeam(){
            return team;
        }

        void setActive(boolean active){
            this.active = active;
            repaint();
        }

        @Override 
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();

            super.paintComponent(g);
        }

        private Color backgroundColor(){
            if(active) {
                return SELECTED;
            }
            if (hovering){
                return HOVER;
            }
            return BACKGROUND;
        }
    }
}
