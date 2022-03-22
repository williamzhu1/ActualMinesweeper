package model;

public class EmptyTile extends AbstractTile{

    /*
    field
    boolean isOpened;
    boolean isFlagged;
    boolean isExplosive;
    */

    //constructor
    public EmptyTile(){
         isOpened = false;
         isFlagged = false;
         isExplosive = false;
    }

    @Override
    public boolean open() {
        if(this.isOpened == true){
            return false;
        }
        isOpened = true;
        return true;
    }

    @Override
    public void flag() {
        isFlagged = true;
    }

    @Override
    public void unflag() {
        isFlagged = false;
    }

    @Override
    public boolean isFlagged() {
        return this.isFlagged;
    }

    @Override
    public boolean isExplosive() {
        return isExplosive;
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }
}
