package ui;

import model.Event;
import model.EventLog;
import model.Movie;
import model.MovieCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// This class references code demo from this website
// Link:https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// Represents application's main window frame.
public class MovieReviewAppUI extends JFrame implements ListSelectionListener {
    private static final String JSON_STORE = "./data/collection.json";
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private static final double CONTENT_PANEL_WIDTH_PERCENTAGE = 0.75;
    private static final int CONTENT_PANEL_WIDTH = (int) Math.floor(WIDTH * CONTENT_PANEL_WIDTH_PERCENTAGE);
    private static final int BUTTON_PANEL_WIDTH = WIDTH - CONTENT_PANEL_WIDTH;
    private static final int BUTTON_WIDTH = (int) Math.floor(BUTTON_PANEL_WIDTH * 0.8);
    private static final int BUTTON_HEIGHT = 30;
    public static final String addString = "ADD MOVIE";
    public static final String deleteString = "DELETE MOVIE";
    public static final String rateString = "UPDATE RATING";
    public static final String reviewString = "UPDATE REVIEW";
    public static final Font FONT = new Font("Monospaced", Font.PLAIN, 13);
    private MovieCollection mc;
    private JButton deleteButton;
    private JList movieList;
    private JTextField movieName;
    private DefaultListModel listModel;
    private boolean isLoaded;
    private boolean toUpdateRating;
    private boolean toUpdateReview;
    private Integer newRating;
    private String newReview;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: sets up main window in which movie review application will be displayed
    public MovieReviewAppUI() {
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        setTitle("Movie review app");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreOnScreen();

        openDialog();
        JPanel contentPanel = setUpContentPanel();
        add(contentPanel, BorderLayout.EAST);

        mc = new MovieCollection();
        loadCollection();

        setUpButtonPanel();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
                System.out.println("Closed");
                e.getWindow().dispose();
                printLog(EventLog.getInstance());
            }
        });
        setVisible(true);
    }

    // EFFECTS: load the previous collection if user chose to
    private void loadCollection() {
        if (isLoaded) {
            loadPreviousCollection();
        }
    }

    // EFFECTS: print every event log
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n\n");
        }
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: create and set up a title panel
    private JPanel setUpTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#C7CEDE"));
        titlePanel.setPreferredSize(new Dimension(CONTENT_PANEL_WIDTH, 50));
        JLabel titleLabel = new JLabel("Your Movie Collection\n");
        JLabel headerLabel = new JLabel(
                String.format("| %25s|  | %s |  | %-30s |", "name", "rating", "review"), SwingConstants.RIGHT);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerLabel.setFont(FONT);
        titleLabel.setLayout(new BoxLayout(titleLabel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(headerLabel);
        return titlePanel;
    }

    // MODIFIES: this
    // EFFECTS: create and set up content panel
    private JPanel setUpContentPanel() {
        JPanel movieCollectionPanel = new JPanel();
        movieCollectionPanel.setBackground(Color.decode("#C7CEDE"));
        movieCollectionPanel.setPreferredSize(new Dimension(CONTENT_PANEL_WIDTH, 550));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JPanel titlePanel = setUpTitlePanel();
        contentPanel.add(titlePanel);
        contentPanel.add(movieCollectionPanel);

        listModel = new DefaultListModel();
        movieList = new JList(listModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setSelectedIndex(0);
        movieList.setFixedCellWidth(CONTENT_PANEL_WIDTH);
        movieList.addListSelectionListener(this);
        movieList.setVisibleRowCount(5);
        movieCollectionPanel.add(movieList);
        movieList.setBackground(Color.decode("#C7CEDE"));
        movieList.setFont(FONT);

        return contentPanel;
    }

    // MODIFIES: this
    // EFFECTS: create and set up the button panel
    private void setUpButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, 100));
        buttonPanel.setBackground(Color.decode("#B1C1EA"));
        add(buttonPanel, BorderLayout.WEST);

        JButton addButton = generateAddMovieButton();
        JButton deleteButton = generateDeleteButton();
        JButton rateButton = generateRateButton();
        JButton reviewButton = generateReviewButton();

        buttonPanel.add(movieName);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(rateButton);
        buttonPanel.add(reviewButton);
        JButton imageButton = generateImageButton();
        buttonPanel.add(imageButton);
    }

    // EFFECTS: generate an ADD MOVIE button called addButton
    private JButton generateAddMovieButton() {
        JButton addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);
        addButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        movieName = new JTextField(10);
        movieName.addActionListener(addListener);
        movieName.getDocument().addDocumentListener(addListener);
        return addButton;
    }

    // EFFECTS: generate a DELETE MOVIE button called deleteButton
    private JButton generateDeleteButton() {
        deleteButton = new JButton(deleteString);
        deleteButton.setActionCommand(deleteString);
        deleteButton.addActionListener(new DeleteListener());
        deleteButton.setBounds(100, 50, 100, 50);
        deleteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return deleteButton;
    }

    // EFFECTS: generate an UPDATE RATING button
    private JButton generateRateButton() {
        JButton rateButton = new JButton(rateString);
        rateButton.setActionCommand(rateString);
        rateButton.addActionListener(new RateListener());
        rateButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return rateButton;
    }

    // EFFECTS:  generate an UPDATE REVIEW button
    private JButton generateReviewButton() {
        JButton reviewButton = new JButton(reviewString);
        reviewButton.setActionCommand(reviewString);
        reviewButton.addActionListener(new ReviewListener());
        reviewButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return reviewButton;
    }

    // EFFECTS: generate an image button
    private JButton generateImageButton() {
        Image ubcLogo = null;
        try {
            ubcLogo = ImageIO.read(new File("src/main/ui/Images/ubc-logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image newImage = ubcLogo.getScaledInstance(120, 164, Image.SCALE_DEFAULT);
        Icon icon = new ImageIcon(newImage);
        return new JButton(icon);
    }

    // EFFECTS: Returns an ImageIcon, or null if the path was invalid.
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MovieReviewAppUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // EFFECTS: pop up a dialog which gives user a choice to save current collection
    private void closeDialog() {
        Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(new Frame(),
                "Save movie collection?",
                "CLOSING...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (n == JOptionPane.YES_OPTION) {
            saveMovieCollection();
        }
    }

    // EFFECTS: save user's current work to JSON
    private void saveMovieCollection() {
        jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(mc);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }

    }

    //loading option dialog
    private void openDialog() {
        Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(new Frame(),
                "Do you want to load the previous movie collection?",
                "Loading option",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        setIsLoaded(n != JOptionPane.NO_OPTION);
    }

    // EFFECTS: pop up a window for users to update the rating
    private void ratingDialog() {
        ImageIcon icon = createImageIcon("images/middle.gif");
        String newRatingStr = (String) JOptionPane.showInputDialog(
                this,
                "Enter new rating:\n"
                        + "(from 1 to 5)",
                "Rating Dialog",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null,
                null);

        if (isValidRating(newRatingStr)) {
            toUpdateRating = true;
            newRating = Integer.parseInt(newRatingStr);
        } else {
            toUpdateRating = false;
            wrongRatingWindow();
        }

    }

    // EFFECTS: pop up a window for users to update the rating
    private void reviewDialog() {
        ImageIcon icon = createImageIcon("images/middle.gif");
        newReview = (String) JOptionPane.showInputDialog(
                this,
                "Enter new review:",
                "Review Dialog",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null,
                null);
        toUpdateReview = (newReview != null) && (newReview.length() > 0);
    }

    // EFFECTS: return true if the rating user entered is valid to update
    private boolean isValidRating(String s) {
        try {
            return 0 < Integer.parseInt(s) && Integer.parseInt(s) <= 5;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // EFFECTS: pop up a window if user enters an invalid rating
    private void wrongRatingWindow() {
        JOptionPane.showMessageDialog(this,
                "This rating is invalid.\n"
                        + "You should enter an integer from 1 to 5.",
                "Invalid rating",
                JOptionPane.WARNING_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: load the previous data from JSON file
    private void loadPreviousCollection() {
        jsonReader = new JsonReader(JSON_STORE);
        try {
            mc = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }

        for (Movie m : mc.viewAllMovies()) {
            listModel.addElement(m);
        }
    }

    // MODIFIES: this
    // EFFECT: if data is loaded, set to true
    private void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            //No selection, disable fire button.
            //Selection, enable the fire button.
            deleteButton.setEnabled(movieList.getSelectedIndex() != -1);
        }
    }

    // this listener is for delete button
    class DeleteListener implements ActionListener {
        // EFFECTS: to set up the behavior
        public void actionPerformed(ActionEvent e) {
            int index = movieList.getSelectedIndex();
            mc.removeMovie((Movie) listModel.getElementAt(index));
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) {
                deleteButton.setEnabled(false);
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
                movieList.setSelectedIndex(index);
                movieList.ensureIndexIsVisible(index);
            }
        }
    }

    // This listener is shared by the text field and the add button
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: required by ActionListener, when action is performed, get the text and react
        public void actionPerformed(ActionEvent e) {
            String name = movieName.getText();

            if (name.equals("")) {
                Toolkit.getDefaultToolkit().beep();
                movieName.requestFocusInWindow();
                movieName.selectAll();
                return;
            }

            int index = movieList.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            Movie newMovie = new Movie(name, 0, "///TO BE UPDATED///");
            mc.addMovie(newMovie);
            listModel.addElement(newMovie);

            //Reset the text field.
            movieName.requestFocusInWindow();
            movieName.setText("");

            //Select the new item and make it visible.
            movieList.setSelectedIndex(index);
            movieList.ensureIndexIsVisible(index);
        }

        // EFFECTS: Required by DocumentListener. Enable button.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECTS: Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //EFFECTS: Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        //MODIFIES: this
        //EFFECTS: enable the button if it is not already working
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        //MODIFIES: this
        //EFFECTS: when the text field is empty, make the button invalid
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // this listener is for rate button
    class RateListener implements ActionListener {
        // EFFECTS: update the rating of the selected movie
        public void actionPerformed(ActionEvent e) {
            int index = movieList.getSelectedIndex();
            ratingDialog();

            if (toUpdateRating) {
                Movie movieToRate = (Movie) listModel.getElementAt(index);
                movieToRate.newRating(newRating);
            }
        }
    }

    // this listener is for review button
    class ReviewListener implements ActionListener {
        // EFFECTS: pop up a review dialog and edit the review when event was made
        public void actionPerformed(ActionEvent e) {
            int index = movieList.getSelectedIndex();
            reviewDialog();
            if (toUpdateReview) {
                Movie movieToReview = (Movie) listModel.getElementAt(index);
                movieToReview.newReview(newReview);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    // EFFECTS: Play the game
    public static void main(String[] args) {
        new MovieReviewAppUI();
    }
}

