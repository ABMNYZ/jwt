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

class WMediaPlayerImpl extends WTemplate {
	public WMediaPlayerImpl(WMediaPlayer player, CharSequence text) {
		super(text);
		this.player_ = player;
		this.setFormObject(true);
	}

	String renderRemoveJs() {
		if (this.isRendered()) {
			return this.player_.getJsPlayerRef()
					+ ".jPlayer('destroy');Wt3_1_11.remove('" + this.getId()
					+ "');";
		} else {
			return super.renderRemoveJs();
		}
	}

	void setFormData(WObject.FormData formData) {
		this.player_.setFormData(formData);
	}

	private WMediaPlayer player_;
}