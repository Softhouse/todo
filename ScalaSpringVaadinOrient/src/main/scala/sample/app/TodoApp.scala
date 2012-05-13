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

package sample.app;

import org.springframework.beans.factory.annotation.Configurable
import com.vaadin.Application
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import java.util.Date
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import vaadin.scala._
import com.vaadin.ui.Alignment
import com.vaadin.ui.Window.Notification
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import javax.annotation.Resource
import java.util.List
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.command.OCommandRequest
import java.util.Locale
import sample.utils.DI

/**
 * @author Mikael Svahn
 *
 */
class TodoApp extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
} with Application with DI {

  def init() {
    setTheme(msgs.get("theme"));
    setMainWindow(new Window(caption = "Todo", width = 100 percent, height = 100 percent) {
      getContent().setWidth("100%")
      getContent().setHeight("100%")
      add(new StartView(width = 100 percent, height = 100 percent, style = "micke"))
    })
  }

}
