package test;

import java.util.Arrays;
import java.util.Objects;


public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;
   

    public Word(Tile[] xtiles, int xrow, int xcol, boolean xvertical) {
        
        this.tiles = xtiles;
        this.row = xrow;
        this.col = xcol;
        this.vertical = xvertical;
    }


    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getVertical() {
        return vertical;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Word otherWord = (Word) obj;

        return Arrays.equals(tiles, otherWord.tiles) && col == otherWord.col && row == otherWord.row
                && vertical == otherWord.vertical;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(col, row, vertical);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }
}
