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

/**
 * @author Mikael Svahn
 *
 */
@Component
class UserManager {

  @transient
  @Resource
  val dbpool: DatabasePool = null

  def onLogin(username: String, password: String): ODocument = {
    val db = dbpool.get
    try {

      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from Users where username = ?"))
      val result: List[ODocument] = cmd.execute(username)

      if (result.size() > 0) {
        val user = result.get(0);
        val pwd: String = user.field("password")
        if (pwd == password) user else null
      } else {
        return null
      }
    } finally {
      db.close()
    }
  }

  def doRegister(username: String, password: String): Boolean = {
    val db = dbpool.get
    try {
      try {
        val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from Users where username = ?"))
        val result: List[ODocument] = cmd.execute(username)
        if (result.size() > 0) {
          return false
        }
      } catch {
        case e: Exception => {}
      }
      val doc = db.newInstance("Users")
      doc.field("username", username)
      doc.field("password", password)
      doc.save()
      return true
    } finally {
      db.close()
    }
  }

}