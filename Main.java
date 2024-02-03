//I Have No Clue What Some Of These Are Needed For\\
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import javax.swing.*;

//Main Handler, Grabs All The Classes Into One Primary Class That Can Control All The Rest\\
public class Main {

    //Custom Classes\\
    static WordHandler wordHandler = new WordHandler();
    static FrameHandler frameHandler = new FrameHandler();

    static List<String> GuessedStrings = new ArrayList<>();
    static JTextField InputField = frameHandler.InputField;

    public static void main(String[] args) {
        InputField.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { String PlayersGuess = InputField.getText(); HandleInputs(PlayersGuess); } } );
        UpdateGUIs();
    }

    public static void HandleInputs(String PlayerGuess) {
        InputField.setText("");

        //Return Cases\\
        if (PlayerGuess.isBlank()) {return;}
        if (GuessedStrings.contains(PlayerGuess.toLowerCase())) {return;}

        GuessedStrings.add(PlayerGuess.toLowerCase());
        UpdateGUIs();
    }

    public static void UpdateGUIs() {
        String HiddenWord = wordHandler.returnCurrentString(GuessedStrings);
        String SpacedOut = WordHandler.addSmallSpaces(HiddenWord);

        boolean PlayerGuessedWord = wordHandler.WordGuessedCorrectly(SpacedOut);
        if (PlayerGuessedWord) {
            frameHandler.currentTextLabel.setForeground(Color.decode("#469437"));
        }

        //Updating All Guis\\
        FrameHandler.SetTextUpdate(frameHandler.currentTextLabel, SpacedOut, 725, 400);
        FrameHandler.SetTextUpdate(frameHandler.GuessedLetters, String.join(" ", GuessedStrings), 650, 135);
    }
}

//Handles The Guessing\\
class WordHandler {
    private String[] wordChoices = {"ABRUPTLY", "ACADEMY", "BANQUET", "BICYCLE", "CABINET", "CALCULUS", "DAMAGED", "DECEMBER", "ECLIPSE", "ELEGANT", "FACULTY", "FANTASY", "GALLERY", "GENESIS", "HARMONY", "HOLIDAY", "ILLUSION", "IMAGINE", "JASMINE", "JOURNEY", "KANGAROO", "KARAOKE", "LANTERN", "LIBRARY", "MAGICAL", "MASCARA", "NEUTRAL", "NIRVANA", "OCTAGON", "ORCHARD", "PACKAGE", "PALADIN", "QUALITY", "QUARTER", "RADIANT", "RAINBOW", "SALMON", "SANDWICH", "TACITUS", "THERAPY", "UMBRELLA", "UNICORN", "VACUUM", "VALIANT", "WALNUT", "WARRIOR", "XENOPHOBIA", "XYLOPHONE", "YACHT", "YOGURT", "ZEBRA", "ZODIAC", "Life is an unpredictable journey, where each step unveils new twists and turns, guiding us through a tapestry of experiences and challenges, shaping our path and defining our story."};;
    private String wordChosen;
    private Random random = new Random();

    //Basic Setup\\
    public WordHandler() {
        int randomIndex = random.nextInt(wordChoices.length);
        wordChosen = wordChoices[randomIndex];
    }

    public boolean WordGuessedCorrectly(String CurrentWord) {
        return wordChosen.equalsIgnoreCase(CurrentWord);
    }

    //Took From ChatGTP\\--
    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                titleCase.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }

        return titleCase.toString().trim();
    }

    //Just Adding A Small Space Between The Characters\\
    public static String addSmallSpaces(String CurrentText) {
        StringBuilder SpacedString = new StringBuilder();

        for (char Letter : CurrentText.toCharArray()) {

            if (Letter=='_') {
                String WithSpace = String.valueOf(Letter) + " ";
                SpacedString.append(WithSpace);
            } else {
                String WithSpace = String.valueOf(Letter);
                SpacedString.append(WithSpace);
            }
        }

        return SpacedString.toString().trim();
    }

    //Returns What Ever The String Is Based On The Letters Guessed - For Example "Test" With Only "t" Guessed Would Be "T__t"\\
    public String returnCurrentString(List<String> GuessedStrings) {
        String HiddenString = wordChosen.replaceAll("[aA-zZ]", "_");

        //Loop Through The Guessed Letters/Strings\\
        for (String StringGuess : GuessedStrings) {
            int StartingIndex = 0;

            //Avoid Issues With indexOf Only Finding One Instances\\
            while (true) {
                int IndexFound = wordChosen.toLowerCase().indexOf(StringGuess.toLowerCase(), StartingIndex);
                if (IndexFound == -1) {break;}

                StartingIndex = IndexFound+1;
                //Replaces The Hidden Word We Have With The Updated Letters\\
                HiddenString = HiddenString.substring(0, IndexFound) + StringGuess + HiddenString.substring(IndexFound + StringGuess.length());
            }
        }

        return toTitleCase(HiddenString); //Simply Just Title Cases HiddenString\\
    }
}

//This Was Not Fun To Work With\\
class FrameHandler {
    JFrame mainFrame;
    JLabel currentTextLabel;
    JLabel GuessedLetters;
    JTextField InputField;

    public FrameHandler() {
        mainFrame = new JFrame();

        //Layout
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(800, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Guessed Letter Input Field
        InputField = new JTextField(); mainFrame.add(InputField, BorderLayout.SOUTH);
        InputField.setBackground(Color.WHITE);
        InputField.setFont(new java.awt.Font("Arial", Font.BOLD, 65));
        InputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        //Current Word Text
        currentTextLabel = new JLabel(); mainFrame.add(currentTextLabel, BorderLayout.CENTER);
        currentTextLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTextLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        //Guesses List
        GuessedLetters = new JLabel(); mainFrame.add(GuessedLetters, BorderLayout.NORTH);
        GuessedLetters.setHorizontalAlignment(JLabel.NORTH_EAST);
        GuessedLetters.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        mainFrame.setVisible(true);
    }

    //Stright From ChatGTP\\
    public static void SetTextUpdate(JLabel label, String text, int maxWidth, int maxHeight) {
        Font currentFont = label.getFont();
        FontMetrics fontMetrics = label.getFontMetrics(currentFont);
        int stringWidth = fontMetrics.stringWidth(text);
        int stringHeight = fontMetrics.getHeight();

        // Calculate the ratio by which to scale the font size
        double widthRatio = (double) maxWidth / (double) stringWidth;
        double heightRatio = (double) maxHeight / (double) stringHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        // Calculate the new font size based on the minimum ratio
        int newSize = (int) (currentFont.getSize() * ratio);

        Font newFont = currentFont.deriveFont(Font.BOLD, newSize);

        label.setFont(newFont);
        label.setText(text);

        // Adjust the preferred size of the label to prevent shifting
        Dimension preferredSize = label.getPreferredSize();
        preferredSize.width = Math.min(maxWidth, stringWidth); // Limit width to maxWidth
        preferredSize.height = Math.min(maxHeight, stringHeight); // Limit height to maxHeight
        label.setPreferredSize(preferredSize);
    }


}
