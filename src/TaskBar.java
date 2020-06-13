import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Taskbar class is responsible for the naviagtion for users. Its main use is
 * choosing which buttons to display depending on the page.
 * 
 */
public class TaskBar {
	// Bounds for graphics
	Dimension mTk = Toolkit.getDefaultToolkit().getScreenSize();
	private CurrentSession cs = new CurrentSession();
	private int taskBarWidth = ((int) mTk.getWidth());
	private int screenHeight = ((int) mTk.getHeight());
	private int btnIconXY = (int) (taskBarWidth * .0475);
	private int taskBarY = (int) (.1 * screenHeight);
	private int taskBarYL = (int) (screenHeight - taskBarY);
	private int taskBarX = (int) (taskBarWidth);
	private int npageXL = (int) (.75 * taskBarWidth - (btnIconXY / 2));
	private int optionsXYL = (int) (.00625 * taskBarX);
	private int backBtnXL = (int) (.25 * taskBarWidth - (btnIconXY / 2));
	private int addMenuBtnXL = (int) ((.5 * taskBarX) - (int) (btnIconXY / 2));

	// imageIcons
	ImageIcon optionIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-settings.png"));
	Image optionImg = optionIcon.getImage();
	Image biOption = optionImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_DEFAULT);
	Icon resizedOptionIcon = (Icon) new ImageIcon(biOption);

	ImageIcon menuIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-menu.png"));
	Image menuImg = menuIcon.getImage();
	Image biMenu = menuImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_DEFAULT);
	Icon resizedMenuIcon = (Icon) new ImageIcon(biMenu);

	ImageIcon nextIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-next.png"));
	Image nextImg = nextIcon.getImage();
	Image biNext = nextImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_DEFAULT);
	Icon resizedNextIcon = (Icon) new ImageIcon(biNext);

	ImageIcon backIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-back.png"));
	Image backImg = backIcon.getImage();
	Image biBack = backImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_DEFAULT);
	Icon resizedBackIcon = (Icon) new ImageIcon(biBack);

	ImageIcon addIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-add.png"));
	Image addImg = addIcon.getImage();
	Image biAdd = addImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_DEFAULT);
	Icon resizedAddIcon = (Icon) new ImageIcon(biAdd);

	ImageIcon undoIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-undo.png"));
	Image undoImg = undoIcon.getImage();
	Image biUndo = undoImg.getScaledInstance(btnIconXY, btnIconXY, Image.SCALE_SMOOTH);
	Icon resizedUndoIcon = (Icon) new ImageIcon(biUndo);

	ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("FT-icon-logo.png"));
	Image logo = logoIcon.getImage();

	// different taskBars
	JPanel taskBarNext = new JPanel();
	JPanel taskBarBack = new JPanel();
	JPanel taskBarBoth = new JPanel();
	JPanel taskBarNeither = new JPanel();
	JPanel taskBarMenu = new JPanel();
	JPanel taskBarUndo = new JPanel();
	static JPanel taskBar = new JPanel();

	// Navitgation buttons
	JButton menuBtn = new JButton("", resizedMenuIcon);
	JButton optionsBtn = new JButton("", resizedOptionIcon);
	JButton nextPage = new JButton("", resizedNextIcon);
	JButton backBtn = new JButton("", resizedBackIcon);
	JButton addBtn = new JButton("", resizedAddIcon);
	JButton undoBtn = new JButton("", resizedUndoIcon);

	private Color backgroundColor = Color.decode("#223843");
	CardLayout cl = new CardLayout();

	static boolean autoNext = false;

	/*
	 * The constructor is used to set the properties of the components being used on
	 * the taskbar. It also sets up the use of CardLayout.
	 * 
	 */
	@SuppressWarnings("static-access")
	public TaskBar() {

		// set dims for taskbars and clear any previous components
		taskBar.setLayout(cl);
		taskBar.setBounds(0, taskBarYL, taskBarX, taskBarY);
		taskBar.setVisible(true);
		taskBar.removeAll();

		StartUp.window.setIconImage(logo);
		// setting layouts for taskbars
		taskBarBack.setLayout(null);
		taskBarNext.setLayout(null);
		taskBarNeither.setLayout(null);
		taskBarBoth.setLayout(null);
		taskBarMenu.setLayout(null);
		taskBarUndo.setLayout(null);

		// Options Button
		optionsBtn.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		optionsBtn.setBounds(optionsXYL, 0, btnIconXY, btnIconXY);
		optionsBtn.setVisible(true);
		optionsBtn.setBackground(null);
		optionsBtn.setContentAreaFilled(false);
		optionsBtn.setFocusPainted(false);
		optionsBtn.setFocusable(false);
		optionsBtn.setBorder(null);
		optionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Options op = new Options();
				taskBar.removeAll();
				op.openMenu();
			}
		});

		// Back button
		backBtn.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		backBtn.setBounds(backBtnXL, 0, btnIconXY, btnIconXY);
		backBtn.setFocusable(false);
		backBtn.setBackground(null);
		backBtn.setContentAreaFilled(false);
		backBtn.setFocusPainted(false);
		backBtn.setBorder(null);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cs.getMenuOpen()) {
					cs.prevPage();
				}
			}
		});

		// Next Page Button
		nextPage.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		nextPage.setBounds(npageXL, 0, btnIconXY, btnIconXY);
		nextPage.setContentAreaFilled(false);
		nextPage.setFocusPainted(false);
		nextPage.setFocusable(false);
		nextPage.setBorder(null);
		nextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cs.getMenuOpen()) {
					cs.nextPage();
				}
			}
		});

		undoBtn.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		undoBtn.setBounds(addMenuBtnXL, 0, btnIconXY, btnIconXY);
		undoBtn.setContentAreaFilled(false);
		undoBtn.setFocusPainted(false);
		undoBtn.setFocusable(false);
		undoBtn.setBorder(null);
		undoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CreateTimer ct = new CreateTimer();
				ct.hideKeyPanels();

				StartUp.optionPanel.setVisible(false);

				@SuppressWarnings("unused")
				InventoryMenu im = new InventoryMenu();
				updateBar("MENU");
			}
		});

		// Menu Button
		menuBtn.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		menuBtn.setBounds(addMenuBtnXL, 0, btnIconXY, btnIconXY);
		menuBtn.setContentAreaFilled(false);
		menuBtn.setFocusPainted(false);
		menuBtn.setFocusable(false);
		menuBtn.setBorder(null);
		menuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				@SuppressWarnings("unused")
				InventoryMenu im = new InventoryMenu();
				// This loop updates the background for pages that may have had their color
				// switched due to expiration flashes.
				for (int i = 0; i <= cs.getCAP(); i++) {
					StartUp.backgroundHash.get(i).setBackground(backgroundColor);
				}
				// Hard coded taskbar switch
				updateBar("MENU");
			}
		});

		// Add Button
		addBtn.setPreferredSize(new Dimension(btnIconXY, btnIconXY));
		addBtn.setBounds(addMenuBtnXL, 0, btnIconXY, btnIconXY);
		addBtn.setContentAreaFilled(false);
		addBtn.setFocusPainted(false);
		addBtn.setFocusable(false);
		addBtn.setBorder(null);
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Update session values and open new toggle page
				cs.setMenuOpen(true);
				cs.setTyping(false);
				InventoryMenu im = new InventoryMenu();
				im.inventoryPanel.removeAll();
				im.inventoryPanel.setVisible(false);
				im.toggleScrollPanel.setVisible(false);

				// Updates layout type and proper gap spacing
				StartUp su = new StartUp();
				su.switchToAddingGraphics();
				updateBar("UNDO");

				// This loop may not be needed since the addition of the menu used for adding
				while (cs.getCurrentPage() < cs.getCAP()) {
					cs.nextPage();
					Sorter so = new Sorter();
				}

				PageManager pm = new PageManager();
				pm.clearPage();

				CreateTimer ct = new CreateTimer();
				ct.paintCreatePanel();

			}
		});

		// Set bounds for all taskbars and label them
		taskBarBack.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarBack, "BACK");
		taskBarNext.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarNext, "NEXT");
		taskBarBoth.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarBoth, "BOTH");
		taskBarNeither.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarNeither, "NEITHER");
		taskBarUndo.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarUndo, "UNDO");
		taskBarMenu.setBounds(0, 0, taskBarX, taskBarY);
		taskBar.add(taskBarMenu, "MENU");

		StartUp su = new StartUp();
		su.window.add(taskBar);
		su.window.repaint();
		su.window.revalidate();

	}

	/*
	 * updateBar is used to change the card being displayed as the taskbar and add
	 * the needed components.
	 */
	@SuppressWarnings("static-access")
	public void updateBar(String taskBarName) {

		String name = taskBarName;
		if (name.equals("BOTH")) {
			taskBarBoth.add(menuBtn);
			taskBarBoth.add(nextPage);
			taskBarBoth.add(backBtn);
		} else if (name.equals("NEXT")) {
			taskBarNext.add(menuBtn);
			taskBarNext.add(nextPage);
		} else if (name.equals("BACK")) {
			taskBarBack.add(menuBtn);
			taskBarBack.add(backBtn);
		} else if (name.equals("NEITHER")) {
			taskBarNeither.add(menuBtn);
		} else if (name.equals("MENU")) {
			taskBarMenu.add(optionsBtn);
			//taskBarMenu.add(addBtn);
		} else if (name.equals("UNDO")) {
			taskBarUndo.add(undoBtn);
		}
		cl.show(taskBar, taskBarName);
		StartUp su = new StartUp();
		su.window.add(taskBar);
		su.window.repaint();
		su.window.revalidate();

	}

	/*
	 * updateTaskBar is the method responsible for checking what taskbar is needed
	 * depending on the page before calling updateBar.
	 */
	public void updateTaskBar() {
		CurrentSession cs = new CurrentSession();
		int currentPage = cs.getCurrentPage();
		cs.updateCAP();
		int cAP = cs.getCAP();

		if (currentPage == 0) {
			if (currentPage != cAP) {
				updateBar("NEXT");
			} else {
				updateBar("NEITHER");
			}
		} else {
			if (currentPage != cAP) {
				if (!autoNext) {
					updateBar("BOTH");
				}
			} else {
				if (!cs.getMenuOpen()) {
					updateBar("BACK");
				}
			}
		}

	}

	/*
	 * setAutoNext takes in a boolean value and updates the autoNext value as
	 * needed. This may not be needed since the FALLS update.
	 */
	public void setAutoNext(boolean b) {
		autoNext = b;
	}
}