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

package sample;

import javax.annotation.PostConstruct
import javax.annotation.Resource
import org.springframework.stereotype.Component
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool
import javax.annotation.PreDestroy
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx

/**
 * @author Mikael Svahn
 *
 */
@Component
class DatabasePool {

  private val pool: ODatabaseDocumentPool = new ODatabaseDocumentPool()

  var path: String = "local:" + System.getProperty("user.home") + "/sampledb"

  @PostConstruct
  def postConstruct() {
    pool.setup()
    val db = new ODatabaseDocumentTx(path)
    if (!db.exists()) {
      db.create()
      db.close()
    }
  }

  @PreDestroy
  def preDestroy() {
    pool.close()
  }

  def get: ODatabaseDocumentTx = {
    pool.acquire(path, "admin", "admin")
  }

  def execute[T](f: ODatabaseDocumentTx => T): T = {
    val db = get
    try {
      f(db)
    } finally {
      db.close()
    }
  }
}
