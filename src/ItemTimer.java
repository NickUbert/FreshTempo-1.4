import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * ItemTimer Class creates new ItemTimer Objects These objects should have
 * unique values that can be referenced in the future The class contains the
 * attributes held by each timer along with key functions.
 * 
 * Author: @nu Last Modified: 6/16/19
 */

public class ItemTimer {
	private int startMin;
	private int startHour;
	private String timerTitle;
	private String timeDisplayString;
	private int sec;
	private int min;
	private int hour;
	private int prgValue;
	private int timerID;
	private int colorCodeInt;
	private double timerVelocity;
	private boolean paused;
	private TimerGraphics timerGraphics;
	public JPanel timerPanel;
	private TimerGraphics tg;
	private boolean prior;
	private boolean toggled;
	private boolean currentlyExpired;
	private boolean justRefreshed;
	private Color backgroundColor = Color.decode("#223843");
	private Color flashColor = Color.decode("#CC2936");
	CurrentSession cs = new CurrentSession();

	/*
	 * Constructor sets itemTimer values to user inputs.
	 */
	public ItemTimer(int min, int hour, String title, int id, boolean loaded, boolean isToggled) {
		// Set all initial values needed when timers are created since the old values
		// are refrenced later in the session when timers are used.
		startMin = min;
		startHour = hour;
		timerTitle = title;
		prior = loaded;
		paused = loaded;
		startCountDown(false);
		timerID = id;
		colorCodeInt = 1;
		CurrentSession.itHash.put(id, this);
		cs.updateCAP();
		tg = new TimerGraphics(this);
		timerPanel = tg.getTimerPanel();
		timerGraphics = tg;
		tg.createTimerUI();
		toggled = isToggled;
		currentlyExpired = false;
		// All pre loaded timers are set to paused onced loaded to avoid overwhelming
		// startups for the user.
		if (loaded) {
			tg.switchToPauseGraphics();
		}

	}

	/*
	 * Empty constructor call is used when the itemTimer is just being referenced
	 * and not creating a new timer.
	 */
	public ItemTimer() {

	}

	/*
	 * startCountDown reassigns the values to user inputs and starts the count down
	 * process, also used for timer resets.
	 */
	public void startCountDown(boolean resetting) {
		if (resetting) {
			countDown.stop();

		}

		// Reset timer values to base values
		prgValue = 0;
		sec = 60;

		// Handles all initial conversions.
		if (startMin != 0) {
			min = startMin - 1;
		} else {
			min = startMin;
		}
		if (startMin != 0 && startHour > 0) {
			hour = startHour;
		} else {
			hour = startHour;
		}
		if (startMin == 0 && startHour == 0) {
			min = 0;
			sec = 0;
			prgValue = 100;
		}
		if (resetting) {
			timerGraphics.getPrg().setValue(prgValue);
			timerGraphics.getPrg().repaint();
			timerGraphics.getPrg().revalidate();
			timerGraphics.getTimeDisplay().setText(timeDisplayString);
			timerGraphics.getTimeDisplay().repaint();
			timerGraphics.getTimeDisplay().revalidate();
		}
		countDown.start();

	}

	/*
	 * countDown is responsible for count down function of ItemTimers and general
	 * conversions of timer values. decrementSec is a helper method used so that
	 * each timer counting down does not affect other timers running at the same
	 * time.
	 */

	// Decrement sec handles increments to timer values since this. calls don't work
	// within Timer objects
	private void decrementSec() {
		this.sec--;
		this.prgValue++;

	}

	Timer countDown = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			if (justRefreshed) {
				justRefreshed = false;
				timerGraphics.getTimeDisplay().setForeground(Color.BLACK);
				timerGraphics.refreshTimer();

			}
			decrementSec();
			CurrentSession cs = new CurrentSession();

			// Converstions while timer is running
			if (sec == -1 && min >= 1) {
				sec = 59;
				min--;

			}
			if (min == 0 && hour >= 1) {
				min = 59;
				hour--;
			}

			if (sec == -60 && min <= 0) {
				sec = 0;
				min--;
				if (min == -60) {
					min = 0;
					hour--;
				}
			}

			// Handles the moment of expiration, currently just adjusts timer and session
			// values.
			if (sec == 0 && min == 0 && hour == 0) {
				cs.increaseNOAE();

				if (cs.getNOAE() == 1) {
					cs.setActiveExpiratons(true);
					currentlyExpired = true;
				}
			}

			// Call the method responsible for updating the time, but not painting it
			timeDisplayString = timeValueToString(sec, min, hour);

