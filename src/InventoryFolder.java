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
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InventoryFolder {
	CurrentSession cs = new CurrentSession();

	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());

	private int folderX = (int) (screenX * .18);
	private int folderY = (int) (screenY * .1);

	private int cardX = (int) (.3125 * screenX);
	private int cardY = (int) (.6 * screenY);

	private int folderIconX = (int) (screenX * .055);
	private int folderIconY = (int) (screenY * .055);

	private int folderLabelY = (int) (screenY * .03);

	private int titlePromptXL = (int) (screenX * .2);
	private int titlePromptX = (int) (screenX * .33);
	private int titlePromptY = (int) (folderY / 1.2);

	private int inventoryOptionXY = (int) (screenX * .045);
	private int deleteFolderIconXY = (int) (screenX * .045);
	private int invetoryOptionButtonX = (int) (screenX * .07);

	private int keyPadButtonX = (int) (.3 * cardX);
	private int keyPadButtonY = (int) (.21 * cardY);

	private int enterButtonXL = (int) (.7 * screenX);

	private int titleTextFieldXL = (int) (.5 * screenX);
	private int titleTextFieldYL = (int) (.05 * titlePromptY);
	private int titleTextFieldX = (int) (.55 * titlePromptX);
	private int titleTextFieldY = (int) (.074 * screenY);

	private int keyboardX = (int) (.0935 * screenX);
	private int keyboardBottomRowX = (int) (.091 * screenX);
	private int keyboardY = (int) (.183 * screenY);
	private int backspaceX = (int) (.13 * screenX);
	private int switchToNumBtnX = (int) (.075 * screenX);
	private int keyPadYL = (int) (.05 * cardY);
	private int keyPadY = (int) (.9 * cardY);

	Icon resizedFolderIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-folder.png"))
			.getImage().getScaledInstance(folderIconX, folderIconY, Image.SCALE_SMOOTH));

	Icon resizedNewTimerIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-new-timer.png")).getImage()
					.getScaledInstance(inventoryOptionXY, inventoryOptionXY, Image.SCALE_SMOOTH));

	Icon resizedDeleteTimerIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-delete-timer.png")).getImage()
					.getScaledInstance(inventoryOptionXY, inventoryOptionXY, Image.SCALE_SMOOTH));

	Icon resizedDeleteFolderIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-delete-folder.png")).getImage()
					.getScaledInstance(deleteFolderIconXY, deleteFolderIconXY, Image.SCALE_SMOOTH));

	private Font folderFont = new Font("Helvetica", Font.BOLD, ((int) (.016 * screenX)));
	private Font bannerFont = new Font("Helvetica", Font.BOLD, ((int) (.03 * screenX)));
	private Font promptFieldFont = new Font("Helvetica", Font.BOLD, (int) (screenX * .02));
	private Font keyboardFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.0625 * screenY));
	private Font backspaceFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.03 * screenY));
	private Font numSwitchFont = new Font("Helvetica", Font.ITALIC, (int) (.035 * screenY));

	Border fieldBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
	Border exitBorder = BorderFactory.createLineBorder(Color.RED, 3);

	// Colors
	private Color exitColor = Color.decode("#CC2936");
	private Color inventoryOptionColor = Color.decode("#6B818C");
	private Color enterColor = Color.decode("#4DA167");
	private Color backColor = Color.decode("#CC2936");
	private Color confirmColor = Color.decode("#4DA167");
	private Color keyboardBackgroundColor = Color.decode("#C2C1C2");
	private Color switchToNumColor = Color.decode("#6B818C");

	private JButton folder;
	private String folderName = "Error";

	private RoundedPanel keyboardPanel = StartUp.mainKeyboard;
	private JPanel keyPadPanel = new JPanel();
	private RoundedPanel popupPanel = new RoundedPanel();
	JButton enterButton;

	public InventoryFolder(String name) {
		folderName = name;
	}

	public InventoryFolder() {

	}

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
				openFolder(name);

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
				openFolder(name);
			}
		});

		if (cs.checkFolderTitle(name)) {
			CurrentSession.folders.add(this);
		}
		return folderPanel;

	}

	private void openFolder(String name) {
		folderName = name;
		InventoryMenu im = new InventoryMenu();
		im.inventoryPanel.removeAll();

		JLabel folderMenuBanner = new JLabel("Inventory Menu - " + folderName);

		folderMenuBanner.setPreferredSize(new Dimension(screenX, (int) (folderY / 1.2)));
		folderMenuBanner.setFont(bannerFont);
		folderMenuBanner.setHorizontalAlignment(JLabel.CENTER);

		folderMenuBanner.setForeground(Color.WHITE);
		im.inventoryPanel.add(folderMenuBanner);

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

		JButton deleteFolderButton = new JButton();
		deleteFolderButton.setPreferredSize(new Dimension(invetoryOptionButtonX, folderY));
		deleteFolderButton.setIcon(resizedDeleteFolderIcon);
		deleteFolderButton.setBackground(inventoryOptionColor);
		deleteFolderButton.setFocusable(false);
		deleteFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InventoryMenu im = new InventoryMenu();
				im.inventoryPanel.removeAll();
				im.toggleScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				im.inventoryPanel.add(popupPanel);
				popupPanel.setPreferredSize(new Dimension(cardX, (int) (cardY * .55)));
				popupPanel.setVisible(true);

				String promptText = "<html>Are you sure you want to<br/>delete " + name
						+ "?<br/><br/> Timers contained within <br/>the folder will not be deleted.</html>";
				JLabel prompt = new JLabel(promptText);
				prompt.setFont(promptFieldFont);
				prompt.setHorizontalAlignment(SwingConstants.CENTER);
				prompt.setBounds(0, 0, cardX, (int) (cardY * 75));
				popupPanel.add(prompt);

				JButton cancelButton = new JButton("  Cancel  ");
				cancelButton.setBounds((int) (cardX * .05), (int) (cardY * .65), invetoryOptionButtonX,
						titleTextFieldY);
				cancelButton.setBackground(exitColor);
				cancelButton.setFont(backspaceFont);
				cancelButton.setForeground(Color.WHITE);
				cancelButton.setFocusable(false);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						popupPanel.removeAll();
						popupPanel.setVisible(false);
						openFolder(name);
					}
				});
				popupPanel.add(cancelButton);

				JButton confirmButton = new JButton("  Confirm  ");
				confirmButton.setBounds((int) (cardX * .55), (int) (cardY * .65), invetoryOptionButtonX,
						titleTextFieldY);
				confirmButton.setBackground(confirmColor);
				confirmButton.setFont(backspaceFont);
				confirmButton.setForeground(Color.WHITE);
				confirmButton.setFocusable(false);
				confirmButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						popupPanel.removeAll();
						popupPanel.setVisible(false);
						for (InventoryFolder temp : CurrentSession.folders) {
							if (temp.getName().equals(name)) {
								CurrentSession.folders.remove(temp);
								break;
							}
						}

						for (int i = 0; i < cs.getTNOT(); i++) {
							if (CurrentSession.itHash.get(i) != null) {
								if (CurrentSession.itHash.get(i).getInventoryGroups().contains(name)) {
									CurrentSession.itHash.get(i).getInventoryGroups().remove(name);
								}
							}
						}

						@SuppressWarnings("unused")
						InventoryMenu im = new InventoryMenu();

					}
				});
				popupPanel.add(confirmButton);
			}
		});
		if (!folderName.equals("ALL")) {
			im.inventoryPanel.add(addTimersButton);
			im.inventoryPanel.add(removeTimersButton);
			im.inventoryPanel.add(deleteFolderButton);
		}

		im.addFolderGap();
		im.addToggles(folderName, false);

	}

	public void openNewFolder() {

		InventoryMenu im = new InventoryMenu();
		im.inventoryPanel.removeAll();

		im.toggleScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		JPanel newFolderPrompt = new JPanel();
		newFolderPrompt.setBackground(null);
		newFolderPrompt.setLayout(null);
		newFolderPrompt.setPreferredSize(new Dimension(screenX, folderY));

		JLabel titlePrompt = new JLabel("Enter Unique Folder Name");
		titlePrompt.setBounds(titlePromptXL, 0, titlePromptX, titlePromptY);
		titlePrompt.setFont(promptFieldFont);
		titlePrompt.setHorizontalAlignment(JLabel.CENTER);
		titlePrompt.setForeground(Color.WHITE);
		newFolderPrompt.add(titlePrompt);

		JTextField nameField = new JTextField();
		nameField.setBounds(titleTextFieldXL, titleTextFieldYL, titleTextFieldX, titleTextFieldY);
		nameField.setColumns(5);
		nameField.setBorder(fieldBorder);
		nameField.setFocusable(false);
		nameField.setHorizontalAlignment(SwingConstants.CENTER);
		nameField.setFont(promptFieldFont);
		newFolderPrompt.add(nameField);

		InventoryFolder newFolder = this;

		enterButton = new JButton("Enter");
		enterButton.setBounds(enterButtonXL, titleTextFieldYL, 2*invetoryOptionButtonX, titleTextFieldY);
		enterButton.setBackground(enterColor);
		enterButton.setFont(backspaceFont);
		enterButton.setForeground(Color.WHITE);
		enterButton.setFocusable(false);

		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = nameField.getText();
				if (cs.checkFolderTitle(input) && input.length() > 0 && input.length() < 14) {
					folderName = input;
					keyboardPanel.removeAll();
					keyboardPanel.setVisible(false);
					popupPanel.setVisible(false);
					keyPadPanel.removeAll();
					popupPanel.setVisible(false);
					CurrentSession.folders.add(newFolder);
					openFolder(folderName);
					TaskBar tb = new TaskBar();
					tb.updateBar("MENU");
				} else {
					nameField.setBorder(exitBorder);
					nameField.setText("");
				}

			}
		});
		newFolderPrompt.add(enterButton);

		im.inventoryPanel.add(newFolderPrompt);
		im.inventoryPanel.add(popupPanel);
		popupPanel.setVisible(false);

		TaskBar tb = new TaskBar();
		tb.updateBar("UNDO");
		displayKeyboard(nameField);
	}

	public void displayKeyboard(JTextField tf) {

		String[] qwerty = new String[] { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H",
				"J", "K", "L", "Z", "X", "C", "V", "B", "N", "M" };
		JButton[] KBArray = new JButton[26];

		// Clear out the old components.
		keyboardPanel.removeAll();

		// Fill in keys for the rows before the backspace button.
		for (int curKeyNum = 0; curKeyNum < 19; curKeyNum++) {
			String thisButtonsValue = (qwerty[curKeyNum]);
			KBArray[curKeyNum] = new JButton((qwerty[curKeyNum]));
			KBArray[curKeyNum].setPreferredSize(new Dimension(keyboardX, keyboardY));
			KBArray[curKeyNum].setFont(keyboardFont);
			KBArray[curKeyNum].setFocusable(false);
			KBArray[curKeyNum].setBackground(keyboardBackgroundColor);
			KBArray[curKeyNum].setBorder(fieldBorder);
			KBArray[curKeyNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String priorText = tf.getText();
					tf.setText(priorText += thisButtonsValue);
				}
			});
			keyboardPanel.add(KBArray[curKeyNum]);
		}

		// Backspace button for keyboard
		JButton bspBtn = new JButton("Backspace");
		bspBtn.setPreferredSize(new Dimension(backspaceX, keyboardY));
		bspBtn.setFont(backspaceFont);
		bspBtn.setForeground(Color.WHITE);
		bspBtn.setFocusable(false);
		bspBtn.setBackground(backColor);
		bspBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (tf.getText().length() > 0) {
					StringBuffer sb = new StringBuffer(tf.getText());
					sb = sb.deleteCharAt(tf.getText().length() - 1);
					tf.setText(sb.toString());
				}
			}
		});
		keyboardPanel.add(bspBtn);

		// Fill in the remaining keys for the bottom row of keyboard.
		for (int curKeyNum = 19; curKeyNum < 26; curKeyNum++) {
			String thisButtonsValue = (qwerty[curKeyNum]);
			KBArray[curKeyNum] = new JButton((qwerty[curKeyNum]));
			KBArray[curKeyNum].setPreferredSize(new Dimension(keyboardBottomRowX, keyboardY));
			KBArray[curKeyNum].setFont(keyboardFont);
			KBArray[curKeyNum].setBackground(keyboardBackgroundColor);
			KBArray[curKeyNum].setFocusable(false);
			KBArray[curKeyNum].setBorder(fieldBorder);
			KBArray[curKeyNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String priorText = tf.getText();
					tf.setText(priorText += thisButtonsValue);
				}
			});
			keyboardPanel.add(KBArray[curKeyNum]);
		}

		JButton switchToNumBtn = new JButton("123");
		switchToNumBtn.setPreferredSize(new Dimension(switchToNumBtnX, keyboardY));
		switchToNumBtn.setFont(numSwitchFont);
		switchToNumBtn.setFocusable(false);
		switchToNumBtn.setBackground(switchToNumColor);
		switchToNumBtn.setForeground(Color.WHITE);
		switchToNumBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				displayKeypad(tf, true);

			}
		});
		keyboardPanel.add(switchToNumBtn);
		// Add the start button to the keyboard.
		JButton startBtn = new JButton("Enter");
		startBtn.setBackground(confirmColor);
		startBtn.setFont(backspaceFont);
		startBtn.setForeground(Color.WHITE);
		startBtn.setPreferredSize(new Dimension(keyboardX, keyboardY));
		startBtn.setVisible(true);
		startBtn.setFocusable(true);

		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				enterButton.doClick();
			}
		});
		keyboardPanel.add(startBtn);
		keyboardPanel.setVisible(true);

	};

	public void displayKeypad(JTextField tf, boolean isCreatePanel) {

		// Creating panels

		popupPanel.setPreferredSize(new Dimension(cardX, cardY));
		popupPanel.setOpaque(false);
		popupPanel.setLayout(null);
		popupPanel.setVisible(true);
		popupPanel.setBackground(Color.white);

		keyPadPanel.setOpaque(false);
		keyPadPanel.setBounds(0, keyPadYL, cardX, keyPadY);
		keyPadPanel.setLayout(new FlowLayout());
		keyPadPanel.removeAll();
		keyboardPanel.setVisible(false);
		keyPadPanel.setVisible(true);

		// Numbers for keypad in the order they appear.
		String[] kpdigits = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0" };

		JButton[] buttonArray = new JButton[10];

		// Add buttons to keypad before backspace button.
		for (int curButtonNum = 0; curButtonNum < 9; curButtonNum++) {
			int thisButtonsValue = Integer.parseInt(kpdigits[curButtonNum]);

			buttonArray[curButtonNum] = new JButton((kpdigits[curButtonNum]));
			buttonArray[curButtonNum].setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
			buttonArray[curButtonNum].setFont(keyboardFont);
			buttonArray[curButtonNum].setBorder(fieldBorder);
			buttonArray[curButtonNum].setFocusable(false);
			buttonArray[curButtonNum].setBackground(keyboardBackgroundColor);
			buttonArray[curButtonNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					tf.setText(tf.getText() + thisButtonsValue);
				}
			});
			keyPadPanel.add(buttonArray[curButtonNum]);
		}

		// Add the backspace button to the keypad.
		JButton bspBtn = new JButton("X");
		bspBtn.setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		bspBtn.setFont(keyboardFont);
		bspBtn.setForeground(Color.WHITE);
		bspBtn.setFocusable(false);
		bspBtn.setBorder(fieldBorder);
		bspBtn.setBackground(backColor);
		bspBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				if (tf.getText().length() > 0) {
					StringBuffer sb = new StringBuffer(tf.getText());
					sb = sb.deleteCharAt(tf.getText().length() - 1);
					tf.setText(sb.toString());
				}
			}
		});
		keyPadPanel.add(bspBtn);

		String lastBtnText = "ABC";
		Font lastBtnFont = numSwitchFont;
		Color lastBtnColor = switchToNumColor;

		buttonArray[9] = new JButton((lastBtnText));
		buttonArray[9].setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		buttonArray[9].setFont(lastBtnFont);
		buttonArray[9].setBorder(fieldBorder);
		buttonArray[9].setFocusable(false);
		buttonArray[9].setBackground(lastBtnColor);
		buttonArray[9].setForeground(Color.WHITE);

		buttonArray[9].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				displayKeyboard(tf);
				keyPadPanel.setVisible(false);
				popupPanel.setVisible(false);
				popupPanel.repaint();
				popupPanel.revalidate();

			}
		});
		keyPadPanel.add(buttonArray[9]);

		// Add the start button to the keypad.
		JButton startBtn = new JButton("O");
		startBtn.setBackground(confirmColor);
		startBtn.setFocusable(false);
		startBtn.setFont(keyboardFont);
		startBtn.setBorder(fieldBorder);
		startBtn.setForeground(Color.WHITE);
		startBtn.setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		startBtn.setVisible(true);

		keyPadPanel.add(startBtn);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				enterButton.doClick();
			}

		});
		popupPanel.add(keyPadPanel);
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
				openFolder(folderName);

			}
		});

		im.inventoryPanel.add(exitButton);

		im.addToggles(folderName, true);
	}

	public String getName() {
		return folderName;
	}

}
