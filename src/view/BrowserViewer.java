package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.module.PageDisplayModule;

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
  // private JEditorPane myPage;
  // information area
  private JLabel myStatus;
  // navigation
  private JTextField myURLDisplay;
  private JButton myBackButton;
  private JButton myNextButton;
  private JButton myHomeButton;
  // favorites
  private JButton myAddButton;
  private DefaultComboBoxModel myFavorites;
  private JComboBox myFavoritesDisplay;
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
    add(new PageDisplayModule(this, myModel).makeModule(), BorderLayout.CENTER);
    add(makeInputPanel(), BorderLayout.NORTH);
    add(makeInformationPanel(), BorderLayout.SOUTH);
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
    myStatus.setText(message);
  }

  // move to the next URL in the history
  private void next() {
    update(myModel.next());
  }

  // move to the previous URL in the history
  private void back() {
    update(myModel.back());
  }

  // change current URL to the home page, if set
  private void home() {
    showPage(myModel.getHome().toString());
  }

  // update just the view to display given URL
  private void update(URL url) {
    myPageDisplay.update(url);
  }

  // prompt user for name of favorite to add to collection
  private void addFavorite() {
    String name =
        JOptionPane.showInputDialog(this, "Enter name", "Add Favorite",
            JOptionPane.QUESTION_MESSAGE);
    // did user make a choice?
    if (name != null) {
      myModel.addFavorite(name);
      myFavorites.addElement(name);
    }
  }


  // only enable buttons when useful to user
  public void enableButtons() {
    myBackButton.setEnabled(myModel.hasPrevious());
    myNextButton.setEnabled(myModel.hasNext());
    myHomeButton.setEnabled(myModel.getHome() != null);
  }

  public void setURLDisplayText(URL url) {
    myURLDisplay.setText(url.toString());
  }

  // organize user's options for controlling/giving input to model
  private JComponent makeInputPanel() {
    JPanel result = new JPanel(new BorderLayout());
    result.add(makeNavigationPanel(), BorderLayout.NORTH);
    result.add(makePreferencesPanel(), BorderLayout.SOUTH);
    return result;
  }

  // make the panel where "would-be" clicked URL is displayed
  private JComponent makeInformationPanel() {
    // BLANK must be non-empty or status label will not be displayed in GUI
    myStatus = new JLabel(BLANK);
    return myStatus;
  }

  // make user-entered URL/text field and back/next buttons
  private JComponent makeNavigationPanel() {
    JPanel result = new JPanel();

    myBackButton = new JButton("Back");
    myBackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        back();
      }
    });
    result.add(myBackButton);

    myNextButton = new JButton("Next");
    myNextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        next();
      }
    });
    result.add(myNextButton);

    myHomeButton = new JButton("Home");
    myHomeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        home();
      }
    });
    result.add(myHomeButton);

    // if user presses button, load/show the URL
    JButton goButton = new JButton("Go");
    goButton.addActionListener(new ShowPageAction());
    result.add(goButton);

    // if user presses return, load/show the URL
    myURLDisplay = new JTextField(35);
    myURLDisplay.addActionListener(new ShowPageAction());
    result.add(myURLDisplay);

    return result;
  }

  // make buttons for setting favorites/home URLs
  private JComponent makePreferencesPanel() {
    JPanel result = new JPanel();

    myAddButton = new JButton("Add Favorite");
    myAddButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addFavorite();
      }
    });
    result.add(myAddButton);

    myFavorites = new DefaultComboBoxModel();
    myFavorites.addElement(" All Favorites ");
    myFavoritesDisplay = new JComboBox(myFavorites);
    result.add(myFavoritesDisplay);

    JButton setHomeButton = new JButton("Set Home");
    setHomeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        myModel.setHome();
        enableButtons();
      }
    });
    result.add(setHomeButton);

    return result;
  }

  /**
   * Inner class to factor out showing page associated with the entered URL
   */
  private class ShowPageAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      showPage(myURLDisplay.getText());
    }
  }


}
