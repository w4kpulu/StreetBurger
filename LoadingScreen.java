import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoadingScreen extends JWindow{
    private JProgressBar progressBar; //progess bar
    private Timer fadeInTimer; 
    private Timer progTimer; //progress timer
    private float opacity = 0f; //opacity
    private int progress = 0; //to show progress number
    private int width;
    private int height; //set the size of LoadingScreen

    public LoadingScreen(){
        width=400;
        height=300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Dimesion class already in java.awt.Dimension;
        int x =(screen.width-width)/2;
        int y =(screen.height-height)/2;
        setBounds(x, y, width, height); //Moves and resizes this component. The new location of the top-left corner is specified by x and y, and the new size is specified by width and height.


        //create a panel for loading screen
        JPanel content = new JPanel();
        content.setBackground(Color.cyan);
        content.setLayout(new BorderLayout());

        //Load logo image and resize it
        ImageIcon imageLogo = new ImageIcon("Krabby.png");
        Image image = imageLogo.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH); //resize image to 200*150
        imageLogo = new ImageIcon(image); //new imageLogo after resize

        //Add the new imageLogo to loading screen
        JLabel imageLabel = new JLabel(imageLogo);
        content.add(imageLabel, BorderLayout.CENTER);

        //add a label for loading text
        JLabel label = new JLabel("Street Burger", JLabel.CENTER);
        label.setFont(new Font("Cooper Black", Font.BOLD, 30));
        label.setForeground(Color.BLACK);
        content.add(label, BorderLayout.NORTH); //place text at top

        //create progress bar over time
        progressBar = new JProgressBar(0,100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(true); //show progress as percentage
        progressBar.setIndeterminate(false);
        content.add(progressBar, BorderLayout.SOUTH); //placed at bottom

        //add content to the splash screen
        add(content);

        //set intial opacity to 0 (invisible when start the program)
        setOpacity(opacity);

        //Fade-in timer (opacity from 0 to 1)
        fadeInTimer = new Timer(10, new ActionListener() {
            
            public void actionPerformed(ActionEvent event){
                if(opacity < 1f){
                    opacity +=0.05f; //increase opacity
                    opacity = Math.min(opacity,1.0f); //ensure opacity is not greater than 1.0
                    setOpacity(opacity);
                }
                else{
                    fadeInTimer.stop(); //stop fade in effect;
                }
            }
        });

        //progress timer

        progTimer = new Timer(50, new ActionListener() {
            
            public void actionPerformed(ActionEvent event){
                progress++; //increament by 1
                progressBar.setValue(progress);

                //stop the timer when progress reached 100
                if(progress>=100){
                    progTimer.stop();
                    setVisible(false);
                    dispose();

                    SwingUtilities.invokeLater(()->{
                        LogIn li = new LogIn();
                        li.LogInitial();
                    });
                }
            }
        });





    }

    public void showLoad(){
        fadeInTimer.start(); //start fade-in animation
        progTimer.start(); //start progress bar animation
    }

    public static void main(String[] args){
        LoadingScreen load = new LoadingScreen();
        load.setVisible(true);
        SwingUtilities.invokeLater(()-> load.showLoad()); //start loading animation after window is visible
    }
}
