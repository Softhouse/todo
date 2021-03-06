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

import java.util.Calendar
import java.util.Date
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import org.springframework.beans.factory.annotation.Configurable
import org.tepi.filtertable.FilterTable
import com.github.wolfie.refresher.Refresher
import com.orientechnologies.orient.core.id.ORID
import com.orientechnologies.orient.core.record.impl.ODocument
import com.vaadin.data.util.IndexedContainer
import com.vaadin.data.Container
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.MenuBar.Command
import javax.annotation.Resource
import sample.core._
import sample.core.DomainConversions._
import sample.utils.DI
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import vaadin.scala._
import com.vaadin.ui.CustomTable
import com.vaadin.ui.CustomTable.ColumnGenerator
import sample.utils.SmartTable

/**
 * @author Mikael Svahn
 *
 */
class MainView extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
  @transient @Resource val todoMgr: TodoManager = null
  @transient @Resource val timerService: TimerService = null
  @transient @Resource val userSession: UserSession = null
} with com.vaadin.ui.VerticalLayout with DI {

  var cont: IndexedContainer = null

  def build(): MainView = {
    setSizeFull()
    addComponent(new Refresher() {
      setRefreshInterval(1000)
    })
    addComponent(new MenuBar(style = "toolbar", width = 100 percent) {
      addItem(MSG.MENU_LOGOUT, new ThemeResource("../runo/icons/32/cancel.png"), new MenuBarCommand(_ => getApplication().close())).setStyleName("last")
      addItem(MSG.MENU_NEW, new ThemeResource("../runo/icons/32/document-add.png"), createTodo())
    })
    addComponent(new Label(height = 10 px))
    val table = buildFilterTable()
    addComponent(table)
    setExpandRatio(table, 1.0f)
    this
  }

  def buildFilterTable(): FilterTable = {
    new FilterTable() with SmartTable with ItemClickListener {
      setSizeFull()
      setContainerDataSource(buildContainer())
      setFiltersVisible(true)
      setColumnReorderingAllowed(true)
      setSelectable(true)
      setColumnHeaders(Array(MSG.TODO_EXPIRED, MSG.TODO_TITLE, MSG.TODO_DATE, MSG.TODO_DESCRIPTION, MSG.TODO_DONE))
      addStyleName("striped")
      addGeneratedColumn("expired", v => { new CheckBox(checked = "Yes" == v) { setReadOnly(true) } })
      addGeneratedColumn("date", v => MSG.TODO_VALUE_DATE(value = v.asInstanceOf[Date]))
      addGeneratedColumn("done", v => { new CheckBox(checked = v.asInstanceOf[Boolean]) { setReadOnly(true) } })
      addItemClickListener(event => {
        if (event.isDoubleClick()) {
          var todo = todoMgr.load(event.getItemId().asInstanceOf[ORID]);
          openTodoEditor(todo, doUpdateTodo(_: Todo, _: com.vaadin.ui.Window))
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
        openTodoEditor(new Todo(), doCreateTodo)
      }
    }
  }

  def openTodoEditor(todo: Todo, action: (Todo, com.vaadin.ui.Window) => Unit) {
    getWindow().addWindow(new Window(caption = "Create TODO Item", modal = false) {
      setResizable(false)
      setClosable(false)
      getContent().setSizeUndefined()
      add(new Label(MSG.TODO_TITLE))
      val titleField = add(new TextField(value = todo.title))
      add(new Label(MSG.TODO_DATE))
      val dateField = add(new PopupDateField(value = todo.date))
      add(new Label(MSG.TODO_DESCRIPTION))
      val descrField = add(new TextArea(value = todo.description))
      add(new Label(MSG.TODO_DONE))
      val doneField = add(new CheckBox(checked = todo.completed))
      add(new Label(height = 10 px))
      add(new HorizontalLayout(width = 100 percent) {
        add(new Button(caption = MSG.TODO_ADD, action = _ => action(todo.copy(title = titleField.getValue().asInstanceOf[String], date = dateField.getValue().asInstanceOf[Date], description = descrField.getValue().asInstanceOf[String], completed = doneField.getValue().asInstanceOf[Boolean]), getWindow())))
        add(new Button(caption = MSG.TODO_CANCEL, action = _ => getWindow().getParent().removeWindow(getWindow())))
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
    getWindow().showNotification(todo.field("title").asInstanceOf[String])
  }
}
