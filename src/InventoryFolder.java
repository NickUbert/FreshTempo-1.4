import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InventoryFolder {

	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());

	private int folderX = (int) (screenX * .18);
	private int folderY = (int) (screenY * .1);

	private int folderIconX = (int) (screenX * .055);
	private int folderIconY = (int) (screenY * .065);

	private int folderLabelY = (int) (screenY * .03);

	private int titlePromptX = (int) (screenX * .33);
	private int titlePromptY = (int) (folderY / 1.2);

	private int inventoryOptionXY = (int) (screenX * .045);
	private int renameIconXY = (int) (screenX * .04);
	private int invetoryOptionButtonX = (int) (screenX * .07);

	private int titleTextFieldXL = (int) (.5 * screenX);
	private int titleTextFieldYL = (int) (.05 * titlePromptY);
	private int titleTextFieldX = (int) (.5 * titlePromptX);
	private int titleTextFieldY = (int) (.074 * screenY);

	Icon resizedFolderIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-folder.png"))
			.getImage().getScaledInstance(folderIconX, folderIconY, Image.SCALE_SMOOTH));

	Icon resizedNewTimerIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-new-timer.png")).getImage()
					.getScaledInstance(inventoryOptionXY, inventoryOptionXY, Image.SCALE_SMOOTH));

	Icon resizedDeleteTimerIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-delete-timer.png")).getImage()
					.getScaledInstance(inventoryOptionXY, inventoryOptionXY, Image.SCALE_SMOOTH));

	Icon resizedRenameIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-rename.png"))
			.getImage().getScaledInstance(renameIconXY, renameIconXY, Image.SCALE_SMOOTH));

	private Font folderFont = new Font("Helvetica", Font.BOLD, ((int) (.016 * screenX)));
	private Font bannerFont = new Font("Helvetica", Font.BOLD, ((int) (.03 * screenX)));

	Border fieldBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);

	private Color exitColor = Color.decode("#CC2936");
	private Color inventoryOptionColor = Color.decode("#6B818C");

	Border exitBorder = BorderFactory.createLineBorder(Color.RED, 3);

	private JButton folder;
	private String folderName = "Error";

	public JPanel createNewIcon(String name) {
		// Assumed that there is no duplicate folder

		folderName = name;
		folder = new JButton();
		folder.setPreferredSize(new Dimension(folderIconX, folderIconY));
		folder.setIcon(resizedFolderIcon);
		folder.setBorderPainted(false);
		folder.setFocusable(false);
		folder.setBackground(null);
		folder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFolder();

			}
		});

		JPanel folderPanel = new JPanel();
		folderPanel.setPreferredSize(new Dimension(folderX, folderY));
		folderPanel.setBackground(null);
		folderPanel.setLayout(new FlowLayout());
		folderPanel.add(folder);

		JLabel folderLabel = new JLabel(folderName);
		folderLabel.setFont(folderFont);
		folderLabel.setPreferredSize(new Dimension(folderX, folderLabelY));
		folderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		folderLabel.setForeground(Color.WHITE);
		folderPanel.add(folderLabel);

		folderPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				openFolder();
			}
		});

		return folderPanel;

	}

	private void openFolder() {
		InventoryMenu im = new InventoryMenu();
		im.inventoryPanel.removeAll();

		JLabel folderMenuBanner = new JLabel("Inventory Menu - " + folderName);

		folderMenuBanner.setPreferredSize(new Dimension(screenX, (int) (folderY / 1.2)));
		folderMenuBanner.setFont(bannerFont);
		folderMenuBanner.setHorizontalAlignment(JLabel.CENTER);

		folderMenuBanner.setForeground(Color.WHITE);
		im.inventoryPanel.add(folderMenuBanner);

		addMenuOptions();
		im.addFolderGap();
		im.addToggles(folderName, false);
		// TODO calculate the scroll value?

	}

	public void openNewFolder() {
		//TODO
		//TODO
		//TODO left off here, set up a new folder, get the keyboard in here eventually to start
		InventoryMenu im = new InventoryMenu();
		im.inventoryPanel.removeAll();

		JPanel newFolderPrompt = new JPanel();
		newFolderPrompt.setBackground(null);
		newFolderPrompt.setLayout(null);
		newFolderPrompt.setPreferredSize(new Dimension(screenX, folderY));

		JLabel titlePrompt = new JLabel("Enter Unique Folder Name");
		titlePrompt.setBounds(0, 0, titlePromptX, titlePromptY);
		titlePrompt.setFont(bannerFont);
		titlePrompt.setHorizontalAlignment(JLabel.CENTER);
		titlePrompt.setForeground(Color.WHITE);
		newFolderPrompt.add(titlePrompt);

		JTextField nameField = new JTextField();
		nameField.setBounds(titleTextFieldXL, titleTextFieldYL, titleTextFieldX, titleTextFieldY);
		nameField.setColumns(5);
		nameField.setBorder(fieldBorder);
		nameField.setHorizontalAlignment(SwingConstants.CENTER);
		nameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		im.inventoryPanel.add(newFolderPrompt);
	}

	public void openEditInventoryMenu(boolean adding) {
		InventoryMenu im = new InventoryMenu();
		im.inventoryPanel.removeAll();
		String prompt = "Highlight the timers you want to keep in the group";
		if (adding) {
			prompt = "Highlight the timers you want to add to the group";
		}
		JLabel folderMenuBanner = new JLabel(prompt);

		folderMenuBanner.setPreferredSize(new Dimension(screenX, (int) (folderY / 1.2)));
		folderMenuBanner.setFont(bannerFont);
		folderMenuBanner.setHorizontalAlignment(JLabel.CENTER);
		folderMenuBanner.setForeground(Color.WHITE);
		im.inventoryPanel.add(folderMenuBanner);

		JButton exitButton = new JButton("SAVE");
		exitButton.setPreferredSize(new Dimension(folderX, folderY));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(exitColor);
		exitButton.setBorder(exitBorder);
		exitButton.setFont(folderFont);
		exitButton.setFocusable(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFolder();

			}
		});

		im.inventoryPanel.add(exitButton);

		im.addToggles(folderName, true);
	}

	public String getName() {
		return folderName;
	}

	private void addMenuOptions() {
		InventoryMenu im = new InventoryMenu();
		JButton exitButton = new JButton("RETURN");
		exitButton.setPreferredSize(new Dimension(folderX, folderY));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(exitColor);
		exitButton.setBorder(exitBorder);
		exitButton.setFont(folderFont);
		exitButton.setFocusable(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				im.inventoryPanel.removeAll();

				@SuppressWarnings("unused")
				InventoryMenu im = new InventoryMenu();

			}
		});
		im.inventoryPanel.add(exitButton);

		JButton addTimersButton = new JButton();
		addTimersButton.setPreferredSize(new Dimension(invetoryOptionButtonX, folderY));
		addTimersButton.setIcon(resizedNewTimerIcon);
		addTimersButton.setBackground(inventoryOptionColor);
		addTimersButton.setFocusable(false);
		addTimersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				openEditInventoryMenu(true);

			}
		});

		JButton removeTimersButton = new JButton();
		removeTimersButton.setPreferredSize(new Dimension(invetoryOptionButtonX, folderY));
		removeTimersButton.setIcon(resizedDeleteTimerIcon);
		removeTimersButton.setBackground(inventoryOptionColor);
		removeTimersButton.setFocusable(false);
		removeTimersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				openEditInventoryMenu(false);

			}
		});

		if (!folderName.equals("All")) {
			im.inventoryPanel.add(addTimersButton);
			im.inventoryPanel.add(removeTimersButton);
		}

		JButton renameButton = new JButton();
		renameButton.setPreferredSize(new Dimension(invetoryOptionButtonX, folderY));
		renameButton.setIcon(resizedRenameIcon);
		renameButton.setBackground(inventoryOptionColor);
		renameButton.setFocusable(false);
		renameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		im.inventoryPanel.add(renameButton);
	}

}
