package serverlang;

import com.ui.MainFrame;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class ServerLang {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

       EventQueue.invokeLater(new Runnable() {
           @Override
           public void run() {
               try {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initLog();
                    mainFrame.init();
                    mainFrame.setVisible(true);
               } catch (Exception ex) {
                   Logger.getLogger(ServerLang.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       });

    }

}
