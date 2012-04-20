/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package sample;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

/**
 * @author Mikael Svahn
 * 
 */
public class TouchScreenApplicationServlet extends ApplicationServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void writeAjaxPageHtmlHeader(final BufferedWriter page, String title, String themeUri, HttpServletRequest request) throws IOException {
		page.write("<meta content=\"user-scalable=no, width=device-width, initial-scale=1.0, maximum-scale=1.0;\" name=\"viewport\"/>\n");
		page.write("<meta content=\"yes\" name=\"apple-touch-fullscreen\">");
		page.write("<meta content=\"yes\" name=\"apple-mobile-web-app-capable\">");
		page.write("<meta content=\"black\" name=\"apple-mobile-web-app-status-bar-style\">");

		super.writeAjaxPageHtmlHeader(page, title, themeUri, request);
	}

}
