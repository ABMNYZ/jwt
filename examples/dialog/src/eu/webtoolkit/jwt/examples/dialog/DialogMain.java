/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
package eu.webtoolkit.jwt.examples.dialog;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class DialogMain extends WtServlet {
    private static final long serialVersionUID = 1L;

    public DialogMain() {
        super();
    }

    public WApplication createApplication(WEnvironment env) {
        return new DialogApplication(env);
    }
}
