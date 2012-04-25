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
import org.springframework.stereotype.Component
import javax.annotation.Resource
import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.record.impl.ODocument
import java.util.Date
import java.util.List
import java.util.ArrayList
import com.orientechnologies.orient.core.id.ORID
import sample.DomainConversions._
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import scala.collection.mutable.MutableList
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx

/**
 * @author Mikael Svahn
 *
 */
@Component
class TodoManager {

  @transient @Resource val dbpool: DatabasePool = null

  def save(todo: Todo): Todo = {
    dbpool.execute(_ => (todo: ODocument).save())
  }

  def list(userId: ORID): List[Todo] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from Todo where user = ?"))
      val todos = MutableList.empty[Todo]
      (cmd.execute(userId): java.util.List[ODocument]).foreach(d => todos += d)
      return todos
    })
  }

  def load(id: ORID): Todo = {
    dbpool.execute(db => (db.load(id): ODocument))
  }
}