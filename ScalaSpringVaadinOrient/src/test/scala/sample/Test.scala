package sample
import java.util.Date
import com.orientechnologies.orient.core.record.impl.ODocument
import sample.DomainConversions._

object Test extends App {

  val pool = new DatabasePool()
  pool.postConstruct()
  val db = pool.get

  val todo = new Todo(title = "Test", date = new Date(), description = "bla", completed = false)

  val doc: ODocument = todo

  val todo2: Todo = doc

  val doc2: ODocument = todo2;

  println(todo.title)
  println(doc.field("title"))
  println(todo2.title)
  println(todo2.description)
  println(doc2.field("title"))
  println(doc2.field("description"))
}