			// Handles all graphic updates for time passing
			if (!cs.getTyping()) {
				timerGraphics.getPrg().setValue(prgValue);
				timerGraphics.getPrg().repaint();
				timerGraphics.getPrg().revalidate();
				timerGraphics.getTimeDisplay().setText(timeDisplayString);
			}

			// Calls the sort method for the session, might be able to cut down on latency
			// if theres a first timer in use check like the one used for background
			// flashes.
			if (!cs.getMenuOpen()) {
				Sorter so = new Sorter();
				so.sort(CurrentSession.itHash);
			}

			// Simply updating the prior value, not sure if it needs to be here though
			if (prior) {
				countDown.stop();
				prior = false;
			}
		}
	});

	/*
	 * timeValueToString is responsible for converting the current time values to
	 * Strings which are used to update the time display for each ItemTimer The
	 * method uses a formatting function and ultimately returns a string.
	 */
	@SuppressWarnings("static-access")
	public String timeValueToString(int sec, int min, int hour) {

		// converts all time values used for painting to absloute values so that its
		// easier proccessing the final string needed.
		int s = Math.abs(sec);
		int m = Math.abs(min);
		int h = Math.abs(hour);
		String formattedTime = "Error";
		String n = "";

		// adds negative symbol to string since all values are passed through Math.abs()
		if (sec <= -1 || min < 0 || hour < 0) {
			n = "-";
			currentlyExpired = true;
			// TODO: update flash is on time out because I dont know whether to remove the
			// feature or let it stutter flash.
			// updateBackgroundFlash();
			tg.getTimeDisplay().setForeground(flashColor);
		}

		// handles all values when hour is not displayed.
		if (hour < 1 && hour > -1) {
			if (min == 0) {
				if (s < 10) {
					return (n + "0:0" + s);
				}
				return (n + "0:" + s);

			}
			if (s >= 10) {
				return formattedTime.format(n + "%2$s:%1$s", s, m);
			} else {
				return formattedTime.format(n + "%2$s:0%1$s", s, m);
			}

			// handles all values with hours
		} else if (hour >= 1 || hour <= -1) {

			// handles all values with double digit minutes.
			if (min >= 10 || min <= -10) {
				if (sec >= 10 || sec <= -10) {
					return formattedTime.format(n + "%3$s:%2$s:%1$s", s, m, h);
				} else {
					return formattedTime.format(n + "%3$s:%2$s:0%1$s", s, m, h);
				}
				// handles all values with single digit minutes.
			} else {
				if (sec >= 10 || sec <= -10) {
					return formattedTime.format(n + "%3$s:0%2$s:%1$s", s, m, h);
				} else {
					return formattedTime.format(n + "%3$s:0%2$s:0%1$s", s, m, h);
				}
			}
		}
		return formattedTime;
	}

	/*
	 * getCurTimeString is used to return a formated time string without needing to
	 * enter the current time values as parameters.
	 */
	public String getCurTimeString() {
		return timeValueToString(sec, min, hour);
	}

	/*
	 * switchTimerLayout is a void method used to call a specific method within the
	 * timer graphics object linked to each specific itemTimer.
	 */
	public void switchTimerLayout() {
		tg.switchLayout();
	}

	/*
	 * getMax is used to set the max value for all new progress bars, it returns the
	 * starting time values in seconds. min is min+1 because of automatic
	 * conversions.
	 */
	public int getMax() {
		int max = ((min + 1) * 60) + (hour * 3600);
		return max;
	}

	/*
	 * getPause is used by boolean checkers for graphics and functions of UI
	 */
	public boolean getPause() {
		return paused;
	}

	/*
	 * setPause is used to assign a boolean value that indicates if this timer is
	 * paused by the user.
	 */
	public void setPause(boolean b) {
		paused = b;
		if (b) {
			tg.switchToPauseGraphics();
		}
	}

	/*
	 * getTitle returns the name assigned to this timer by the user.
	 */
	public String getTitle() {
		return timerTitle;
	}

	/*
	 * getTimerPanel returns all components contained by each timer mainly used when
	 * adding timers to pages.
	 */
	public JPanel getTimerPanel() {
		return timerPanel;
	}

	/*
	 * getPrgVelocity returns a double value used to describe a timers velocity
	 * velocity is a shitty term but it's accurate so don't @ me, progress velocity
	 * describes the timers current progress divided it's max time.
	 * 
	 * This used to use the unique name identifier, it was refactored to use a time
	 * ID instead.
	 * 
	 */
	public double getPrgVelocity() {
		int currentSec = sec;
		currentSec += (min * 60);
		currentSec += (hour * 3600);

		int initialSec = (startMin * 60);
		initialSec += (startHour * 3600);

		timerVelocity = (double) (currentSec) / (double) (initialSec);

		timerVelocity += ((double) timerID / (double) 10000000);
		return timerVelocity;

	}

	/*
	 * getTimerID returns the id assigned to this timer
	 */
	public int getTimerID() {
		return timerID;
	}

	/*
	 * setTimerID is used when timers are priors and loaded on startup. Never use
	 * outside of startup to avoid orphans
	 */
	public void setTimerID(int i) {
		timerID = i;
	}

	/*
	 * getStartMin returns the inital value used for minutes
	 */
	public int getStartMin() {
		return startMin;
	}

	/*
	 * getStartHour returns the inital value used for hours
	 */
	public int getStartHour() {
		return startHour;
	}

	/*
	 * setCurSec is used to update the current second value, should only be used by
	 * startup when loading the prior time
	 */
	public void setCurSec(int i) {
		sec = i;
	}

	/*
	 * setCurMin is used to update the current minute value, should only be used by
	 * startup when loading the prior time
	 */
	public void setCurMin(int i) {
		min = i;
	}

	/*
	 * setCurHour is used to update the current hour value, should only be used by
	 * startup when loading the prior time
	 */
	public void setCurHour(int i) {
		hour = i;
	}

	/*
	 * getCurSec returns the current sec value for this timer.
	 */
	public int getCurSec() {
		return this.sec;
	}

	/*
	 * getCurMin returns the current min value for this timer.
	 */
	public int getCurMin() {
		return this.min;
	}

	/*
	 * getCurHour returns the current hour value for this timer
	 */
	public int getCurHour() {
		return this.hour;
	}

	/*
	 * getColorCode returns the colorCode id value for this timer
	 */
	public int getColorCode() {
		return colorCodeInt;
	}

	/*
	 * increaseColorCode allows the value to cycle through the 3 options
	 */
	public void increaseColorCode() {
		colorCodeInt++;
		if (colorCodeInt == 4) {
			colorCodeInt = 1;
		}
	}

	/*
	 * getCurrentlyExpired returns the boolean for expiration status on this timer
	 */
	public boolean getCurrentlyExpired() {
		return currentlyExpired;
	}

	/*
	 * setCurrentlyExpired updates the current boolean value for expiration status
	 */
	public void setCurrentlyExpired(boolean b) {
		currentlyExpired = b;
	}

	public void setJustRefreshed(boolean b) {
		justRefreshed = b;
	}

	public boolean getDoubleTap() {
		return prgValue < 5;
	}

	/*
	 * setPrgValue is used to set a specific value to the progress bar linked with
	 * this itemTimer. This is mainly used by loading priors in order to update the
	 * prgValue of the timer.
	 */

	public void setPrgValue(int i) {
		prgValue = i;
		timerGraphics.getPrg().setValue(prgValue);
		timerGraphics.getPrg().repaint();
		timerGraphics.getPrg().revalidate();
	}

	/*
	 * switchToResumedGraphics exists because you have to call timerGraphics in
	 * refrence to this timer.
	 */
	public void switchToResumedGraphics() {
		tg.switchToResumedGraphics();
	}

	/*
	 * updateBackgroundFlash is used to check which timer should be calling the
	 * flashBackground method since there should only be one, which should be the
	 * lowest timerID that is expired and running
	 */
	private void updateBackgroundFlash() {

		if (!cs.getActiveExpirations() && cs.getNOAE() >= 1) {
			cs.setActiveExpiratons(true);
		}
		// TODO cant get this to stutter flash anymore but I'm sure it will happen again
		flashBackground(sec);

	}

	/*
	 * flashBackground is used to switch the background color when the timer calling
	 * it has an equal number for seconds
	 */
	private void flashBackground(int i) {

		if (i % 2 != 0) {
			StartUp.backgroundHash.get(cs.getCurrentPage()).setBackground(flashColor);
		} else {
			StartUp.backgroundHash.get(cs.getCurrentPage()).setBackground(backgroundColor);
		}

	}

	/*
	 * getTimer returns the Timer object being used by this ItemTimer
	 */
	public Timer getTimer() {
		return countDown;
	}

	/*
	 * getToggled returns a boolean showing whether or not the timer is currently
	 * toggled
	 */
	public boolean getToggled() {
		return toggled;
	}

	/*
	 * setToggled is used to update the boolean showing whether or not this timer is
	 * toggled
	 */
	public void setToggled(boolean b) {
		toggled = b;
	}
}