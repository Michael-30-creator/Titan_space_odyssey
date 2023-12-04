package SolarSystem;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import PhysicsEngine.Simulations.Simulation;
import PhysicsEngine.Solvers.*;

public class Main {
    
    public static  String[] solvers = {"Euler" , "Runge-Kutta4" , "Runge-Kutta2", "Adams-Bashforth" , "Heuns" };
    static iSolver solver;
    static Simulation sim;
    static JFrame solverChooser;
        public static void main(String[] args)  {

        //label with the project title and our group
        JLabel intro = new JLabel("<html>    Project Group 15<br> A Titanic Space Odyssey!</html>") ;
        intro.setFont(new Font("Consolas",Font.BOLD,16));

        //explanatory note on how to use the zoomIn/zoomOut functionality
        JLabel tip = new JLabel("Use arrow up/down to zoom in/out");
        tip.setFont(new Font("Consolas",Font.PLAIN,10));
        tip.setForeground(Color.RED);

        JLabel label = new JLabel("Select solver");
        label.setFont(new Font("Consolas",Font.PLAIN,12));
    
        //offers the choice of any solver for the simulation we are running
        JComboBox<String> chooser = new JComboBox<>(solvers);
        chooser.setSelectedItem(null);
        chooser.setFont(new Font("Consolas",Font.PLAIN,12));

        //button that starts the simulation
        JButton runButton = new JButton("Run");
        runButton.setFont(new Font("Consolas",Font.PLAIN,12));

        //main frame
        solverChooser = new JFrame();
        solverChooser.setSize(350,225);
        solverChooser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        solverChooser.setLocationRelativeTo(null);
        solverChooser.setUndecorated(true);
        solverChooser.setLayout(new FlowLayout(FlowLayout.CENTER,150,20));
        solverChooser.getContentPane().setBackground(new Color(255,255,255));
        solverChooser.add(intro);
        solverChooser.add(tip);
        solverChooser.add(label);
        solverChooser.add(chooser);
        solverChooser.add(runButton);
        solverChooser.setVisible(true);
        
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            if(e.getSource()== runButton){
                if(chooser.getSelectedItem() == null)
                JOptionPane.showMessageDialog(null, "You have not selected a valid solver", "Error", JOptionPane.INFORMATION_MESSAGE);
                else{
                String value = chooser.getSelectedItem().toString();

                switch (value) {
                case "Euler":
                    solver = new EulerSolver();
                    break;
                case "Runge-Kutta4":
                    solver = new RungeKutta4Solver();
                    break;
                case "Runge-Kutta2":
                    solver = new RungeKuttaSolver(2);
                    break;
                case "Adam-Bashforth":
                    solver = new AdamBashforthSolver();
                    break;
                case "Heuns":
                    solver = new HeunsSolver();
                    break;
                default:
                    break;
                }

            solverChooser.dispose();
            //start the simulation
            Runner.run(solver);
                }
            }
            }
        });
    }
}
