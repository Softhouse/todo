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

import com.orientechnologies.orient.core.record.impl.ODocument
import java.util.Date
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.id.ORID
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }

/**
 * @author Mikael Svahn
 *
 */

class DomainObject(var className: String, val orid: ORID)
class User(orid: ORID = null, var username: String = null, var password: String = null) extends DomainObject("User", orid)
class Todo(orid: ORID = null, var title: String = null, var date: Date = null, var description: String = null, var completed: Boolean = false, var userId: ORID = null) extends DomainObject("Todo", orid)

object DomainConversions {

  implicit def doc2Todo(from: ODocument): Todo = fromDoc(from, new Todo(from.getIdentity()))
  implicit def doc2User(from: ODocument): User = fromDoc(from, new User(from.getIdentity()))

  def fromDoc[T](from: ODocument, to: T): T = {
    val fields = to.getClass().getDeclaredFields()
    val methods = to.getClass().getMethods()
    fields.foreach(f => {
      val method = to.getClass().getMethod(f.getName() + "_$eq", f.getType())
      if (method != null) method.invoke(to, from.field(f.getName()))
    })
    to
  }

  implicit def toDoc(from: DomainObject): ODocument = {
    val doc = if (from.orid == null) new ODocument(from.className) else new ODocument(from.className, from.orid)
    val fields = from.getClass().getDeclaredFields()
    fields.foreach(f => {
      val method = from.getClass().getMethod(f.getName())
      if (method != null) doc.field(f.getName(), method.invoke(from))
    })
    doc
  }

}
