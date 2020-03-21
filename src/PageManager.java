/**
 * PageManager class is the backbone of all naviagtion in the program, any
 * movement in the program eventually ends up using one of these functions.
 * 
 */
public class PageManager {
	StartUp su = new StartUp();
	CurrentSession cs = new CurrentSession();
	int pageNum;
	int cAP;

	/*
	 * The constructor is used to get the current values assigned to local varaibles
	 * to be used by other functions in this class
	 */
	public PageManager() {
		pageNum = cs.getCurrentPage();
		cAP = cs.getCAP();
	}

	/*
	 * updatePage is used to set any page that should be visible to false
	 */
	public void updatePage() {
		if (pageNum > 0) {
			StartUp.backgroundHash.get(pageNum - 1).setVisible(false);
			StartUp.backgroundHash.get(pageNum + 1).setVisible(false);
		}

		if (pageNum == 0 && cAP != 0) {
			for (int curPageNum = 1; curPageNum <= cAP; curPageNum++) {
				StartUp.backgroundHash.get(curPageNum).setVisible(false);
			}

		}

		StartUp.backgroundHash.get(pageNum).setVisible(true);

		if (!cs.getMenuOpen()) {
			Sorter so = new Sorter();
			so.sort(CurrentSession.itHash);
			TaskBar tb = new TaskBar();
			tb.updateTaskBar();
		}
	
	}

	/*
	 * nextPage advances the current page number and assigns the new value to a
	 * currentSession value
	 */
	public void nextPage() {
		pageNum++;
		cs.setCurrentPage(pageNum);
		updatePage();
	}

	public void goToCAP() {
		cs.updateCAP();
		pageNum = cAP;
		updatePage();
		TaskBar tb = new TaskBar();
		tb.updateTaskBar();
	}

	/*
	 * prevPage does the same thing as next page, dumbass.
	 */
	public void prevPage() {
		pageNum--;
		cs.setCurrentPage(pageNum);
		updatePage();
	}

	/*
	 * clearPage is used to remove all components from a certain page. This is
	 * normally used by menus opening
	 */
	public void clearPage() {
		StartUp.backgroundHash.get(pageNum).removeAll();
		StartUp.backgroundHash.get(pageNum).repaint();
		StartUp.backgroundHash.get(pageNum).revalidate();
	}

}