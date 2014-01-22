package view.module;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.BrowserModel;

import view.BrowserViewer;

public class InputPanelModule implements BrowserViewerModule {

  private BrowserViewer myParent;
  private BrowserModel myModel;

  private JTextField myURLDisplay;
  private JButton myBackButton;
  private JButton myNextButton;
  private JButton myHomeButton;

  private JButton myAddButton;
  private DefaultComboBoxModel myFavorites;
  private JComboBox myFavoritesDisplay;

  public InputPanelModule(BrowserViewer parent, BrowserModel model) {
    myParent = parent;
    myModel = model;
  }

  @Override
  public JComponent makeModule() {
    JPanel result = new JPanel(new BorderLayout());
    result.add(makeNavigationPanel(), BorderLayout.NORTH);
    result.add(makePreferencesPanel(), BorderLayout.SOUTH);
    return result;
  }
  
  public void setURLDisplayText(URL url) {
    myURLDisplay.setText(url.toString());
  }

  public void enableButtons() {
    myBackButton.setEnabled(myModel.hasPrevious());
    myNextButton.setEnabled(myModel.hasNext());
    myHomeButton.setEnabled(myModel.getHome() != null);
  }

  // move to the next URL in the history
  private void next() {
    myParent.update(myModel.next());
  }

  // move to the previous URL in the history
  private void back() {
    myParent.update(myModel.back());
  }

  // change current URL to the home page, if set
  private void home() {
    myParent.showPage(myModel.getHome().toString());
  }

  // make user-entered URL/text field and back/next buttons
  private JComponent makeNavigationPanel() {
    JPanel result = new JPanel();

    myBackButton = new JButton("Back");
    myBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        back();
      }
    });
    result.add(myBackButton);

    myNextButton = new JButton("Next");
    myNextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        next();
      }
    });
    result.add(myNextButton);

    myHomeButton = new JButton("Home");
    myHomeButton.addActionListener(new ActionListener() {
      @Override
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
      @Override
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
      @Override
      public void actionPerformed(ActionEvent e) {
        myModel.setHome();
        enableButtons();
      }
    });
    result.add(setHomeButton);

    return result;
  }
  
//prompt user for name of favorite to add to collection
 private void addFavorite() {
   String name =
       JOptionPane.showInputDialog(myParent, "Enter name", "Add Favorite",
           JOptionPane.QUESTION_MESSAGE);
   // did user make a choice?
   if (name != null) {
     myModel.addFavorite(name);
     myFavorites.addElement(name);
   }
 }

  /**
   * Inner class to factor out showing page associated with the entered URL
   */
  private class ShowPageAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      myParent.showPage(myURLDisplay.getText());
    }
  }

}
