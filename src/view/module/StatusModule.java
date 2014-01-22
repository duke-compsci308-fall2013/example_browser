package view.module;

import javax.swing.JComponent;
import javax.swing.JLabel;
import view.BrowserViewer;


public class StatusModule implements BrowserViewerModule {

    private JLabel myStatus;

    @Override
    public JComponent makeModule () {
        // JLabel initial contents must be non-empty or status label will not be
        // displayed in GUI
        myStatus = new JLabel(BrowserViewer.BLANK);
        return myStatus;
    }

    public void setStatus (String status) {
        myStatus.setText(status);
    }

}
