/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
package eu.webtoolkit.jwt;


class SizeHandle {
	public static void loadJavaScript(WApplication app) {
		String THIS_JS = "js/SizeHandle.js";
		if (!app.isJavaScriptLoaded(THIS_JS)) {
			app.doJavaScript(wtjs1(app), false);
			app.setJavaScriptLoaded(THIS_JS);
		}
	}

	static String wtjs1(WApplication app) {
		return "Wt3_1_1.SizeHandle = function(d,g,j,k,n,o,p,q,e,l,h,i){function m(b){b=d.pageCoordinates(b);return Math.min(Math.max(g==\"h\"?b.x-f.x-c.x:b.y-f.y-c.y,n),o)}var a=document.createElement(\"div\");a.style.position=\"absolute\";a.style.zIndex=\"100\";if(g==\"v\"){a.style.width=k+\"px\";a.style.height=j+\"px\"}else{a.style.height=k+\"px\";a.style.width=j+\"px\"}var f=d.widgetCoordinates(e,l),c=d.widgetPageCoordinates(e);h-=d.px(e,\"marginLeft\");i-=d.px(e,\"marginTop\");c.x+=h;c.y+=i;f.x-=h;f.y-=i;a.style.left= c.x+\"px\";a.style.top=c.y+\"px\";a.className=p;document.body.appendChild(a);d.capture(a);d.cancelEvent(l);a.onmousemove=function(b){b=m(b);if(g==\"h\")a.style.left=c.x+b+\"px\";else a.style.top=c.y+b+\"px\"};a.onmouseup=function(b){if(a.parentNode!=null){a.parentNode.removeChild(a);q(m(b))}}};";
	}
}
