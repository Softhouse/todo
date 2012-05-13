/**
 * Copyright (c) 2012, Mikael Svahn, Softhouse Consulting AB
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

import sample.utils._
import sample.core._
import sample.core.DomainConversions._

/**
 * @author Mikael Svahn
 *
 */
class StartView(width: String = 100 percent, height: String = null, margin: Boolean = false, spacing: Boolean = false, caption: String = null, style: String = null, size: Tuple2[String, String] = null) extends {
  @transient @Resource val userSession: UserSession = null
  @transient @Resource val userManager: UserManager = null
} with VerticalLayout(width, height, margin, spacing, caption, style, size) with DI {

  add(createLoginPanel())

  def createLoginPanel() = {
    new HorizontalLayout(width = 100 percent) {
      add(new Panel(caption = MSG.LOGIN_TITLE, width = 200 px) {
        add(new LoginForm(action = event => onLogin(event)) { setDebugId("loginForm") })
        add(new Label(height = 10 px))
        add(new LinkButton(caption = MSG.LOGIN_REGISTER, action = _ => onRegister()))
      }, alignment = Alignment.MIDDLE_CENTER)
    }
  }

  def onLogin(event: com.vaadin.ui.LoginForm#LoginEvent) {
    if (userSession.onLogin(event.getLoginParameter("username"), event.getLoginParameter("password"))) {
      removeAllComponents();
      add(new MainView().build())
    }
  }

  def showLogin() {
    removeAllComponents()
    addComponent(createLoginPanel())
  }

  def onRegister() {
    removeAllComponents()
    addComponent(new HorizontalLayout(100 percent) {
      add(new Panel(caption = MSG.REGISTER_TITLE, width = 200 px) {
        add(new FormLayout() {
          add(new Label(MSG.REGISTER_USERNAME))
          val usernameField = add(new TextField())
          add(new Label(MSG.REGISTER_PASSWORD))
          val passwordField = add(new TextField())
          add(new HorizontalLayout() {
            add(new Button(caption = MSG.REGISTER_BUTTON, action = _ => doRegister(usernameField, passwordField)))
            add(new Button(caption = MSG.REGISTER_CANCEL, action = _ => showLogin()))
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
      getWindow().showNotification(MSG.REGISTER_FAIL_TITLE, Notification.TYPE_ERROR_MESSAGE)
    }
  }
}
