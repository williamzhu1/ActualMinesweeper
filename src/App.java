import model.Difficulty;
import model.Minesweeper;
import model.PlayableMinesweeper;
import view.MinesweeperView;

public class App {
    public static void main(String[] args) throws Exception {
        MinesweeperView view = new MinesweeperView();
        //Uncomment the lines below once your game model code is ready; don't forget to import your game model 
<<<<<<< HEAD
        PlayableMinesweeper model = new Minesweeper();

        view.setGameModel(model);
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======

>>>>>>> 546933d4ec4a715d5daeabf65e955e0be3f308c9
=======
        //PlayableMinesweeper model = new Minesweeper();
>>>>>>> parent of bfcd9fb (Merge branch 'main' of https://github.com/williamzhu1/TrueMinesweeper)
        
        
        /**
            Your code to bind your game model to the game user interface
        */
        
        
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of a008bb0 (Difficulty level)
        model.startNewGame(Difficulty.EASY);
=======
        model.startNewGame(Difficulty.HARD);
>>>>>>> a008bb0ad17fb8bcce7c9b19ffaa3ab71ab50940
=======
        model.startNewGame(Difficulty.HARD);
>>>>>>> 546933d4ec4a715d5daeabf65e955e0be3f308c9
=======
        //model.startNewGame(Difficulty.EASY);
>>>>>>> parent of bfcd9fb (Merge branch 'main' of https://github.com/williamzhu1/TrueMinesweeper)
    }
}
