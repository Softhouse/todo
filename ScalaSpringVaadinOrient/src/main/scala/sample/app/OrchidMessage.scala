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

package sample.app
import org.springframework.context.MessageSourceResolvable
import se.softhouse.garden.orchid.spring.text.OrchidMessageSourceBuilder
import se.softhouse.garden.orchid.commons.text.OrchidDefaultMessageCode
import org.springframework.beans.factory.annotation.Configurable
import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import sample.utils.DI
import se.softhouse.garden.orchid.spring.text.OrchidMessageSource
import java.util.Date

/**
 * @author Mikael Svahn
 *
 */
case class OrchidMessage(val key: String) {
  override def toString() = OrchidMessage.format(key, this.getClass().getDeclaredFields() map { f => f.setAccessible(true); (f.getName(), f.get(this)) }: _*)
}

object OrchidMessage extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
} with DI {
  implicit def OrchidMessageToString(m: OrchidMessage): String = {
    msgs.get(m.key)
  }

  def format(code: String, args: Tuple2[String, Any]*): String = {
    val msg = OrchidMessageSource.code(code)
    args.foreach(t => msg.arg(t._1, t._2))
    msgs.get(msg)
  }
}

object MSG {
  val WINDOW_TITLE = OrchidMessage("window.title")

  val LOGIN_REGISTER = OrchidMessage("login.register")
  val LOGIN_TITLE = OrchidMessage("login.title")
  val LOGIN_USERNAME = OrchidMessage("login.username")
  val LOGIN_PASSWORD = OrchidMessage("login.password")
  val LOGIN_BUTTON = OrchidMessage("login.button")

  val REGISTER_TITLE = OrchidMessage("register.title")
  val REGISTER_USERNAME = OrchidMessage("register.username")
  val REGISTER_PASSWORD = OrchidMessage("register.password")
  val REGISTER_BUTTON = OrchidMessage("register.button")
  val REGISTER_CANCEL = OrchidMessage("register.cancel")
  val REGISTER_FAIL_TITLE = OrchidMessage("register.fail.title")

  val MENU_NEW = OrchidMessage("menu.new")
  val MENU_LOGOUT = OrchidMessage("menu.logout")

  val TODO_EXPIRED = OrchidMessage("todo.expired")
  val TODO_TITLE = OrchidMessage("todo.title")
  val TODO_DATE = OrchidMessage("todo.date")
  val TODO_DESCRIPTION = OrchidMessage("todo.description")
  val TODO_DONE = OrchidMessage("todo.done")
  val TODO_ADD = OrchidMessage("todo.add")
  val TODO_CANCEL = OrchidMessage("todo.cancel")

  case class TODO_VALUE_DATE(value: Date) extends OrchidMessage("todo.value.date")
}

