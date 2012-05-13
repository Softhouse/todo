package sample.app;

import com.vaadin.terminal.gwt.client.ApplicationConnection
import scala.io.Source
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import javax.annotation.Resource
import org.springframework.beans.factory.annotation.Configurable
import se.softhouse.garden.orchid.commons.text.OrchidMessage.arg
import sample.utils.DI

class LoginForm(usernameCaption: String = MSG.LOGIN_USERNAME,
  passwordCaption: String = MSG.LOGIN_PASSWORD,
  loginButtonCaption: String = MSG.LOGIN_BUTTON,
  action: com.vaadin.ui.LoginForm#LoginEvent => Unit = null,
  messages: OrchidLocalizedMesageSource = null) extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = messages;
} with com.vaadin.ui.LoginForm with DI {

  setUsernameCaption(usernameCaption)
  setPasswordCaption(MSG.LOGIN_PASSWORD.key)
  setLoginButtonCaption(MSG.LOGIN_BUTTON.key)
  if (action != null) addListener(action)

  override def getLoginHTML() = {
    val appUri = getApplication().getURL().toString() + getWindow().getName() + "/"
    val loginHtml = Source.fromURL(getClass().getResource("/sample/login.html")).mkString
    val format: OrchidMessageFormat = new OrchidMessageFormat(loginHtml, msgs.getLocale())
    format.format(arg("username", msgs.get(getUsernameCaption()))
      .arg("password", msgs.get(getPasswordCaption()))
      .arg("button", msgs.get(getLoginButtonCaption()))
      .arg("appUri", appUri)).getBytes()
  }

  def addListener(action: com.vaadin.ui.LoginForm#LoginEvent => Unit): Unit = addListener(new LoginListener(action))

}

class LoginListener(action: com.vaadin.ui.LoginForm#LoginEvent => Unit) extends com.vaadin.ui.LoginForm.LoginListener {
  def onLogin(event: com.vaadin.ui.LoginForm#LoginEvent) = action(event)
}
