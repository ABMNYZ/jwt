/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
package eu.webtoolkit.jwt;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.ref.*;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.*;
import javax.servlet.*;
import eu.webtoolkit.jwt.*;
import eu.webtoolkit.jwt.chart.*;
import eu.webtoolkit.jwt.utils.*;
import eu.webtoolkit.jwt.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A date edit.
 * <p>
 * 
 * A date picker is a line edit with support for date entry (using an icon and a
 * calendar).
 * <p>
 * A {@link WDateValidator} is used to validate date entry.
 */
public class WDateEdit extends WLineEdit {
	private static Logger logger = LoggerFactory.getLogger(WDateEdit.class);

	/**
	 * Creates a new date edit.
	 */
	public WDateEdit(WContainerWidget parent) {
		super(parent);
		this.changed().addListener(this, new Signal.Listener() {
			public void trigger() {
				WDateEdit.this.setFromLineEdit();
			}
		});
		String TEMPLATE = "${calendar}";
		WTemplate t = new WTemplate(new WString(TEMPLATE));
		this.popup_ = new WPopupWidget(t, this);
		this.popup_.setAnchorWidget(this);
		this.popup_.setTransient(true);
		this.calendar_ = new WCalendar();
		this.calendar_.activated().addListener(this.popup_,
				new Signal1.Listener<WDate>() {
					public void trigger(WDate e1) {
						WDateEdit.this.popup_.hide();
					}
				});
		this.calendar_.activated().addListener(this,
				new Signal1.Listener<WDate>() {
					public void trigger(WDate e1) {
						WDateEdit.this.setFocus();
					}
				});
		this.calendar_.selectionChanged().addListener(this,
				new Signal.Listener() {
					public void trigger() {
						WDateEdit.this.setFromCalendar();
					}
				});
		t.bindWidget("calendar", this.calendar_);
		WApplication.getInstance().getTheme().apply(this, this.popup_,
				WidgetThemeRole.DatePickerPopupRole);
		t.escapePressed().addListener(this.popup_, new Signal.Listener() {
			public void trigger() {
				WDateEdit.this.popup_.hide();
			}
		});
		t.escapePressed().addListener(this, new Signal.Listener() {
			public void trigger() {
				WDateEdit.this.setFocus();
			}
		});
		this.setValidator(new WDateValidator("dd/MM/yyyy", this));
	}

	/**
	 * Creates a new date edit.
	 * <p>
	 * Calls {@link #WDateEdit(WContainerWidget parent)
	 * this((WContainerWidget)null)}
	 */
	public WDateEdit() {
		this((WContainerWidget) null);
	}

	/**
	 * Sets the date.
	 * <p>
	 * Does nothing if the current date is <code>Null</code>.
	 * <p>
	 * 
	 * @see WDateEdit#getDate()
	 */
	public void setDate(WDate date) {
		if (!(date == null)) {
			this.setText(date.toString(this.getFormat()));
			this.calendar_.select(date);
			this.calendar_.browseTo(date);
		}
	}

	/**
	 * Returns the date.
	 * <p>
	 * Reads the current date.
	 * <p>
	 * Returns <code>null</code> if the date could not be parsed using the
	 * current {@link WDateEdit#getFormat() getFormat()}. <br>
	 * <p>
	 * 
	 * @see WDateEdit#setDate(WDate date)
	 * @see WLineEdit#getText()
	 */
	public WDate getDate() {
		return WDate.fromString(this.getText(), this.getFormat());
	}

	/**
	 * Returns the validator.
	 * <p>
	 * Most of the configuration of the date edit is stored in the validator.
	 */
	public WDateValidator getValidator() {
		return ((super.getValidator()) instanceof WDateValidator ? (WDateValidator) (super
				.getValidator())
				: null);
	}

	/**
	 * Sets the format used for representing the date.
	 * <p>
	 * This sets the format in the validator.
	 * <p>
	 * The default format is <code>&apos;dd/MM/yyyy&apos;</code>.
	 * <p>
	 * 
	 * @see WDateValidator#setFormat(String format)
	 */
	public void setFormat(String format) {
		WDate d = this.getDate();
		this.getValidator().setFormat(format);
		this.setDate(d);
	}

	/**
	 * Returns the format.
	 * <p>
	 * 
	 * @see WDateEdit#setFormat(String format)
	 */
	public String getFormat() {
		return this.getValidator().getFormat();
	}

	/**
	 * Sets the lower limit of the valid date range.
	 * <p>
	 * This sets the lower limit of the valid date range in the validator.
	 * <p>
	 * 
	 * @see WDateValidator#setBottom(WDate bottom)
	 */
	public void setBottom(WDate bottom) {
		this.getValidator().setBottom(bottom);
		this.calendar_.setBottom(bottom);
	}

	/**
	 * Returns the lower limit of the valid date range.
	 * <p>
	 * 
	 * @see WDateEdit#setBottom(WDate bottom)
	 */
	public WDate getBottom() {
		return this.getValidator().getBottom();
	}

