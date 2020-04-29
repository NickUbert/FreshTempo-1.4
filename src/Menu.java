import java.awt.Dimension;
import java.awt.Toolkit;

public class Menu {

	public Menu() {
		// empty constructor
	}

	// Main constructor that decideds which method to direct to.
	public Menu(String style) {
		switch (style) {
		case "options":
			openOptions();
			break;
		case "toggle":
			openToggle();
			break;
		case "serverConnect":
			openServerConnect();
			break;
		case "create":
			openCreate();
			break;
		case "analytics":
			openAnalytics();
			break;
		}

	}

	// Constructor for additional data
	// Currently only used by error messages
	public Menu(String style, String message) {
		openError(message);
	}

	/*
	 * createWindow is used to create a general purpose window for menus. The
	 * parameters w and h represent what percentage of the screen each attribute
	 * should take up
	 */
	private RoundedPanel createWindow(double w, double h) {

		Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = ((int) mtk.getWidth());
		int screenY = ((int) mtk.getHeight());

		int width = (int) w * screenX;
		int height = (int) h * screenY;

		RoundedPanel frame = new RoundedPanel();
		frame.setSize(width, height);

		return frame;
	}

	private void displayWindow() {
		// remove items from frame and other menus
		// align the menu in the window
		// update session bools
		// display frame
	}

	private void openOptions() {
		// TODO
	}

	private void openError(String message) {
		// TODO
	}

	private void openToggle() {
		// TODO
	}

	private void openServerConnect() {
		// TODO
	}

	private void openCreate() {
		// TODO
	}

	private void openAnalytics() {
		// TODO
	}

}
