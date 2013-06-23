package com.github.dreamrec;
import com.github.dreamrec.gcomponent.GComponentView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private Model model;
    private Controller controller;
    private GraphScrollBar graphScrollBar;
    private ActionMap actionMap;
    private ApplicationProperties applicationProperties;

    public MainWindow(Controller controller, Model model, ApplicationProperties applicationProperties) {
        this.controller = controller;
        this.model = model;
        this.applicationProperties = applicationProperties;
        actionMap = new GUIActions(controller).getActionMap();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        setTitle("DreamRec");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                controller.closeApplication();
                System.exit(0);
            }
        });

        mainPanel = new JPanel(new GridLayout(0, 1));

       // Filter<Short> fastDreamView = new FirstDerivativeAbsFilter(model.getEyeDataList());
       // mainPanel.add(Factory.getGComponentView(fastDreamView, model, controller));
        GComponentView acc1DataView = Factory.getGComponentView(model, controller, model.getAcc1DataList(), model.getAcc2DataList(), model.getAcc3DataList());
        mainPanel.add(acc1DataView);
        acc1DataView.getComponentModel().centreX();

//        GComponentView eyeDataView = Factory.getGComponentView(model, controller, new FirstDerivativeAbsFilter(model.getEyeDataList()));
        GComponentView eyeDataView = Factory.getGComponentView(model, controller, model.getEyeDataList());
//        GComponentView eyeDataView = Factory.getGComponentView(model, controller, model.getEyeDataList());
        mainPanel.add(eyeDataView);
        eyeDataView.getComponentModel().centreX();

        GComponentView ch2DataView = Factory.getGComponentView(model, controller, model.getCh2DataList());
        mainPanel.add(ch2DataView);
        ch2DataView.getComponentModel().centreX();

        /*Filter<Short> slowDreamView = new AveragingFilter(new FirstDerivativeAbsFilter(model.getEyeDataList()), Model.DIVIDER);
        mainPanel.add(Factory.getGComponentView(model, controller,slowDreamView));*/

        add(mainPanel, BorderLayout.CENTER);
        graphScrollBar = Factory.getSlowGraphScrollBar(model, controller);
        add(graphScrollBar, BorderLayout.SOUTH);
        setActionMap(actionMap);
        registerKeyActions();
        setJMenuBar(new MainMenu(actionMap, applicationProperties));
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);


    }


    public void setActionMap(ActionMap actionMap) {
        mainPanel.setActionMap(actionMap);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void registerKeyActions() {
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), SAVE_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), OPEN_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), SCROLL_CURSOR_BACKWARD_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), SCROLL_CURSOR_FORWARD_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK), START_RECORDING_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK), STOP_RECORDING_ACTION);
    }


}