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

/**
 * @author Mikael Svahn
 *
 */
@Configurable
class TodoApp extends Application {

  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
  @transient @Resource val userSession: UserSession = null
  @transient @Resource val userManager: UserManager = null

  var root: VerticalLayout = null

  def init() {
    setTheme(msgs.get("theme"));
    setMainWindow(new Window(caption = msgs.get("window.title"), width = 100 percent, height = 100 percent) {
      getLayout().setWidth("100%")
      getLayout().setHeight("100%")
      root = add(new VerticalLayout(width = 100 percent, height = 100 percent, style = "micke") {
        add(createLoginPanel())
      })
    })
  }

  def createLoginPanel() = {
    new HorizontalLayout(width = 100 percent) {
      add(new Panel(caption = msgs.get("login.title"), width = 200 px) {
        add(new LoginForm(action = event => onLogin(event)))
        add(new Label(height = 10 px))
        add(new LinkButton(caption = msgs.get("login.register"), action = _ => onRegister()))
      }, alignment = Alignment.MIDDLE_CENTER)
    }
  }

  def onLogin(event: com.vaadin.ui.LoginForm#LoginEvent) {
    if (userSession.onLogin(event.getLoginParameter("username"), event.getLoginParameter("password"))) {
      root.removeAllComponents();
      root.addComponent(new MainView().build())
    }
  }

  def showLogin() {
    root.removeAllComponents()
    root.addComponent(createLoginPanel())
  }

  def onRegister() {
    root.removeAllComponents()
    root.addComponent(new HorizontalLayout(100 percent) {
      add(new Panel(caption = msgs.get("register.title"), width = 200 px) {
        add(new FormLayout() {
          add(new Label(msgs.get("register.username")))
          val usernameField = add(new TextField())
          add(new Label(msgs.get("register.password")))
          val passwordField = add(new TextField())
          add(new HorizontalLayout() {
            add(new Button(caption = msgs.get("register.button"), action = _ => doRegister(usernameField, passwordField)))
            add(new Button(caption = msgs.get("register.cancel"), action = _ => showLogin()))
          })
        })
      }, alignment = Alignment.MIDDLE_CENTER)
    })
  }

  def doRegister(usernameField: TextField, passwordField: TextField) {
    val username = usernameField.getValue().asInstanceOf[String]
    val password = passwordField.getValue().asInstanceOf[String]

    if (userManager.doRegister(username, password)) {
      showLogin()
    } else {
      root.getWindow().showNotification(msgs.get("register.fail.title"), Notification.TYPE_ERROR_MESSAGE)
    }
  }
}
