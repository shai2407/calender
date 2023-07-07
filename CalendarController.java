import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class CalendarController {

	final int NUM_OF_BUTTONS_IN_LINE = 7;
	final int NUM_OF_BUTTONS = 42;
	final int FIRST_YEAR = 2020;
	final int LAST_YEAR = 2030;
	@FXML
	private GridPane grid;
	@FXML
	private ComboBox<String> monthC;
	@FXML
	private ComboBox<String> yearC;
	private Button[] btns;
	private HashMap<Calendar,String> events;

	public void initialize(){
		// initialize variables
		events = new HashMap<Calendar,String>();
		setMonths();
		setYears();
		btns = new Button[NUM_OF_BUTTONS];
		for(int i = 0 ; i < NUM_OF_BUTTONS; i++) {
			btns[i] = new Button();
			btns[i].setVisible(false);
			
			// buttons design
			btns[i].setStyle("-fx-font-size:12");
			btns[i].setPrefSize(grid.getPrefWidth()/ NUM_OF_BUTTONS_IN_LINE ,grid.getPrefHeight()/ NUM_OF_BUTTONS_IN_LINE);
			grid.add(btns[i], i%NUM_OF_BUTTONS_IN_LINE, i/NUM_OF_BUTTONS_IN_LINE);
		}
	}
	
	@FXML
	void monthPressed(ActionEvent event) {
		if(monthC.getValue() == null || yearC.getValue() == null )
			return;
		setButtons();
	}
	
	@FXML
	void yearPressed(ActionEvent event) {
		if(monthC.getValue() == null || yearC.getValue() == null )
			return;
		setButtons();
	}
	
	// set buttons according to input of year and month
	private void setButtons() {
		clearGrid();
		// set parameters
		int currYear = Integer.parseInt(yearC.getValue());
		int currMonth = monthC.getItems().indexOf(monthC.getValue());
		int daysInMonth = getMaxDaysInMonth(currMonth,currYear);
		int firstInTheMonth = getDayOfWeek(currMonth,currYear) - 1; // fit as index
		int numOfDay = 1;
		// set buttons
		for(int i = firstInTheMonth ; i < daysInMonth + firstInTheMonth;i++) {
			String temp = String.valueOf(numOfDay);
			btns[i].setVisible(true);
			btns[i].setText(temp);
			numOfDay++;
			// button on action function
			btns[i].setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					btnPreesed(event,currYear,currMonth);
				}
			});
		}
	}
	
	// button on action function
	private void btnPreesed(ActionEvent event, int currYear, int currMonth) {
		// set calendar instance
		Button b = (Button)event.getSource();
		int currDay = Integer.parseInt(b.getText());
		Calendar cal = Calendar.getInstance();
		cal.set(currYear, currMonth, currDay);
		// view or add
		String[] options = {"Add", "View"};
		int asnwered = JOptionPane.showOptionDialog(null,"View events or Add an event", null, 0, 0, null, options,options[0]);	
		if (asnwered == 0) {
			add(cal);
		}
		if (asnwered == 1) {
			view(cal);
		}
	}
	
	private void add(Calendar cal) {
		String reminder = JOptionPane.showInputDialog("Write your event:");
		events.put(cal, reminder);
	}

	private void view(Calendar cal) {
		boolean empty = true;
		String dayEvents = "";
		// find relevant events. store it in dayEvents
		for (Map.Entry<Calendar,String> entry:events.entrySet()) {
			if(sameDay(entry.getKey(),cal)) {
				empty = false;
				dayEvents += entry.getValue() + "\n";
			}
		}
		// show all events
		if(!empty) {
			String[] options = {"Edit", "Exit"};
			if(0 == JOptionPane.showOptionDialog(null,"Your meeting are:\n" + dayEvents, null, 0, 0, null, options,options[0]))
				edit(cal);
		}
		// no events on this day
		if(empty)
			JOptionPane.showMessageDialog(null,"You are free today! Go to a nice hike");
	}
	
	private void edit(Calendar cal) {
		String dayEvents = "";
		int i = 1;
		// view
		for (Map.Entry<Calendar,String> entry:events.entrySet()) {
			if(sameDay(entry.getKey(),cal)) {
				dayEvents += "\n" + Integer.toString(i)+". " + entry.getValue() + "\n";
				i++;
			}
		}
		// choose which event to edit
		String answer = JOptionPane.showInputDialog("Your meeting today are:" + dayEvents + "\nChoose the number of the meeting you want to edit.");
		while(invalidAns(answer,i) ) {
			answer = JOptionPane.showInputDialog("Your meeting today are:" + dayEvents + "\nChoose the number of the meeting you want to edit.");
		}
		// edit
		int ans = Integer.parseInt(answer);
		String change = JOptionPane.showInputDialog("Write again your meeting.");
		// find the same event again
		int j = 1;
		for (Map.Entry<Calendar,String> entry:events.entrySet()) {
			if(sameDay(entry.getKey(),cal)) {
				// set the changes
				if(ans == j) {
					entry.setValue(change);
				}
				j++;
			}
		}
	}
	
	// utils
	
	private void setMonths() {
		monthC.getItems().addAll(
				"January","February","March","April","May",
				"June","July","Augost","September",
				"October","November","December"
				);	
	}

	private void setYears() {
		for (int i = FIRST_YEAR; i<LAST_YEAR;i++) {
			String temp = String.valueOf(i);
			yearC.getItems().add(temp);
		}
	}
	
	// clear grid from past buttons
	private void clearGrid() {
		for(int j = 0; j < NUM_OF_BUTTONS; j++) {
			btns[j].setText("");
			btns[j].setVisible(false);		
		}
	}
	
	private static int getMaxDaysInMonth(int month, int year) {
		Calendar cal = Calendar.getInstance();
		// Note: 0-based months
		cal.set(year, month, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	private static int getDayOfWeek(int month, int year) {
		Calendar cal = Calendar.getInstance();
		// Note: 0-based months
		cal.set(year, month, 1);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	// check if c1 == c2
	private boolean sameDay(Calendar c1,Calendar c2) {
		if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {	
			if(c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
				if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// check if your answer is an index of one of your meetings
	private boolean invalidAns(String answer,int i) {
		int foo;
		try {
			foo = Integer.parseInt(answer);
			for(int j = 1;j< i; j++) {
				// input is Integer and a relevant one
				if(foo == j)
					return false;
			}
			return true;

		}
		catch (NumberFormatException e) {
			return true;
		}
	}
}