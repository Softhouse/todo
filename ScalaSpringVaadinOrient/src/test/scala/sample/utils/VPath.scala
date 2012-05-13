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
package sample.utils

import com.vaadin.ui._
import scalaj.collection.Imports._
import scala.annotation.tailrec
import java.util.ArrayList
import com.vaadin.data.Property

/**
 * @author Mikael Svahn
 *
 */
object VPath {

  implicit def componentToVPath[T](c: Component) = new VPath(VComponent(c))
  implicit def applicationToVPath[T](c: com.vaadin.Application) = new VPath(VComponent(c.getMainWindow().getContent()))
  implicit def vPathComponentToComponent[T](c: VPath): T = if (c.component != null) c.component.content.asInstanceOf[T] else throw new NoSuchElementException("Component not found")

  implicit def stringToDebugId(s: String) = DebugId(s)

  def DebugId(s: String) = new VPathCondition(DEBUGID, s)
  def Caption(s: String) = new VPathCondition(CAPTION, s)
  def Style(s: String) = new VPathCondition(STYLE, s)
  def Value(s: String) = new VPathCondition(VALUE, s)

  val DEBUGID = "debugId"
  val CAPTION = "caption"
  val STYLE = "style"
  val VALUE = "value"
}

class VPath(protected val component: VComponent[Any]) {

  def \(x: Int, y: Int = 0) = {
    component.at(x, y);
  }

  def \(condition: VPathCondition): VPath = {
    val vuc = find(component, createFilter(condition), false, condition.index)
    if (vuc != null) vuc else new VPath(null)
  }

  def \\(condition: VPathCondition): VPath = {
    val vuc = find(component, createFilter(condition), true, condition.index)
    if (vuc != null) vuc else new VPath(null)
  }

  def !(event: VEvent.VEventBase): VPath = {
    if (this.component == null) {
      throw new NoSuchElementException("Component not found")
    }
    event.fire(this.component)
    this
  }

  private def find(comp: VComponent[Any], filter: VComponent[Any] => Boolean, recursive: Boolean, index: Int): VPath = {
    val iter = comp.componentIterator
    while (iter.hasNext) {
      var v = VComponent(iter.next)
      if (filter(v)) {
        for (i <- 0 until index) v = VComponent(iter.next)
        return new VPath(v)
      } else if (recursive) {
        val vuc = find(v, filter, true, index)
        if (vuc != null) return vuc
      }
    }
    null
  }

  private def createFilter(condition: VPathCondition): VComponent[Any] => Boolean = {
    condition.identifier match {
      case VPath.DEBUGID => c => c.debugId != null && c.debugId.endsWith(condition.value)
      case VPath.CAPTION => c => condition.value == c.caption
      case VPath.STYLE => c => c.styles.split(" ").find(condition.value.equals(_)).isDefined
      case VPath.VALUE => c => c.content match {
        case p: Property => condition.value == p.getValue()
        case _ => false
      }
      case _ => c => false
    }
  }

}

class VPathCondition(val identifier: String, val value: String) {
  var index = 0
  def +(idx: Int): VPathCondition = { index = idx; this; }
}

object VEvent {

  trait VEventBase {
    def fire(component: VComponent[Any])
  }

  case class VClickEvent extends VEventBase with VEventUtils {
    def fire(component: VComponent[Any]) {
      component.content match {
        case button: Button => fire(button, new button.ClickEvent(button))
        case menu: MenuBar#MenuItem => menu.getCommand.menuSelected(menu)
      }
    }
  }

  case class VLoginEvent(username: String, password: String) extends VEventBase with VEventUtils {
    def fire(component: VComponent[Any]) {
      fire(component.content.asInstanceOf[LoginForm], VEvent.loginEventConstructor.newInstance(component.content.asInstanceOf[AnyRef], Map("username" -> username, "password" -> password) asJava))
    }
  }

  protected trait VEventUtils {
    protected def fire(component: Component, event: Component.Event) {
      VEvent.fireEventMethod.invoke(component, event)
    }
  }

  private val fireEventMethod = classOf[com.vaadin.ui.AbstractComponent].getDeclaredMethod("fireEvent", classOf[Component.Event])
  fireEventMethod.setAccessible(true)
  private val loginEventConstructor = classOf[com.vaadin.ui.LoginForm#LoginEvent].getDeclaredConstructor(classOf[com.vaadin.ui.LoginForm], classOf[java.util.Map[String, String]])
  loginEventConstructor.setAccessible(true)
}

object VComponent {
  def apply(a: Any): VComponent[Any] = {
    a match {
      case c: Component => new VComponentComponent(c)
      case c: MenuBar#MenuItem => new VComponentMenuItem(c)
    }
  }
}

trait VComponent[+T] {
  def content: T
  def debugId: String
  def styles: String
  def caption: String
  def at(x: Int, y: Int): VComponent[Any]
  def componentIterator: java.util.Iterator[_ <: Any]
}

class VComponentComponent(c: Component) extends VComponent[Component] {
  def content = c
  def debugId = c.getDebugId()
  def styles = c.getStyleName()
  def caption = c.getCaption()
  def at(x: Int, y: Int = 0): VComponent[Any] = {
    c match {
      case c: GridLayout => VComponent(c.getComponent(x, y), true)
      case c: AbstractOrderedLayout => VComponent(c.getComponent(x), true)
      case c: MenuBar => VComponent(c.getItems().get(x), false)
      case _ => throw new IllegalArgumentException("Component do not contain other components")
    }
  }
  def componentIterator: java.util.Iterator[_ <: Any] = {
    c match {
      case c: Panel => c.getComponentIterator()
      case c: GridLayout => c.getComponentIterator()
      case c: AbstractOrderedLayout => c.getComponentIterator()
      case c: MenuBar => c.getItems().iterator()
      case _ => new ArrayList().iterator()
    }
  }
}

class VComponentMenuItem(m: MenuBar#MenuItem) extends VComponent[MenuBar#MenuItem] {
  def content = m
  def debugId = null
  def styles = m.getStyleName()
  def caption = m.getText()
  def at(x: Int, y: Int = 0) = VComponent(m.getChildren().get(x))
  def componentIterator: java.util.Iterator[_ <: Any] = m.getChildren().iterator()
}

