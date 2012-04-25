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

package sample

import org.springframework.beans.factory.annotation.Configurable
import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import org.tepi.filtertable.FilterTable
import com.vaadin.data.util.IndexedContainer
import com.vaadin.data.Container
import java.util.Calendar
import java.util.Date
import java.util.Random
import com.vaadin.ui.PopupView
import com.vaadin.ui.MenuBar.Command
import com.vaadin.terminal.ThemeResource
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.id.ORID
import com.github.wolfie.refresher.Refresher
import scala.compat.Platform
import vaadin.scala._
import sample.DomainConversions._

/**
 * @author Mikael Svahn
 *
 */
@Configurable
class MainView extends com.vaadin.ui.VerticalLayout {

  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
  @transient @Resource val todoMgr: TodoManager = null
  @transient @Resource val timerService: TimerService = null
  @transient @Resource val userSession: UserSession = null

  var cont: IndexedContainer = null

  def build(): MainView = {
    setSizeFull()
    addComponent(new Refresher() {
      setRefreshInterval(1000)
    })
    addComponent(new MenuBar(style = "toolbar", width = 100 percent) {
      addItem("logout", new ThemeResource("../runo/icons/32/cancel.png"), new MenuBarCommand(_ => getApplication().close())).setStyleName("last")
      addItem("new", new ThemeResource("../runo/icons/32/document-add.png"), createTodo())
    })
    addComponent(new Label(height = 10 px))
    val table = buildFilterTable()
    addComponent(table)
    setExpandRatio(table, 1.0f)
    this
  }

  def buildFilterTable(): FilterTable = {
    new FilterTable() with ItemClickListener {
      setSizeFull()
      setContainerDataSource(buildContainer())
      setFiltersVisible(true)
      setColumnReorderingAllowed(true)
      setSelectable(true)
      addStyleName("striped")
      addItemClickListener(event => {
        if (event.isDoubleClick()) {
          var todo = todoMgr.load(event.getItemId().asInstanceOf[ORID]);
          openTodoEditor(todo.title, todo.date, todo.description, todo.completed, doUpdateTodo(_: Todo, _: com.vaadin.ui.Window))
        }
      })
    }
  }

  def buildContainer(): Container = {
    cont = new IndexedContainer()
    val c = Calendar.getInstance()

    cont.addContainerProperty("expired", classOf[String], null)
    cont.addContainerProperty("title", classOf[String], null)
    cont.addContainerProperty("date", classOf[Date], null)
    cont.addContainerProperty("description", classOf[String], null)
    cont.addContainerProperty("done", classOf[java.lang.Boolean], null)

    return cont;
  }

  override def attach() {
    val todos = todoMgr.list(userSession.user.orid)
    todos.foreach(todo => addTodoToContainer(todo))
    super.attach()
  }

  def addTodoToContainer(todo: Todo) {
    var item = cont.addItem(todo.orid)
    if (item == null) item = cont.getItem(todo.orid)
    val now = new Date()
    item.getItemProperty("expired").setValue(if (todo.date.compareTo(now) <= 0) "Yes" else "No")
    item.getItemProperty("title").setValue(todo.title)
    item.getItemProperty("date").setValue(todo.date)
    item.getItemProperty("description").setValue(todo.description)
    item.getItemProperty("done").setValue(todo.completed)
    if (todo.completed) {
      timerService.add(todo, todo.date, p => onTimeout(p))
    }
  }

  def createTodo(): Command = {
    new Command() {
      def menuSelected(selectedItem: com.vaadin.ui.MenuBar#MenuItem) {
        openTodoEditor("", new Date(), "", false, doCreateTodo)
      }
    }
  }

  def openTodoEditor(title: String, date: Date, description: String, done: Boolean, action: (Todo, com.vaadin.ui.Window) => Unit) {
    getWindow().addWindow(new Window(caption = "Create TODO Item", modal = false) {
      setResizable(false)
      setClosable(false)
      getLayout().setSizeUndefined()
      add(new Label(msgs.get("todo.title")))
      val titleField = add(new TextField(value = title))
      add(new Label(msgs.get("todo.date")))
      val dateField = add(new PopupDateField(value = date))
      add(new Label(msgs.get("todo.description")))
      val descrField = add(new TextArea(value = description))
      add(new Label(msgs.get("todo.done")))
      val doneField = add(new CheckBox(checked = done))
      add(new Label(height = 10 px))
      add(new HorizontalLayout(width = 100 percent) {
        add(new Button(caption = "Add", action = _ => action(new Todo(title = titleField.getValue().asInstanceOf[String], date = dateField.getValue().asInstanceOf[Date], description = descrField.getValue().asInstanceOf[String], completed = doneField.getValue().asInstanceOf[Boolean]), getWindow())))
        add(new Button(caption = "Cancel", action = _ => getWindow().getParent().removeWindow(getWindow())))
      })
    })
  }

  def doCreateTodo(todo: Todo, window: com.vaadin.ui.Window) {
    addTodoToContainer(todoMgr.save(todo))
    getWindow().removeWindow(window)
  }

  def doUpdateTodo(todo: Todo, window: com.vaadin.ui.Window) {
    todoMgr.save(todo)
    getWindow().removeWindow(window)
    timerService.remove(i => i.asInstanceOf[Todo].orid.equals(todo.orid))
    addTodoToContainer(todo)
  }

  def onTimeout(o: Any) {
    val todo = o.asInstanceOf[ODocument]
    println("Timeoout: " + todo.field("title").asInstanceOf[String])
    getWindow().showNotification(todo.field("title").asInstanceOf[String])
  }
}
