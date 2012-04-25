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
import javax.annotation.PostConstruct
import scala.concurrent.ops.spawn
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit
import java.util.Date
import scala.compat.Platform
import scala.collection.JavaConversions.{ iterableAsScalaIterable => _, _ }
import javax.annotation.PreDestroy

/**
 * @author Mikael Svahn
 *
 */
@Component
class TimerService {

  @volatile var running = true;
  val queue = new DelayQueue[QueueEntry]();

  @PostConstruct
  def start() {
    spawn {
      while (running) {
        val entry = queue.take();
        if (entry != null && entry.action != null) {
          entry.action(entry.key)
        }
      }
    }
  }

  @PreDestroy
  def stop() {
    running = false
    queue.add(new QueueEntry(null, new Date(), null))
  }

  def remove(filter: Any => Boolean) {
    queue.remove(queue.find(i => filter(i.key)))
  }

  def add(todo: Any, timeout: Date, action: Any => Unit) {
    queue.add(new QueueEntry(todo, timeout, action))
  }

  class QueueEntry(val key: Any, val timeout: Date, val action: Any => Unit) extends Delayed {

    def compareTo(entry: Delayed): Int = {
      return timeout.compareTo(entry.asInstanceOf[QueueEntry].timeout)
    }

    def getDelay(unit: TimeUnit): Long = {
      unit.convert(timeout.getTime() - Platform.currentTime, TimeUnit.MILLISECONDS)
    }
  }
}
