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

package sample
import com.vaadin.ui.VerticalLayout
import org.springframework.beans.factory.annotation.Configurable
import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import org.tepi.filtertable.FilterTable
import vaadin.scala._
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
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.orientechnologies.orient.core.id.ORID

/**
 * @author mis
 *
 */
@Configurable
class MainView extends VerticalLayout {

  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
  @transient @Resource val todoMgr: TodoManager = null

  var cont: IndexedContainer = null

  def build(): MainView = {
    setSizeFull()
    addComponent(new MenuBar(style = "toolbar", width = 100 percent) {
      addItem("logout", new ThemeResource("../runo/icons/32/cancel.png"), logout()).setStyleName("last")
      addItem("new", new ThemeResource("../runo/icons/32/document-add.png"), createTodo())
    })
    addComponent(new Label(height = 10 px))
    val table = buildFilterTable()
    addComponent(table)
    setExpandRatio(table, 1.0f)
    this
  }

  def buildFilterTable(): FilterTable = {
    new FilterTable() {
      setSizeFull()
      setContainerDataSource(buildContainer())
      setFiltersVisible(true)
      setColumnReorderingAllowed(true)
      setSelectable(true)
      addStyleName("striped")
      addListener(onSelect())
    }
  }

  def buildContainer(): Container = {
    cont = new IndexedContainer()
    val c = Calendar.getInstance()

    cont.addContainerProperty("title", classOf[String], null)
    cont.addContainerProperty("date", classOf[Date], null)
    cont.addContainerProperty("description", classOf[String], null)
    cont.addContainerProperty("done", classOf[java.lang.Boolean], null)

    val todos = todoMgr.list()
    todos.foreach(todo => addTodoToContainer(todo))

    return cont;
  }

  def addTodoToContainer(todo: ODocument) {
    var item = cont.addItem(todo.getIdentity())
    if (item == null) item = cont.getItem(todo.getIdentity())
    item.getItemProperty("title").setValue(todo.field("title"))
    item.getItemProperty("date").setValue(todo.field("date"))
    item.getItemProperty("description").setValue(todo.field("description"))
    item.getItemProperty("done").setValue(todo.field("completed"))
  }

  def createTodo(): Command = {
    new Command() {
      def menuSelected(selectedItem: com.vaadin.ui.MenuBar#MenuItem) {
        openTodoEditor("", new Date(), "", false, doCreateTodo)
      }
    }
  }

  def openTodoEditor(title: String, date: Date, description: String, done: Boolean, action: (String, Date, String, Boolean, com.vaadin.ui.Window) => Unit) {
    getWindow().addWindow(new Window(caption = "Create TODO Item", modal = true) {
      setResizable(false)
      setClosable(false)
      getLayout().setSizeUndefined();
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
        add(new Button(caption = "Add", action = _ => action(titleField.getValue().asInstanceOf[String], dateField.getValue().asInstanceOf[Date], descrField.getValue().asInstanceOf[String], doneField.getValue().asInstanceOf[Boolean], getWindow())))
        add(new Button(caption = "Cancel", action = _ => getWindow().getParent().removeWindow(getWindow())))
      })
    })
  }

  def doCreateTodo(title: String, date: Date, description: String, done: Boolean, window: com.vaadin.ui.Window) {
    addTodoToContainer(todoMgr.create(title, date, description, done))
    getWindow().removeWindow(window)
  }

  def doUpdateTodo(todo: ODocument, title: String, date: Date, description: String, done: Boolean, window: com.vaadin.ui.Window) {
    addTodoToContainer(todoMgr.update(todo, title, date, description, done))
    getWindow().removeWindow(window)
  }

  def logout(): Command = {
    new Command() {
      def menuSelected(selectedItem: com.vaadin.ui.MenuBar#MenuItem) {
        getApplication().close()
      }
    }
  }

  def onSelect(): com.vaadin.event.ItemClickEvent.ItemClickListener = {
    new ItemClickListener() {
      override def itemClick(event: com.vaadin.event.ItemClickEvent) {
        if (event.isDoubleClick()) {
          var todo = todoMgr.load(event.getItemId().asInstanceOf[ORID]);
          openTodoEditor(todo.field("title"), todo.field("date"), todo.field("description"), todo.field("completed"), doUpdateTodo(todo, _: String, _: Date, _: String, _: Boolean, _: com.vaadin.ui.Window))
        }
      }
    }
  }
}