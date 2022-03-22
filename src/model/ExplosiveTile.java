package model;

public class ExplosiveTile extends AbstractTile{

    public ExplosiveTile(){
        isFlagged = false;
        isOpened = false;
        isExplosive = true;
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
        return this.isExplosive;
    }

    @Override
    public boolean isOpened() {
        return this.isOpened;
    }
}
