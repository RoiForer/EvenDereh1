package test;
import java.util.Objects;
import java.util.Random;


public class Tile {

    public final int score;
    public final char letter;
    

    private Tile(int score, char letter) {


        this.score = score;
        this.letter = letter;
    }


    public char getLetter() {
        return letter;
    }

    public int getScoreTile() {
        return score;
    }

    

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Tile tail = (Tile) obj;  
        return letter== tail.letter && score == tail.score;  

    }

    @Override
    public int hashCode() {

        return Objects.hash(score, letter);
    }

    public static class Bag {

        private static Bag instance= null;

        private final int [] arrLetters = new int[26];
        private final Tile [] tiles;

        private Bag() {
           arrLetters[0]=9;
           arrLetters[1]=2;
           arrLetters[2]=2;
           arrLetters[3]=4;
           arrLetters[4]=12;
           arrLetters[5]=2;
           arrLetters[6]=3;
           arrLetters[7]=2;
           arrLetters[8]=9;
           arrLetters[9]=1;
           arrLetters[10]=1;
           arrLetters[11]=4;
           arrLetters[12]=2;
           arrLetters[13]=6;
           arrLetters[14]=8;
           arrLetters[15]=2;
           arrLetters[16]=1;
           arrLetters[17]=6;
           arrLetters[18]=4;
           arrLetters[19]=6;
           arrLetters[20]=4;
           arrLetters[21]=2;
           arrLetters[22]=2;
           arrLetters[23]=1;
           arrLetters[24]=2;
           arrLetters[25]=1;

           tiles= new Tile[] {
            new Tile(1, 'A'),
            new Tile(3, 'B'),
            new Tile(3, 'C'),
            new Tile(2, 'D'),
            new Tile(1, 'E'),
            new Tile(4, 'F'),
            new Tile(2, 'G'),
            new Tile(4, 'H'),
            new Tile(1, 'I'),
            new Tile(8, 'J'),
            new Tile(5, 'K'),
            new Tile(1, 'L'),
            new Tile(3, 'M'),
            new Tile(1, 'N'),
            new Tile(1, 'O'),
            new Tile(3, 'P'),
            new Tile(10, 'Q'),
            new Tile(1, 'R'),
            new Tile(1, 'S'),
            new Tile(1, 'T'),
            new Tile(1, 'U'),
            new Tile(4, 'V'),
            new Tile(4, 'W'),
            new Tile(8, 'X'),
            new Tile(4, 'Y'),
            new Tile(10, 'Z')
           };
           
        }   
        
        public boolean isEmpty() {
            for (int i=0; i<26; i++) {
                if (arrLetters[i]>0) {
                    return false;
                }
            }
            return true;

        }


        public Tile getRand() {
            if (isEmpty()) {
                return null;
            }

            Random rand = new Random();
            int random = rand.nextInt(98)+1;

            int cumulativeCount = 0;
            for (Tile tile : tiles) {
                cumulativeCount += arrLetters[tile.letter - 'A'];
                if (cumulativeCount >= random) {
                    arrLetters[tile.letter - 'A']--;
                    return tile;
                }
            }
            return null;

        }

        public Tile getTile(char c) {

            if (c < 'A' || c > 'Z') {
                return null;
            }
        
            int index = c - 'A';
        
            if (arrLetters[index] > 0) {
                arrLetters[index]--;
                for (Tile tile : tiles) {
                    if (tile.letter == c) {
                        return tile;
                    }
                }
            }
            return null;
        }

        
  

        public void put(Tile tile) {
            if (tile != null) {
                int count = 0;
                for (int i = 0; i < 26; i++) {
                    count += arrLetters[i];
                }
        
                if (count < 98) {
                    int index = tile.letter - 'A';
                    arrLetters[index]++;
                }
            }
        }

        public int size() {
            int counter=0;
            for (int i=0;i<26;i++) {
                counter += arrLetters[i];
            }

            return counter;
        }

        public int[] getQuantities() {
            int[] quantitiesCopy = new int[arrLetters.length];
            System.arraycopy(arrLetters, 0, quantitiesCopy, 0, arrLetters.length);
            return quantitiesCopy;
        }

        public static Bag getBag() {
            if (instance == null) {
                instance = new Bag();
            }
            
            return instance;
        }


    }

}
