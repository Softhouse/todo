package sample;

import com.vaadin.terminal.gwt.client.ApplicationConnection
import scala.io.Source
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import javax.annotation.Resource
import org.springframework.beans.factory.annotation.Configurable
import se.softhouse.garden.orchid.commons.text.OrchidMessage.arg

@Configurable
class LoginForm(usernameCaption: String = "login.username",
  passwordCaption: String = "login.password",
  loginButtonCaption: String = "login.button",
  action: com.vaadin.ui.LoginForm#LoginEvent => Unit = null,
  messages: OrchidLocalizedMesageSource = null) extends com.vaadin.ui.LoginForm {

  setUsernameCaption(usernameCaption)
  setPasswordCaption("login.password")
  setLoginButtonCaption("login.button")
  if (action != null) addListener(action)

  @transient
  @Resource
  val msgs: OrchidLocalizedMesageSource = messages;

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
