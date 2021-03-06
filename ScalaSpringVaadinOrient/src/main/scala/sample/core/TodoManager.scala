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

package sample.core
import org.springframework.stereotype.Component
import javax.annotation.Resource
import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.record.impl.ODocument
import java.util.Date
import java.util.List
import java.util.ArrayList
import com.orientechnologies.orient.core.id.ORID
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import scala.collection.mutable.MutableList
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import DomainConversions._
import javax.annotation.PostConstruct

/**
 * @author Mikael Svahn
 *
 */
@Component
class TodoManager {

  @transient @Resource val dbpool: DatabasePool = null

  private var SCHEMA_NAME = "Todo"

  @PostConstruct
  def init() {
    dbpool.execute(db =>
      if (db.getMetadata.getSchema.getClass(SCHEMA_NAME) == null)
        db.getMetadata.getSchema.createClass(SCHEMA_NAME))
  }

  def list(userId: ORID): List[Todo] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from " + SCHEMA_NAME + " where user = ?"))
      for (doc <- (cmd.execute(userId): java.util.List[ODocument])) yield (doc: Todo)
    })
  }

  def load(id: ORID): Todo = dbpool.execute(db => (db.load(id): ODocument))

  def save(todo: Todo): Todo = dbpool.execute(_ => toTodo((toDoc(todo).save())))
}