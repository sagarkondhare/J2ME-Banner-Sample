package mobi.vserv.J2MEbannerSample;

import java.util.Hashtable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

import vInAppAdEngine.VservAd;
import vInAppAdEngine.VservAdListener;
import vInAppAdEngine.VservManager;

public class J2MEbannerSample extends MIDlet implements CommandListener,
		VservAdListener, ItemCommandListener {

	public J2MEbannerSample() {

		// This is required only once in your application life cycle
		Hashtable vservConfigTable = new Hashtable();
		vservConfigTable.put("appId", "20846");
		vservManager = new VservManager(this, vservConfigTable);

	}

	// Display
	private Display display;
	// Main form
	private Form form;
	// For the message
	private StringItem string;
	// For the exit command
	private Command exitCommand;

	// Vserv Ad Manager
	private VservManager vservManager;
	// Vserv Ad
	private VservAd vservAd;

	// Ad Placeholders
	private ImageItem imageItem;
	private StringItem stringItem;

	public void commandAction(Command command, Displayable displayable) {
		if (displayable == form) {
			if (command == exitCommand) {
				exitMIDlet();
			}
		}
	}

	public void startApp() {
		// Create form
		string = new StringItem("Hello", "Hello World!");
		form = new Form(null, new Item[] { string });
		exitCommand = new Command("Exit", Command.EXIT, 1);
		form.addCommand(exitCommand);
		form.setCommandListener(this);

		// This is required for requesting new ad
		vservAd = new VservAd(this);
		vservAd.requestAd();

		// Get display for drawning
		display = Display.getDisplay(this);
		display.setCurrent(form);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public void exitMIDlet() {
		display.setCurrent(null);
		notifyDestroyed();
	}

	public void vservAdFailed(Object arg0) {

	}

	public void vservAdReceived(Object arg0) {
		if (((VservAd) arg0).getAdType().equals(VservAd.AD_TYPE_IMAGE)) {
			// Use retrived image ad for rendering
			Image imageAd = (Image) ((VservAd) arg0).getAd();
			imageItem = new ImageItem("", imageAd, ImageItem.LAYOUT_DEFAULT,
					"", Item.BUTTON);
			imageItem
					.setDefaultCommand(new Command("ClickImgAd", Command.OK, 2));
			imageItem.setItemCommandListener(this);
			form.append(imageItem);
		} else if (((VservAd) arg0).getAdType().equals(VservAd.AD_TYPE_TEXT)) {
			// Use retrieved text ad for rendering
			String textAd = (String) ((VservAd) arg0).getAd();
			stringItem = new StringItem("", textAd, Item.BUTTON);
			stringItem.setDefaultCommand(new Command("ClickTextAd", Command.OK,
					2));
			stringItem.setItemCommandListener(this);
			form.append(stringItem);
		}
	}

	// Handle command action to visit ad
	public void commandAction(Command com, Item item) {
		if (item == imageItem) {
			if (vservAd.hasAction)
				vservAd.handleAdAction();
		} else if (item == stringItem) {
			if (vservAd.hasAction)
				vservAd.handleAdAction();
		}
	}
}