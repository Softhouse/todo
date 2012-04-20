#!/bin/env python
import re
from collections import namedtuple 

_TODO_RE = re.compile("\* (TODO|DONE) (.*)")
_DATETIME_RE = "\d{2}\d{2}\d{2} \d{2}:\d{2}"
_DATE_RE = "\d{2}\d{2}\d{2}"

class Todo(namedtuple('Todo', 'caption done deadline contents')):
    def __repr__(self):
        return "%s %s%s%s" % ('DONE' if self.done else 'TODO',
                                  self.caption,
                                  ' ' + self.deadline if self.deadline else '',
                                  '\n\t' + self.contents if self.contents else '')

def _deadline_or_none(line):
    return re.findall(_DATETIME_RE, line) or re.findall(_DATE_RE, line) or None

def todo_parser(text):
    caption = None
    deadline = None
    contents = []
    done = False

    for line in text.split("\n"):
        match = _TODO_RE.match(line)
        if match:
            if caption:
                yield Todo(caption, done, deadline, "\n".join(contents))
                contents = []
            done = match.group(1) == 'DONE'
            caption = match.group(2)
            deadline = _deadline_or_none(line)
        elif caption:
            contents.append(line.strip())
    if caption:
        yield Todo(caption, done, deadline, "\n".join(contents))


def todo_coder(todos):
    out = []
    for todo in todos:
        out += ["* %s %s%s" % ('DONE' if todo.done else 'TODO',
                              todo.caption,
                              todo.deadline if todo.deadline else "")]
        for line in todo.contents.split('\n'):
            out += ["  " + line]
    return "\n".join(out)

def load_todo(path):
    with open(path) as source:
        data = source.read()
    return list(todo_parser(data))

def save_todo(path, todos):
    with open(path, "w") as dest:
        dest.write(todo_coder(todos))

def cmdline_parse(raw):
    if not '---' in raw:
        deadline = _deadline_or_none(raw)
        return Todo(raw.strip(), False, deadline, '')

    data = raw.split('---')
    caption = data[0].strip()
    content = "\n".join(data[1:]).strip()
    deadline = _deadline_or_none(caption)
    return Todo(caption, False,deadline, content)

def cmd_add(path, raw):
    todo = cmdline_parse(raw)
    todos = load_todo(path)
    todos = [todo] + todos
    save_todo(path, todos)

def cmd_view(path):
    todos = load_todo(path)
    for i, todo in zip(range(1, len(todos)+1), todos):
        print(i, str(todo))

def cmd_done(path, index):
    """1-based index!"""
    index = int(index)
    todos = load_todo(path)
    todo = todos.pop(index-1)
    todos.append(todo._replace(done=True))
    save_todo(path, todos)

def cmd_delete(path, index):
    """1-based index!"""
    index = int(index)
    todos = load_todo(path)
    todo = todos[index-1]
    del todos[index-1]
    save_todo(path, todos)

def cmd_init(path):
    save_todo(path, [])

if __name__ == "__main__":
    import sys, os
    if len(sys.argv) < 2: sys.exit()
    path = os.environ['HOME'] + "/.plan"
    fn = globals()['cmd_%s' % sys.argv[1]]
    if sys.argv[2:]:
        fn(path, " ".join(sys.argv[2:]))
    else:
        fn(path)






    
