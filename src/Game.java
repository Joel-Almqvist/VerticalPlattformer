public class Game{
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Board board = new Board(40, 50);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame(board, "window name", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);


        System.out.println("Bye World!");


        // TODO Add jumping functionality


    }
}