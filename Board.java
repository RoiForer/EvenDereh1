package test;
import java.util.ArrayList;
//import java.util.List;



public class Board {

    private static Board instance= null;
    private Tile[][] board;
   
    
    
    public Board() {
        this.board = new Tile[15][15];
    }

    int turn = 0;

    public static Board getBoard() {
        if (instance == null) {
            instance = new Board();
        }
        
        return instance;
    }

    public Tile[][] getTiles() {
        Tile[][] copyBoard = new Tile[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j]==null) {
                    copyBoard[i][j]=null;
                }
                else copyBoard[i][j] = board[i][j];
            }
        }
        return copyBoard;
    }

    public boolean inBoard(Word word) {

        int row = word.getRow();
        int col = word.getCol();
        int wordLen = word.getTiles().length;

        if ((row+wordLen<15&&col>=0&&col<15&&row>=0&&row<15) || (col + wordLen<15&&col>=0&&col<15&&row>=0&&row<15)) {
            return true;
        }

        return false;
    }

    public boolean isFirstWord(Word word) {
        Tile[] tiles = word.getTiles();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] != null) {
                    boolean found = false;
                    for (Tile tile : tiles) {
                        if (tile != null && board[i][j].getLetter() == tile.getLetter()) {  // Use getter method to access letter
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
   


    public boolean isAdjacentToLetter(Word word) {

        Tile[] wordTiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
        int wordLength = wordTiles.length;
        
        if (isFirstWord(word)) {
            return true;
        }

        for (int i = 0; i < wordLength; i++) {
            int currentRow, currentCol;
            if (vertical) {
                currentRow = row + i;
                currentCol = col;
            } 
            
            else {
                currentRow = row;
                currentCol = col + i;
            }

            if (currentRow >= 0 && currentRow < board.length && currentCol >= 0 && currentCol < board[0].length) {
  
                if (board[currentRow][currentCol] != null) {
                    return true;
                }
            }
        }

        return false;
    }

    public char whichLetterLeaning (Word word) {


        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
    
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == null) {
                int currentRow = vertical ? row + i : row;
                int currentCol = vertical ? col : col + i;
                if (currentRow >= 0 && currentRow < board.length &&
                    currentCol >= 0 && currentCol < board[0].length &&
                    board[currentRow][currentCol] != null) {
                    return board[currentRow][currentCol].getLetter();
                }
            }
        }
    
        // If no leaning letter is found, return a default character, such as '_'
        return '_';
    }

    public boolean isWithoutChange(Word word) {

        Tile[] wordTiles = word.getTiles();
        boolean vertical = word.getVertical();
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = wordTiles.length;
        char letter;

        for (int i = 0; i < wordLength; i++) {
            if (board[row][col] != null) {
                letter = whichLetterLeaning(word);
                if (board[row][col].getLetter()!=letter) {
                    return false;
                }
            }

            if (vertical) {
                row++;
            } 
            else {
                col++;
            }
        }
        return true;
    }

    public boolean isFirstWordOnStar(Word word) {
        int row = word.getRow();
        int col = word.getCol();

        int wordLength = word.getTiles().length;

        if (isFirstWord(word) && row <= 7 && col <= 7 && row + wordLength > 7 && col + wordLength > 7) {
            return true;
        }
        if(!isFirstWord(word)&&inBoard(word)){
            return true;
        }
        return false;
    }

    public boolean boardLegal(Word word) {
       
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wTiles = word.getTiles();

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return false;

        if (word.getVertical() && (row + wTiles.length > board.length)) return false;
        if (!word.getVertical() && (col + wTiles.length > board[0].length)) return false;

        if (turn == 0) {
            return (word.getVertical() && col == 7 && row <= 7 && (row + wTiles.length - 1) >= 7) ||
                    (!word.getVertical() && row == 7 && col <= 7 && (col + wTiles.length - 1) >= 7);
        }

        return true; 
    }

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    private ArrayList<Word> findNewWords(int row, int col, boolean vertical) {
        ArrayList<Word> words = new ArrayList<>();
        int start = vertical ? row : col;
        int end = start;
        int constant = vertical ? col : row;
        int maxIndex = vertical ? board.length : board[0].length;

        while (start > 0 && board[vertical ? start - 1 : constant][vertical ? constant : start - 1] != null) {
            start--;
        }
      
        while (end < maxIndex && board[vertical ? end : constant][vertical ? constant : end] != null) {
            end++;
        }

        if (end - start > 1) { 
            Tile[] newWordTiles = new Tile[end - start];
            for (int k = start; k < end; k++) {
                newWordTiles[k - start] = board[vertical ? k : constant][vertical ? constant : k];
            }
            words.add(new Word(newWordTiles, start, constant, vertical));
        }

        return words;
    }

    public ArrayList<Word> getWords(Word word) {
        if (!boardLegal(word)) return new ArrayList<>();
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wTiles = word.getTiles();

        ArrayList<Word> words = new ArrayList<>();

        // Temporarily simulate the placement to find new words
        for (int k = 0; k < wTiles.length; k++) {
            int i = row + (word.getVertical() ? k : 0);
            int j = col + (word.getVertical() ? 0 : k);

            // Check horizontally and vertically only if the tile position is part of the new word
            if (board[i][j] == null) {
                board[i][j] = wTiles[k];
                if (word.getVertical()) {
                    words.addAll(findNewWords(i, j, false));  // Check horizontally
                } else {
                    words.addAll(findNewWords(i, j, true));  // Check vertically
                }
            }
        }

        words.add(word);  // Include the main word itself
        return words;
      }


    private int getLettersScore(char letter) {
        switch (letter) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'L':
            case 'N':
            case 'S':
            case 'T':
            case 'R':
                return 1;
            case 'D':
            case 'G':
                return 2;
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J':
            case 'X':
                return 8;
            case 'Q':
            case 'Z':
                return 10;
             
            default:
                return 0; 
        }
    }

    private static final int[][] LETTER_MULTIPLIERS = {
       // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14     
        { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },
        { 1, 1, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 1, 1 },
        { 1, 1, 1, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 1, 1, 1 },
        { 2, 1, 1, 1, 1, 1, 1, 2, 1 ,1 ,1, 1, 1, 1, 2 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1, 1, 1, 1, 1 },
        { 1, 3, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 3, 1 },
        { 1, 1, 2, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 2, 1, 1 },
        { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },
        { 1, 1, 2, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 2, 1, 1 },
        { 1, 3, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 3, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1, 1, 1, 1, 1 },
        { 2, 1, 1, 1, 1, 1, 1, 2, 1 ,1 ,1, 1, 1, 1, 2 },
        { 1, 1, 1, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 1, 1, 1 },
        { 1, 1, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 1, 1 },
        { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },
    
    };

    private static final int[][] WORD_MULTIPLIER = {
      // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14
        {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3 },
        {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
        {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1 },
        {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
        {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1 },
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3 },
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1 },
        {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
        {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1 },
        {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
        {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3 },

    };

    private int getScore(Word word) {
        int sumScoreLetters = 0;
        int sumScoreWord = 0;
        int multiWord =1;
        Tile[] tiles = word.getTiles();
        int wordLength = tiles.length;


        for (int i = 0; i < wordLength; i++) {
            int row = word.getRow();
            int col = word.getCol();
            int currentRow, currentCol;
            if (word.getVertical()) {
                currentRow = row + i;
                currentCol = col;
            }

            else {
                currentRow = row;
                currentCol = col + i;
            }

            int letterMultiplier = LETTER_MULTIPLIERS[currentRow][currentCol];
            int wordMultiplier = WORD_MULTIPLIER[currentRow][currentCol];
            if (tiles[i] != null&&tiles[i].getLetter()!='_') {
                char letter = tiles[i].getLetter();
                int tileScore = getLettersScore(letter);

                sumScoreLetters += tileScore * letterMultiplier;

            }

            if (isFirstWord(word)==false) {
                if (currentRow==7&&currentCol==7) {
                    wordMultiplier =1;
                }

            }

            if (tiles[i]== null) {
                char letter =whichLetterLeaning(word);
                int tileScore = getLettersScore(letter);

                sumScoreLetters += tileScore * letterMultiplier;
                if (!isFirstWord(word)) {
                    wordMultiplier =1;
                }
            }

            if (wordMultiplier !=1) {
                multiWord = wordMultiplier;

            }

        }
        sumScoreWord = sumScoreLetters*multiWord;

        return sumScoreWord;
    }

    
    public void placeWordOnBoard(Word word) {
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.getVertical();
    
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                int currentRow = vertical ? row + i : row;
                int currentCol = vertical ? col : col + i;
                board[currentRow][currentCol] = tiles[i];
            }
        }
    }


public int tryPlaceWord(Word word) {
    if (!boardLegal(word)) {
        return 0;
    }
    
    ArrayList<Word> words = getWords(word);
    int score = 0;

    for (Word w : words) {
        if (!dictionaryLegal(w)) {
            return 0;
        }
        score += getScore(w);
        placeWordOnBoard(w); 
    }

    turn++;
    return score;
}

}
