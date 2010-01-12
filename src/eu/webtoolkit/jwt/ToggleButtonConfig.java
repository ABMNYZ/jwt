/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
package eu.webtoolkit.jwt;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

class ToggleButtonConfig {
	public ToggleButtonConfig(WWidget parent) {
		this.states_ = new ArrayList<String>();
		this.toggleJS_ = null;
		this.toggleJS_ = new JSlot(parent);
	}

	public void addState(String className) {
		this.states_.add(className);
	}

	public void generate() {
		WApplication app = WApplication.getInstance();
		StringWriter js = new StringWriter();
		js.append("function(s, e) {var states = new Array(");
		for (int i = 0; i < this.states_.size(); ++i) {
			if (i != 0) {
				js.append(',');
			}
			js.append('\'').append(this.states_.get(i)).append('\'');
		}
		js.append("), i, il;for (i=0; i<").append(
				String.valueOf(this.states_.size())).append(
				"; ++i) {if (s.className == states[i]) {").append(
				app.getJavaScriptClass()).append(
				".emit(s, 't-'+s.className);s.className = states[(i+1) % ")
				.append(String.valueOf(this.states_.size())).append(
						"];break;}}}");
		this.toggleJS_.setJavaScript(js.toString());
	}

	public List<String> getStates() {
		return this.states_;
	}

	private List<String> states_;
	JSlot toggleJS_;
}