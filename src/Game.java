public class Game{
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        Board board = new Board(50,50);
        BoardVisualComponent comp = new BoardVisualComponent(board);
	BoardVisualFrame frame = new BoardVisualFrame(board, "window name", comp);
        System.out.println("Bye World!"); // Display the string.
        // TODO Fix prefered window size and such
    }



}