	/**
	 * Sets the upper limit of the valid date range.
	 * <p>
	 * This sets the upper limit of the valid date range in the validator.
	 * <p>
	 * 
	 * @see WDateValidator#setTop(WDate top)
	 */
	public void setTop(WDate top) {
		this.getValidator().setTop(top);
		this.calendar_.setTop(top);
	}

	/**
	 * Returns the upper limit of the valid range.
	 * <p>
	 * 
	 * @see WDateEdit#setTop(WDate top)
	 */
	public WDate getTop() {
		return this.getValidator().getTop();
	}

	/**
	 * Returns the calendar widget.
	 * <p>
	 * The calendar may be 0 (e.g. when using a native date entry widget).
	 */
	public WCalendar getCalendar() {
		return this.calendar_;
	}

	/**
	 * Hide/unhide the widget.
	 */
	public void setHidden(boolean hidden, WAnimation animation) {
		super.setHidden(hidden, animation);
		this.popup_.setHidden(hidden, animation);
	}

	protected void render(EnumSet<RenderFlag> flags) {
		if (!EnumUtils.mask(flags, RenderFlag.RenderFull).isEmpty()) {
			this.defineJavaScript();
			this.setTop(this.getValidator().getTop());
			this.setBottom(this.getValidator().getBottom());
			this.setFormat(this.getValidator().getFormat());
		}
		super.render(flags);
	}

	protected void propagateSetEnabled(boolean enabled) {
		super.propagateSetEnabled(enabled);
	}

	private WPopupWidget popup_;
	private WCalendar calendar_;

	private void defineJavaScript() {
		WApplication app = WApplication.getInstance();
		app.loadJavaScript("js/WDateEdit.js", wtjs1());
		String jsObj = "new Wt3_3_0.WDateEdit(" + app.getJavaScriptClass()
				+ "," + this.getJsRef() + "," + this.popup_.getJsRef() + ");";
		this.setJavaScriptMember(" WDateEdit", jsObj);
		AbstractEventSignal b = this.mouseMoved();
		AbstractEventSignal c = this.keyWentDown();
		this.connectJavaScript(this.mouseMoved(), "mouseMove");
		this.connectJavaScript(this.mouseWentUp(), "mouseUp");
		this.connectJavaScript(this.mouseWentDown(), "mouseDown");
		this.connectJavaScript(this.mouseWentOut(), "mouseOut");
	}

	private void setFromCalendar() {
		if (!this.calendar_.getSelection().isEmpty()) {
			WDate calDate = this.calendar_.getSelection().iterator().next();
			this.setText(calDate.toString(this.getFormat()));
			this.changed().trigger();
		}
	}

	private void setFromLineEdit() {
		WDate d = WDate.fromString(this.getText(), this.getFormat());
		if ((d != null)) {
			if (this.calendar_.getSelection().isEmpty()) {
				this.calendar_.select(d);
				this.calendar_.selectionChanged().trigger();
			} else {
				WDate j = this.calendar_.getSelection().iterator().next();
				if (!(j == d || (j != null && j.equals(d)))) {
					this.calendar_.select(d);
					this.calendar_.selectionChanged().trigger();
				}
			}
			this.calendar_.browseTo(d);
		}
	}

	private void connectJavaScript(AbstractEventSignal s, String methodName) {
		String jsFunction = "function(obj, event) {var o = jQuery.data("
				+ this.getJsRef() + ", 'obj');if (o) o." + methodName
				+ "(obj, event);}";
		s.addListener(jsFunction);
	}

	static WJavaScriptPreamble wtjs1() {
		return new WJavaScriptPreamble(
				JavaScriptScope.WtClassScope,
				JavaScriptObjectType.JavaScriptConstructor,
				"WDateEdit",
				"function(g,a,h){function f(){return!!a.getAttribute(\"readonly\")}function i(){var b=$(\"#\"+h.id).get(0);return jQuery.data(b,\"popup\")}function j(){c.removeClass(\"active\")}function k(){var b=i();b.bindHide(j);b.show(a,e.Vertical)}jQuery.data(a,\"obj\",this);var e=g.WT,c=$(a);this.mouseOut=function(){c.removeClass(\"hover\")};this.mouseMove=function(b,d){if(!f())if(e.widgetCoordinates(a,d).x>a.offsetWidth-40)c.addClass(\"hover\");else c.hasClass(\"hover\")&& c.removeClass(\"hover\")};this.mouseDown=function(b,d){if(!f())if(e.widgetCoordinates(a,d).x>a.offsetWidth-40){e.cancelEvent(d);c.addClass(\"unselectable\").addClass(\"active\")}};this.mouseUp=function(b,d){c.removeClass(\"unselectable\");e.widgetCoordinates(a,d).x>a.offsetWidth-40&&k()}}");
	}
}