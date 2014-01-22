package view.module;

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import view.BrowserViewer;

import model.BrowserModel;


public class PageDisplayModule implements BrowserViewerModule {

  private static final Dimension SIZE = new Dimension(800, 600);
  private static final String PROTOCOL_PREFIX = "http://";

  private final BrowserViewer myParent;
  private final BrowserModel myModel;
  private JEditorPane myPage;

  public PageDisplayModule(BrowserViewer parent, BrowserModel model) {
    myParent = parent;
    myModel = model;
  }

  @Override
  public JComponent makeModule() {
    // displays the web page
    myPage = new JEditorPane();
    myPage.setPreferredSize(SIZE);
    // allow editor to respond to link-clicks/mouse-overs
    myPage.setEditable(false);
    myPage.addHyperlinkListener(new LinkFollower());
    return new JScrollPane(myPage);
  }

  public void showPage(String url) {
    try {
      if (url != null) {
        // check for a valid URL before updating model, view
        URL valid = new URL(completeURL(url));
        myModel.go(valid);
        update(valid);
      }
    } catch (MalformedURLException e) {
      showError("Could not load " + url);
    }
  }

  public void update(URL url) {
    try {
      myPage.setPage(url);
      myParent.setURLDisplayText(url);
      myParent.enableButtons();
    } catch (IOException e) {
      // should never happen since only checked URLs make it this far ...
      showError("Could not load " + url);
    }
  }

  // deal with a potentially incomplete URL,
  // e.g., let user leave off initial protocol
  private String completeURL(String url) {
    if (!url.startsWith(PROTOCOL_PREFIX)) {
      return PROTOCOL_PREFIX + url;
    }
    return url;
  }

  /**
   * Display given message as an error in the GUI.
   */
  private void showError(String message) {
    JOptionPane.showMessageDialog(myParent, message, "Browser Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Inner class to deal with link-clicks and mouse-overs
   */
  private class LinkFollower implements HyperlinkListener {
    @Override
    public void hyperlinkUpdate(HyperlinkEvent evt) {
      // user clicked a link, load it and show it
      if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        myParent.showPage(evt.getURL().toString());
      }
      // user moused-into a link, show what would load
      else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
        myParent.showStatus(evt.getURL().toString());
      }
      // user moused-out of a link, erase what was shown
      else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
        myParent.showStatus(BrowserViewer.BLANK);
      }
    }
  }

}
