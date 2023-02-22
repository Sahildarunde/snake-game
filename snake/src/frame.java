import javax.swing.JFrame;

public class frame extends JFrame{
    
    frame(){
        this.add(new panel());
        this.setTitle("snakegame");
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}
