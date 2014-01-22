package view;

import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.JPanel;

import view.module.InputPanelModule;
import view.module.PageDisplayModule;
import view.module.StatusModule;

import model.BrowserModel;



/**
 * A class used to display the viewer for a simple HTML browser.
 * 
 * See this tutorial for help on how to use the variety of components:
 * http://docs.oracle.com/javase/tutorial/uiswing/examples/components/
 * 
 * @author Owen Astrachan
 * @author Marcin Dobosz
 * @author Robert C. Duvall
 */
@SuppressWarnings("serial")
public class BrowserViewer extends JPanel {
  // constants
  public static final String BLANK = " ";

  // web page
  private PageDisplayModule myPageDisplay;
  // information area
  private StatusModule myStatus;
  // navigation
  private InputPanelModule myInputPanel;
  // the data
  private BrowserModel myModel;


  /**
   * Create a view of the given model of a web browser.
   */
  public BrowserViewer(BrowserModel model) {
    myModel = model;
    // add components to frame
    setLayout(new BorderLayout());
    // must be first since other panels may refer to page
    myPageDisplay = new PageDisplayModule(this, myModel);
    add(myPageDisplay.makeModule(), BorderLayout.CENTER);

    myInputPanel = new InputPanelModule(this, myModel);
    add(myInputPanel.makeModule(), BorderLayout.NORTH);

    myStatus = new StatusModule();
    add(myStatus.makeModule(), BorderLayout.SOUTH);

    // control the navigation
    enableButtons();
  }

  /**
   * Display given URL.
   */
  public void showPage(String url) {
    myPageDisplay.showPage(url);
  }

  /**
   * Display given message as information in the GUI.
   */
  public void showStatus(String message) {
    myStatus.setStatus(message);
  }

  // update just the view to display given URL
  public void update(URL url) {
    myPageDisplay.update(url);
  }

  // only enable buttons when useful to user
  public void enableButtons() {
    myInputPanel.enableButtons();
  }

  public void setURLDisplayText(URL url) {
    myInputPanel.setURLDisplayText(url);
  }

}
