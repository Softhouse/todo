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
import org.springframework.stereotype.Component
import javax.annotation.Resource
import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.record.impl.ODocument
import java.util.List
import java.util.Date
import com.orientechnologies.orient.core.id.ORID

/**
 * @author Mikael Svahn
 *
 */
@Component
class TodoManager {

  @transient @Resource val dbpool: DatabasePool = null
  @transient @Resource val userSession: UserSession = null

  def create(title: String, date: Date, description: String, done: Boolean): ODocument = {
    val db = dbpool.get
    try {
      val doc = db.newInstance("Todo")
      doc.field("title", title)
      doc.field("date", date)
      doc.field("description", description)
      doc.field("completed", done)
      doc.field("user", userSession.user.getIdentity())
      doc.save()
    } finally {
      db.close()
    }
  }

  def update(doc: ODocument, title: String, date: Date, description: String, done: Boolean): ODocument = {
    val db = dbpool.get
    try {
      doc.field("title", title)
      doc.field("date", date)
      doc.field("description", description)
      doc.field("completed", done)
      doc.save()
    } finally {
      db.close()
    }
  }

  def list(): List[ODocument] = {
    val db = dbpool.get
    try {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from Todo where user = ?"))
      cmd.execute(userSession.user.getIdentity())
    } finally {
      db.close()
    }
  }

  def load(id: ORID): ODocument = {
    val db = dbpool.get
    try {
      db.load(id)
    } finally {
      db.close()
    }
  }

}