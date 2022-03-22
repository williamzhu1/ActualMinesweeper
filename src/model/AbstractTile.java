package model;

import notifier.ITileStateNotifier;
import test.TestableTile;

public abstract class AbstractTile implements TestableTile {

    boolean isFlagged;
    boolean isOpened ;
    boolean isExplosive;

    protected ITileStateNotifier viewNotifier;
    public abstract boolean open();
    public abstract void flag();
    public abstract void unflag();
    public abstract boolean isFlagged();
    public abstract boolean isOpened();
    public final void setTileNotifier(ITileStateNotifier notifier) {
        this.viewNotifier = notifier;
    }
}
